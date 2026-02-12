package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.icm.*;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.*;
import cn.idicc.taotie.infrastructment.request.icm.ChainStartProducingDTO;
import cn.idicc.taotie.infrastructment.request.icm.RecordVersionCheckRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordVersionQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordVersionDTO;
import cn.idicc.taotie.infrastructment.response.icm.VersionProduceStatusDTO;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.service.client.RedisClient;
import cn.idicc.taotie.service.services.icm.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2025/1/3
 * @Description:
 * @version: 1.0
 */
@Service
public class RecordVersionServiceImpl implements RecordVersionService {

	private static final Logger                       log = LoggerFactory.getLogger(RecordVersionServiceImpl.class);
	@Resource(name = "redisClientInteger")
	private              RedisClient<String, Integer> redisClient;

	@Autowired
	private RecordVersionMapper recordVersionMapper;

	@Autowired
	private RecordIndustryChainMapper industryChainMapper;

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	@Autowired
	private RecordVersionChangeFlowMapper recordVersionChangeFlowMapper;

	@Autowired
	private RecordIndustryLabelMapper industryLabelMapper;

	@Autowired
	private RecordAppEnterpriseIndustryChainSuspectedMapper industryChainSuspectedMapper;

	@Autowired
	private RecordAgentProductMatchesMapper recordAgentProductMatchesMapper;

	@Autowired
	private RecordIndustryChainNodeService industryChainNodeService;

	@Autowired
	private RecordIndustryChainNodeMapper industryChainNodeMapper;

	@Autowired
	private RecordIndustryLabelMapper recordIndustryLabelMapper;

	@Autowired
	private DwSyncService dwSyncService;

	@Autowired
	private ChainVectorService chainVectorService;

	@Autowired
	private ChainNodeRefAtomNodeMapper chainNodeRefAtomNodeMapper;


	@Override
	public void chainStartOnline(Long chainId, Boolean isDw, Boolean isChain) {

		RecordIndustryChainDO chainDO = industryChainMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.eq(RecordIndustryChainDO::getBizId, chainId)
				.eq(RecordIndustryChainDO::getDeleted, false)
		);
		if (chainDO == null) {
			log.warn("未找到行业链:{}", chainId);
			return;
		}

		String lockKey = String.format("chainStartOnline:%s", chainId);
		RLock  rLock   = redisClient.getLock(lockKey);
		try {
			if (rLock.tryLock(0, TimeUnit.MINUTES)) {
				if (isDw) {
					try {
						dwSyncService.dwOnline(chainDO);
					} catch (Exception e) {
						log.error("数据生成，上线异常", e);
						throw new BizException("数据上线异常");
					}
				}
				if (isChain) {
					try {
						recordIndustryChainService.exportChain(chainDO);
					} catch (Exception e) {
						log.error("数据生成，上线异常", e);
						throw new BizException("数据上线异常");
					}
				}
			}
		} catch (BizException e) {
			throw e;
		} catch (Exception e) {
			log.error("上线同步异常", e);
			throw new BizException("上线失败");
		} finally {
			if (rLock.isHeldByCurrentThread()) {
				rLock.unlock();
			}
		}
	}

	@Override
	public void chainStartProducing(ChainStartProducingDTO producingDTO) {
		RecordIndustryChainDO industryChainDO = industryChainMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.eq(RecordIndustryChainDO::getBizId, producingDTO.getChainId())
		);

		if (industryChainDO.getState() >= RecordChainStateEnum.PRODUCING.getValue() &&
				industryChainDO.getState() < RecordChainStateEnum.ERROR.getValue()) {
			throw new BizException("该产业链正在生产中");
		}

		List<RecordIndustryLabelDO> labelDOS = recordIndustryLabelMapper.getRelationErrorCount(producingDTO.getChainId());
		if (!labelDOS.isEmpty()) {
			log.error("存在标签关联多个产业链异常情况:chainId:{},data:{}", producingDTO.getChainId(), labelDOS);
			throw new BizException("存在标签关联多个产业链异常情况");
		}

		//把最新的产业链结构向量化
		chainVectorService.buildVector(producingDTO.getChainId());

		//更产业链为生产中
		industryChainDO.setState(RecordChainStateEnum.PRODUCING.getValue());
		industryChainMapper.updateById(industryChainDO);
	}

	@Override
	public void chainStopProducing(String chainId) {
		RecordIndustryChainDO industryChainDO = industryChainMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.eq(RecordIndustryChainDO::getBizId, chainId));
		if (industryChainDO == null) {
			throw new BizException("该产业链不存在");
		}
		industryChainDO.setState(RecordChainStateEnum.NORMAL.getValue());

		industryChainMapper.updateById(industryChainDO);
	}


	@Override
	public IPage<RecordVersionDTO> pageChainProduceRecords(RecordVersionQueryRequest request) {
		return recordVersionMapper.produceChainRecords(
				Page.of(request.getPageNum(), request.getPageSize()),
				request.getChainId());
	}

	@Override
	public IPage<RecordVersionDTO> pageChainOnlineRecords(RecordVersionQueryRequest request) {
		return recordVersionMapper.onlineChainRecords(
				Page.of(request.getPageNum(), request.getPageSize()),
				request.getChainId());
	}

	@Override
	public List<VersionProduceStatusDTO> getProducingStatus(String chainId) {
		List<VersionProduceStatusDTO> res = new ArrayList<>();

		RecordIndustryChainDO industryChainDO = industryChainMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.eq(RecordIndustryChainDO::getBizId, chainId));
		if (industryChainDO == null) {
			return res;
		}

		//产品找企业 进度
		Long totalLabel = industryLabelMapper.countForJobDetailByChainId(Long.valueOf(chainId), null);
		Long completeLabel = industryLabelMapper.countForJobDetailByChainId(Long.valueOf(chainId),
				RecordChainStateEnum.LABEL_FIND_ENTERPRISE_END.getValue());

		//企业找产品 进度
		Long totalEnterprise = industryChainSuspectedMapper.selectCount(Wrappers.lambdaQuery(RecordAppEnterpriseIndustryChainSuspectedDO.class)
				.eq(RecordAppEnterpriseIndustryChainSuspectedDO::getIndustryChainId, Long.valueOf(chainId))
				.eq(RecordAppEnterpriseIndustryChainSuspectedDO::getNegative, 0)
		);
		Long completeEnterprise = industryChainSuspectedMapper.selectCount(Wrappers.lambdaQuery(RecordAppEnterpriseIndustryChainSuspectedDO.class)
				.eq(RecordAppEnterpriseIndustryChainSuspectedDO::getIndustryChainId, Long.valueOf(chainId))
				.eq(RecordAppEnterpriseIndustryChainSuspectedDO::getNegative, 0)
				.ge(RecordAppEnterpriseIndustryChainSuspectedDO::getStatus, RecordChainStateEnum.ENTERPRISE_FIND_LABEL_END.getValue())
		);

		if (industryChainDO.getState() == RecordChainStateEnum.NORMAL.getValue()) {
			res.add(new VersionProduceStatusDTO("产品找企业", totalLabel, completeLabel, completeLabel >= totalLabel ? "搜索完成" : "待进行"));
			res.add(new VersionProduceStatusDTO("企业找产品", totalEnterprise, completeEnterprise, completeEnterprise >= totalEnterprise ? "生产完成" : "待进行"));
		} else {
			res.add(new VersionProduceStatusDTO("产品找企业", totalLabel, completeLabel, completeLabel >= totalLabel ? "搜索完成" : "生产中"));
			res.add(new VersionProduceStatusDTO("企业找产品", totalEnterprise, completeEnterprise, completeEnterprise >= totalEnterprise ? "生产完成" : "生产中"));
		}


//		挂载进度
		Long completeProduct = recordAgentProductMatchesMapper.selectCount(Wrappers.lambdaQuery(RecordAgentProductMatchesDO.class)
				.eq(RecordAgentProductMatchesDO::getIndustryChainId, chainId)
				.ge(RecordAgentProductMatchesDO::getStatus, RecordChainStateEnum.MATCH_ENTERPRISE_END.getValue()));
		Long totalProduct = recordAgentProductMatchesMapper.selectCount(Wrappers.lambdaQuery(RecordAgentProductMatchesDO.class)
				.eq(RecordAgentProductMatchesDO::getIndustryChainId, chainId));
		res.add(new VersionProduceStatusDTO("挂载进度", totalProduct, completeProduct, completeProduct >= totalProduct ? "挂载完成" : "挂载中"));
		return res;
	}

	@Override
	public void checkExport(RecordVersionCheckRequest request, HttpServletResponse response) {

		RecordIndustryChainDO chainDO = industryChainMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.eq(RecordIndustryChainDO::getBizId, request.getChainId())
		);
		if (request.getNodeId() == null) {
			RecordIndustryChainNodeDO nodeDO = industryChainNodeMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
					.eq(RecordIndustryChainNodeDO::getChainId, chainDO.getBizId())
					.eq(RecordIndustryChainNodeDO::getNodeParent, 0L)
					.last("limit 1")
			);
			if (nodeDO != null) {
				request.setNodeId(nodeDO.getBizId());
			}
		}
		if (request.getNodeId() == null) {
			throw new BizException("无节点信息");
		}

		List<RecordIndustryChainNodeDO> chainNodeDOS = Lists.newArrayList();
		industryChainNodeService.queryChildNodes(request.getNodeId(), chainNodeDOS);
		Map<String, Long> nodeCountMap = chainNodeDOS.stream().filter(e -> e.getIsLeaf().equals(1)).collect(Collectors.toConcurrentMap(RecordIndustryChainNodeDO::getNodeName, e -> 0L));
		if (chainNodeDOS.isEmpty()) {
			return;
		}
		List<Long> labelIds = chainNodeRefAtomNodeMapper.selectNodeRefLabelByNodeIdsAndLabelIds(
				null,
				chainNodeDOS.stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet())
		).stream().map(RecordIndustryChainNodeLabelRelationDO::getIndustryLabelId).collect(Collectors.toList());
		List<List<Object>> sheetData = new ArrayList<>();
		sheetData.add(Arrays.asList(
				"企业名称",
				"企业产品",
				"产品描述",
				"产品用途",
				"产品来源",
				"产品标签",
				"节点名称",
				"匹配度",
				"匹配理由"
		));
		List<ProductMatchesDTO> res1 = dwSyncService.exportMatchesData(chainDO.getBizId(), request.getIsLimitScore(), labelIds);
		List<ProductMatchesDTO> res2 = dwSyncService.exportTypicalData(chainDO, labelIds);
		List<ProductMatchesDTO> res  = dwSyncService.mergeData(res1, res2);
		res.forEach(e -> {
			Long cnt = nodeCountMap.get(e.getIndustryChainNodeName());
			if (cnt == null) {
				log.error("异常的挂载节点: {} {}", chainDO.getChainName(), e.getIndustryChainNodeName());
				cnt = 1L;
			} else {
				cnt += 1;
			}
			nodeCountMap.put(e.getIndustryChainNodeName(), cnt);
			sheetData.add(Arrays.asList(
					e.getEnterpriseName() == null || e.getEnterpriseName().isEmpty() ? null : e.getEnterpriseName(),
					e.getProductName() == null || e.getProductName().isEmpty() ? null : e.getProductName(),
					e.getProductDescription() == null || e.getProductDescription().isEmpty() ? null : e.getProductDescription(),
					e.getProductPurpose() == null || e.getProductPurpose().isEmpty() ? null : e.getProductPurpose(),
					e.getProductUrl() == null || e.getProductUrl().isEmpty() ? null : e.getProductUrl(),
					e.getIndustryLabelName() == null || e.getIndustryLabelName().isEmpty() ? null : e.getIndustryLabelName(),
					e.getIndustryChainNodeName() == null || e.getIndustryChainNodeName().isEmpty() ? null : e.getIndustryChainNodeName(),
					e.getMatchedProductScore(),
					e.getExtraReason() == null || e.getExtraReason().isEmpty() ? null : e.getExtraReason()
			));
		});
		List<List<Object>> sheetData2 = new ArrayList<>();
		sheetData2.add(Arrays.asList(
				"节点名称",
				"统计值"
		));
		nodeCountMap.forEach((key, value) -> sheetData2.add(Arrays.asList(
				key,
				value
		)));
		Map<String, List<List<Object>>> map = new HashMap<>();
		map.put("节点统计", sheetData2);
		map.put("质检名单", sheetData);
		ExcelUtils.export(response, null,
				String.format("%s[%s]质检名单", chainDO.getChainName(), "null"),
				map, null);
	}

	private String generateVersion(String businessUk, String businessRelationKey, Integer sort) {
		return businessUk + "." + businessRelationKey + "." + sort;
	}


}
