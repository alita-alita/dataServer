package cn.idicc.taotie.service.services.icm.impl;

import cn.hutool.json.JSONUtil;
import cn.idicc.identity.interfaces.config.login.UserContext;
import cn.idicc.taotie.infrastructment.constant.TopicConstant;
import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.icm.*;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckRecordStateEnum;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.*;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesAiCheckQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesAiCheckDTO;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.service.client.KafkaClient;
import cn.idicc.taotie.service.services.icm.RecordAiCheckRecordService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesAiCheckService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @Author: MengDa
 * @Date: 2025/4/2
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class RecordProductMatchesAiCheckServiceImpl implements RecordProductMatchesAiCheckService {

	@Autowired
	private RecordProductMatchesAiCheckMapper productMatchesAiCheckMapper;

	@Autowired
	private RecordAppProductMapper recordAppProductMapper;

	@Autowired
	private RecordIndustryLabelMapper recordIndustryLabelMapper;

	@Autowired
	private RecordIndustryChainMapper recordIndustryChainMapper;

	@Autowired
	private RecordIndustryChainNodeMapper recordIndustryChainNodeMapper;

	@Autowired
	private RecordProductMatchesAiCheckMapper recordProductMatchesAiCheckMapper;

	@Autowired
	private RecordBlacklistKeywordsMapper recordBlacklistKeywordsMapper;

	@Autowired
	private RecordAiCheckRecordService aiCheckRecordService;

	@Autowired
	private IndustryLabelRefProductMapper industryLabelRefProductMapper;

	@Autowired
	private ChainNodeRefAtomNodeMapper chainNodeRefAtomNodeMapper;

	@Autowired
	private KafkaClient kafkaClient;

	private final static Integer BATCH_SIZE = 1_0000;

	@Override
	public void createAICheckTask(Long chainId, List<RecordAgentProductMatchesDO> matchesDTOS) {
		List<RecordIndustryLabelDO> industryLabelList = recordIndustryLabelMapper.getLabelByChainId(chainId);
		if (industryLabelList.isEmpty()) {
			log.error("未找到行业标签,chain id:{}", chainId);
			return;
		}
		Map<String, RecordIndustryLabelDO> industryLabelMap = industryLabelList
				.stream().collect(toMap(RecordIndustryLabelDO::getLabelName, e -> e));

		List<RecordIndustryChainNodeDO> nodeList = recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getChainId, chainId)
				.eq(RecordIndustryChainNodeDO::getIsLeaf, true)
				.eq(RecordIndustryChainNodeDO::getDeleted, false));

		Map<Long, RecordIndustryChainNodeDO> nodeMaps = nodeList.stream()
				.collect(toMap(RecordIndustryChainNodeDO::getBizId, e -> e));

		Set<Long> nodeIds = nodeList.stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet());

		List<RecordIndustryChainNodeLabelRelationDO> industryLabelRefNodeMap = chainNodeRefAtomNodeMapper.getRelationByNodeIds(nodeIds);

		Map<Long, RecordIndustryChainNodeDO> industryLabelMapNode = industryLabelRefNodeMap
				.stream().collect(
						toMap(e -> (Long) e.getIndustryLabelId(),
								e -> nodeMaps.get(e.getChainNodeId())));
		if (industryLabelMapNode.isEmpty()) {
			log.error("未找到节点数据,chain id:{}", chainId);
			return;
		}

		log.info("chainId:{}, ai check do insert start", chainId);
		int cnt = 0;
		for (RecordAgentProductMatchesDO recordAgentProductMatchesDO : matchesDTOS) {
			RecordProductMatchesAiCheckDO recordProductMatchesAiCheckDO = createRecordProductMatchesAiCheckDO(chainId, recordAgentProductMatchesDO, industryLabelMap, industryLabelMapNode);
			if (recordProductMatchesAiCheckDO != null) {
				recordProductMatchesAiCheckMapper.insert(recordProductMatchesAiCheckDO);
			}
			log.info("version:{}, ai check do insert, cnt:{}/{}", chainId, ++cnt, matchesDTOS.size());
		}
		log.info("version:{}, ai check do insert end", chainId);
//        sendKafkaByChainId(chainId);
	}

	private RecordProductMatchesAiCheckDO createRecordProductMatchesAiCheckDO(Long chainId, RecordAgentProductMatchesDO matchesDTO,
																			  Map<String, RecordIndustryLabelDO> industryLabelMap,
																			  Map<Long, RecordIndustryChainNodeDO> industryLabelMapNode) {


		RecordProductMatchesAiCheckDO aiCheckDO = new RecordProductMatchesAiCheckDO();
		aiCheckDO.setChainId(chainId);

		aiCheckDO.setProductId(matchesDTO.getProductId());
		aiCheckDO.setProductName(matchesDTO.getProductName());

		RecordAppProductDO recordAppProductDO = recordAppProductMapper.selectByBizId(matchesDTO.getProductId());
		if (recordAppProductDO == null) {
			log.warn("chain_id:{},产品不存在：{}", chainId, matchesDTO.getProductId());
			return null;
		}
		long effectRows = recordProductMatchesAiCheckMapper.selectCount(Wrappers.lambdaQuery(RecordProductMatchesAiCheckDO.class)
				.eq(RecordProductMatchesAiCheckDO::getChainId, chainId)
				.eq(RecordProductMatchesAiCheckDO::getProductId, matchesDTO.getProductId())
				.eq(RecordProductMatchesAiCheckDO::getEnterpriseId, recordAppProductDO.getEnterpriseId()));
		if (effectRows > 0) {
			log.warn("产业AI质检，chain_id:{}, enterprise_id:{},产品已存在：{}", chainId, recordAppProductDO.getEnterpriseId(), matchesDTO.getProductId());
			return null;
		}
		if (StringUtils.isAnyEmpty(recordAppProductDO.getEnterpriseId(),
				recordAppProductDO.getEnterpriseName(),
				recordAppProductDO.getEnterpriseUniCode())) {
			log.warn("产业AI质检: 企业信息不全,product biz id:{},{},{},{}", recordAppProductDO.getBizId(),
					recordAppProductDO.getEnterpriseId(),
					recordAppProductDO.getEnterpriseName(),
					recordAppProductDO.getEnterpriseUniCode());
			return null;
		}
		aiCheckDO.setEnterpriseId(recordAppProductDO.getEnterpriseId());
		aiCheckDO.setEnterpriseName(recordAppProductDO.getEnterpriseName());
		aiCheckDO.setEnterpriseUniCode(recordAppProductDO.getEnterpriseUniCode());

		aiCheckDO.setProductUrl(recordAppProductDO.getProductUrl());
		aiCheckDO.setProductDescription(recordAppProductDO.getProductDescription());
		aiCheckDO.setProductPurpose(recordAppProductDO.getProductPurpose());

		RecordIndustryLabelDO labelDO = industryLabelMap.get(matchesDTO.getMatchedProduct());
		if (labelDO == null) {
			log.error("出现了有产品未关联产业链标签的数据,chain id:{}, matchedProduct:{}", chainId, matchesDTO.getMatchedProduct());
			return null;
		}
		aiCheckDO.setLabelId(labelDO.getBizId());
		aiCheckDO.setLabelName(labelDO.getLabelName());

		RecordIndustryChainNodeDO nodeDO = industryLabelMapNode.get(labelDO.getBizId());
		if (nodeDO == null) {
			log.error("出现了有标签未关联节点的数据,chain id:{}, labelId:{}", chainId, labelDO.getBizId());
			return null;
		}
		if (nodeDO.getThresholdScore().compareTo(matchesDTO.getMatchedProductScore()) > 0) {
			return null;
		}
		aiCheckDO.setNodeId(nodeDO.getBizId());
		aiCheckDO.setNodeName(nodeDO.getNodeName());

		aiCheckDO.setMatchedScore(matchesDTO.getMatchedProductScore());

		aiCheckDO.setStatus(RecordAiCheckRecordStateEnum.DEFAULT.getCode());


		return aiCheckDO;
	}


	@Override
	public void sendAllFailKafkaByVersion(Long chainId) {
		log.info("chainId:{}, ai check fail send kafka start", chainId);
		List<RecordProductMatchesAiCheckDO> checkDOList = productMatchesAiCheckMapper.selectList(Wrappers.lambdaQuery(RecordProductMatchesAiCheckDO.class)
				.eq(RecordProductMatchesAiCheckDO::getChainId, chainId)
				.eq(RecordProductMatchesAiCheckDO::getStatus, RecordAiCheckRecordStateEnum.ERROR.getCode())
		);
		for (RecordProductMatchesAiCheckDO checkRecordDO : checkDOList) {
			checkRecordDO.setStatus(RecordAiCheckRecordStateEnum.DEFAULT.getCode());
			productMatchesAiCheckMapper.updateRecordState(checkRecordDO.getId(), RecordAiCheckRecordStateEnum.DEFAULT.getCode());
			kafkaClient.sendSync(TopicConstant.PRODUCT_AI_CHECK_1, JSONUtil.toJsonStr(checkRecordDO));
		}
		log.info("chainId:{}, ai check  fail send kafka end", chainId);
	}

	@Override
	public void sendKafkaByChainId(Long chainId) {
		log.info("chainId:{}, ai check send kafka start", chainId);
		List<RecordProductMatchesAiCheckDO> checkDOList = productMatchesAiCheckMapper.selectList(Wrappers.lambdaQuery(RecordProductMatchesAiCheckDO.class)
				.eq(RecordProductMatchesAiCheckDO::getChainId, chainId));
		for (RecordProductMatchesAiCheckDO checkRecordDO : checkDOList) {
			kafkaClient.sendSync(TopicConstant.PRODUCT_AI_CHECK_1, JSONUtil.toJsonStr(checkRecordDO));
		}
		log.info("chainId:{}, ai check send kafka end", chainId);
	}

	@Override
	public Long countByChainId(Long chainId, List<RecordAiCheckRecordStateEnum> stateEnums) {
		return productMatchesAiCheckMapper.selectCount(Wrappers.lambdaQuery(RecordProductMatchesAiCheckDO.class)
//                .eq(RecordProductMatchesAiCheckDO::getVersion,version)
				.in(RecordProductMatchesAiCheckDO::getStatus, stateEnums.stream().map(RecordAiCheckRecordStateEnum::getCode).collect(Collectors.toList())));
	}

	@Override
	public IPage<RecordProductMatchesAiCheckDTO> pageList(RecordProductMatchesAiCheckQueryRequest request) {
		long start = (request.getPageNum() - 1) * request.getPageNum();

		long total = productMatchesAiCheckMapper.pageCount(request.getChainId(),
				request.getStatus(),
				request.getLabelId(), request.getNodeId(),
				request.getEnterpriseName(), request.getProductName());


		List<RecordProductMatchesAiCheckDTO> pageData = productMatchesAiCheckMapper.pageList(
				start, request.getPageSize(),
				request.getChainId(),
				request.getStatus(),
				request.getLabelId(), request.getNodeId(),
				request.getEnterpriseName(), request.getProductName());

		IPage<RecordProductMatchesAiCheckDTO> page = new Page<>(request.getPageNum(), request.getPageSize(), total);
		page.setRecords(pageData);
		return page;
	}

	@Override
	public void exportFileList(RecordProductMatchesAiCheckQueryRequest request, HttpServletResponse response) {
//		IPage<RecordProductMatchesAiCheckDTO> page = null;
		List<RecordProductMatchesAiCheckDTO>  res  = new ArrayList<>();
//        List<RecordProductMatchesAiCheckDTO> res = productMatchesAiCheckMapper.allList(
//                request.getVersion(),
//                request.getStatus(),
//                request.getLabelId(),request.getNodeId(),
//                request.getEnterpriseName(), request.getProductName());
		int cnt = 1;
		long total = productMatchesAiCheckMapper.pageCount(request.getChainId(),
				request.getStatus(),
				request.getLabelId(), request.getNodeId(),
				request.getEnterpriseName(), request.getProductName());
		do {
			long start = (cnt - 1) * BATCH_SIZE;
			cnt ++;
			List<RecordProductMatchesAiCheckDTO> pageData = productMatchesAiCheckMapper.pageList(
					start, BATCH_SIZE,
					request.getChainId(),
					request.getStatus(),
					request.getLabelId(), request.getNodeId(),
					request.getEnterpriseName(), request.getProductName());
			if (pageData.isEmpty()) {
				break;
			}
			res.addAll(pageData);
			log.info("产业链质检结果数据导出中,版本:{},已导出数据量:{},进度:{}/{}",
					request.getVersion(),
					res.size(),
					start,
					(start + BATCH_SIZE) / total);
		} while (true);
		List<List<Object>> sheetData = new ArrayList<>();
		sheetData.add(Arrays.asList(
				"ID",
				"企业名称",
				"产品名称",
				"社会信用代码",
				"产品用途",
				"产品描述",
				"产品URL",
				"标签名",
				"标签定义",
				"节点名称",
				"节点定义",
				"匹配分数",
				"匹配理由",
				"质检结果",
				"状态"
		));
		res.forEach(e -> {
			sheetData.add(Arrays.asList(
					e.getId(),
					e.getEnterpriseName() == null || e.getEnterpriseName().isEmpty() ? null : e.getEnterpriseName(),
					e.getProductName() == null || e.getProductName().isEmpty() ? null : e.getProductName(),
					e.getEnterpriseUniCode() == null || e.getEnterpriseUniCode().isEmpty() ? null : e.getEnterpriseUniCode(),
					e.getProductPurpose() == null || e.getProductPurpose().isEmpty() ? null : e.getProductPurpose(),
					e.getProductDescription() == null || e.getProductDescription().isEmpty() ? null : e.getProductDescription(),
					e.getProductUrl() == null || e.getProductUrl().isEmpty() ? null : e.getProductUrl(),
					e.getLabelName() == null || e.getLabelName().isEmpty() ? null : e.getLabelName(),
					e.getLabelDesc() == null || e.getLabelDesc().isEmpty() ? null : e.getLabelDesc(),
					e.getNodeName() == null || e.getNodeName().isEmpty() ? null : e.getNodeName(),
					e.getNodeDesc() == null || e.getNodeDesc().isEmpty() ? null : e.getNodeDesc(),
					e.getMatchedScore() == null ? null : e.getMatchedScore(),
					e.getMatchReason() == null || e.getMatchReason().isEmpty() ? null : e.getMatchReason(),
					e.getCheckReason() == null || e.getCheckReason().isEmpty() ? null : e.getCheckReason(),
					e.getStatus() == null ? null : RecordAiCheckRecordStateEnum.getByCode(e.getStatus()).getDesc()
			));
		});
		Map<String, List<List<Object>>> map = new HashMap<>();
		map.put("AI质检名单", sheetData);
		ExcelUtils.export(response, null,
				"AI质检名单",
				map, null);
	}

	@Override
	public void updateRecordState(Long id, RecordAiCheckRecordStateEnum stateEnum) {
		RecordProductMatchesAiCheckDO recordProductMatchesAiCheckDO = productMatchesAiCheckMapper.selectById(id);
		if (recordProductMatchesAiCheckDO == null) {
			return;
		}
		productMatchesAiCheckMapper.updateRecordState(id, stateEnum.getCode());
	}

	@Override
	public void updateAllRecordState(Long chainId, RecordAiCheckRecordStateEnum oldStateEnum, RecordAiCheckRecordStateEnum newStateEnum) {
		if (oldStateEnum.equals(RecordAiCheckRecordStateEnum.PASS)) {
			throw new BizException("不被允许的操作");
		}
		//更新AI质检结果
		productMatchesAiCheckMapper.updateAllRecordState(chainId, oldStateEnum.getCode(), newStateEnum.getCode());

		long startId = 0;
		while (true) {
			List<RecordProductMatchesAiCheckDO> pages = productMatchesAiCheckMapper.selectList(Wrappers.lambdaQuery(RecordProductMatchesAiCheckDO.class)
					.eq(RecordProductMatchesAiCheckDO::getChainId, chainId)
					.eq(RecordProductMatchesAiCheckDO::getStatus, RecordAiCheckRecordStateEnum.PASS.getCode())
					.gt(RecordProductMatchesAiCheckDO::getId, startId)
					.orderByAsc(RecordProductMatchesAiCheckDO::getId)
					.last("limit 1000"));
			if (pages.isEmpty()) {
				log.info("质检数据同步至结果表已完成");
				break;
			}

			pages.forEach(row -> {
				IndustryLabelRefProductDO data = new IndustryLabelRefProductDO();
				IndustryLabelRefProductDO refProductDO = industryLabelRefProductMapper.selectOne(Wrappers.lambdaQuery(IndustryLabelRefProductDO.class)
						.eq(IndustryLabelRefProductDO::getIndustryLabelId, row.getLabelId())
						.eq(IndustryLabelRefProductDO::getProductId, row.getProductId())
						.last("limit 1"));
				if (refProductDO != null) {
					log.info("数据已存在:{}-{}", row.getLabelId(), row.getProductId());
					return;
				}
				data.setIndustryLabelId(row.getLabelId());
				data.setProductId(row.getProductId());
				data.setUpdateBy(UserContext.getUserName());
				data.setCreateBy(UserContext.getUserName());
				data.setGmtCreate(LocalDateTime.now());
				data.setGmtModify(LocalDateTime.now());
				industryLabelRefProductMapper.insert(data);
			});
			if (pages.size() < 1000) {
				log.info("质检数据同步至结果表已完成");
				break;
			}
			log.info("质检数据同步至结果表, {}:1000,已完成", startId);
			startId = pages.get(pages.size() - 1).getId();
		}

		aiCheckRecordService.aiCheckDataOnline(chainId, true);

		//更新产业链结果为 待同步集市
		recordIndustryChainMapper.update(null,
				Wrappers.lambdaUpdate(RecordIndustryChainDO.class)
						.set(RecordIndustryChainDO::getState, RecordChainStateEnum.WAITING_SYNC_DW.getValue())
						.eq(RecordIndustryChainDO::getBizId, chainId));
	}

	@Override
	public List<ProductMatchesDTO> selectAllPassData(Long chainId) {
		List<RecordBlacklistKeywordsDO> recordBlacklistKeywordsDOS = recordBlacklistKeywordsMapper.selectAll(chainId.intValue());
		Set<String>                     keywords                   = recordBlacklistKeywordsDOS.stream().map(RecordBlacklistKeywordsDO::getBlacklistKeywordsName).collect(Collectors.toSet());
		List<RecordProductMatchesAiCheckDO> matchesDOS = recordProductMatchesAiCheckMapper.selectList(
						Wrappers.lambdaQuery(RecordProductMatchesAiCheckDO.class)
								.eq(RecordProductMatchesAiCheckDO::getChainId, chainId)
								.eq(RecordProductMatchesAiCheckDO::getStatus, RecordAiCheckRecordStateEnum.PASS.getCode()))
				.stream().filter(e -> {
					for (String key : keywords) {
						if (e.getEnterpriseName().contains(key)) {
							return false;
						}
					}
					return true;
				}).collect(toList());
		return matchesDOS.stream().map(ProductMatchesDTO::fromAiCheckDO).collect(toList());
	}
}
