package cn.idicc.taotie.service.services.icm.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryChainIndustryLabelRelationDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.dw.DwdIndustryLabelDO;
import cn.idicc.taotie.infrastructment.entity.icm.*;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.dw.*;
import cn.idicc.taotie.infrastructment.mapper.icm.*;
import cn.idicc.taotie.infrastructment.utils.MD5Util;
import cn.idicc.taotie.service.client.RedisClient;
import cn.idicc.taotie.service.services.icm.DwSyncService;
import cn.idicc.taotie.service.services.icm.RecordIndustryLabelService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesAiCheckService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @Author: wangjun
 * @Date: 2025/1/9
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class DwSyncServiceImpl implements DwSyncService {

	@Resource(name = "redisClientInteger")
	private RedisClient<String, Integer> redisClient;

	@Autowired
	private RecordAppEnterpriseIndustryChainSuspectedMapper enterpriseIndustryChainSuspectedMapper;

	@Resource
	private RecordAgentProductMatchesMapper recordAgentProductMatchesMapper;

	@Resource
	private RecordAppTypicalEnterpriseMapper recordAppTypicalEnterpriseMapper;

	@Resource
	private DwdAllMapper dwdAllMapper;


	@Resource
	private DwdIndustryChainNodeMapper dwdIndustryChainNodeMapper;

	@Resource
	private DwdIndustryChainMapper dwdIndustryChainMapper;

	@Resource
	private DwdIndustryLabelMapper dwdIndustryLabelMapper;

	@Resource
	private DwdIndustryChainIndustryLabelRelationMapper dwdIndustryChainIndustryLabelRelationMapper;


	@Autowired
	private RecordIndustryChainNodeMapper industryChainNodeMapper;

	@Autowired
	private RecordIndustryLabelMapper industryLabelMapper;

	@Autowired
	private RecordProductMatchesDissociatedMapper recordProductMatchesDissociatedMapper;

	private final static Integer                            BATCH_SIZE = 10_0000;
	@Autowired
	private              RecordProductMatchesAiCheckService recordProductMatchesAiCheckService;
	@Autowired
	private              RecordIndustryChainMapper          recordindustryChainMapper;

	@Autowired
	private IndustryLabelRefProductMapper industryLabelRefProductMapper;
	@Autowired
	private RecordIndustryLabelService    recordIndustryLabelService;

	@Autowired
	private ChainNodeRefAtomNodeMapper chainNodeRefAtomNodeMapper;

	@Override
	public void dwOnline(RecordIndustryChainDO chainDO) {
		String lockKey = String.format("dwOnline:%s", chainDO.getChainName());
		RLock  rLock   = redisClient.getLock(lockKey);
		try {
			if (rLock.tryLock(0, TimeUnit.MINUTES)) {
				String date = DateUtil.formatDateTime(new Date());
				if (ObjectUtil.equals(chainDO.getState(), RecordChainStateEnum.WAITING_SYNC_DW.getValue())) {
					log.info("集市产业链结构入库中");
					importChainToDW(chainDO);
					log.info("集市产业链结构入库完成");
					syncIndustryLabelRefProductData(chainDO.getBizId(), chainDO.getChainName(), date);
//					List<ProductMatchesDTO> res1 = recordProductMatchesAiCheckService.selectAllPassData(chainDO.getBizId());
					List<ProductMatchesDTO> res2 = exportTypicalData(chainDO, null);
					List<ProductMatchesDTO> res3 = exportDissociatedData(chainDO, null);
					List<ProductMatchesDTO> res  = mergeData(res3, res2);
//					res = mergeData(res1, res);

					List<List<ProductMatchesDTO>> partitionRes = Lists.partition(new ArrayList<>(res), 1000);
					int                           cnt          = 0;
					for (List<ProductMatchesDTO> part : partitionRes) {
						if (!part.isEmpty()) {
							dwdAllMapper.insertOrUpdateProductBatch(part, date);
							dwdAllMapper.insertOrUpdateProductLabelRelationBatch(part, date);
							dwdAllMapper.insertOrUpdateEnterpriseLabelRelationBatch(part, date);
//							dwdAllMapper.insertOrUpdateEnterpriseChainRelationBatch(part, date, chainDO.getChainName());
						}
						log.info("集市数据入库中 {}/{}", ++cnt, partitionRes.size());
					}


					log.info("集市数据删除中");
					dwdAllMapper.deleteProductLabelRelationBatch(chainDO.getBizId(), date);
					dwdAllMapper.deleteEnterpriseLabelRelationBatch(chainDO.getBizId(), date);
//					dwdAllMapper.deleteEnterpriseChainRelationBatch(chainDO.getBizId(), date);

					log.info("集市数据删除完成");

					//merge 典型企业
					List<ProductMatchesDTO> typicalRes = new ArrayList<>(res2.stream()
							.collect(Collectors.groupingBy(
									ProductMatchesDTO::getEnterpriseChainRelationId,
									Collectors.collectingAndThen(
											Collectors.toList(),
											list -> {
												Set<String>       labels   = new HashSet<>();
												ProductMatchesDTO firstDto = null;
												for (ProductMatchesDTO dto : list) {
													if (firstDto == null) {
														firstDto = dto;
													}
													labels.add(dto.getIndustryLabelName());
												}
												firstDto.setIndustryLabelName(String.join("、", labels));
												firstDto.setProductName(String.join("、", labels));
												return firstDto;
											}
									)
							))
							.values());
					if (!typicalRes.isEmpty()) {
						log.info("典型企业入库中");
						dwdAllMapper.insertOrUpdateTypicalEnterpriseBatch(typicalRes, date, chainDO.getChainName());
					}
					if (!typicalRes.isEmpty()) {
						dwdAllMapper.deleteTypicalEnterpriseBatch(chainDO.getBizId(), date);
						log.info("典型企业数据删除完成");
					}

					//更新产业链状态
					recordindustryChainMapper.update(null,
							Wrappers.lambdaUpdate(RecordIndustryChainDO.class)
									.set(RecordIndustryChainDO::getState, RecordChainStateEnum.SYNC_DW_FINISH.getValue())
									.eq(RecordIndustryChainDO::getBizId, chainDO.getBizId()));
				}
			}
		} catch (Exception e) {
			log.error("dwOnline同步dw异常", e);
			throw new BizException("导出失败");
		} finally {
			if (rLock.isHeldByCurrentThread()) {
				rLock.unlock();
			}
		}
	}

	private void syncIndustryLabelRefProductData(Long chainId, String chainName, String date) {

		List<RecordIndustryLabelDO> refLabels   = recordIndustryLabelService.listAllLabelRefByChainId(chainId);
		Set<Long>                   refLabelIds = refLabels.stream().map(RecordIndustryLabelDO::getBizId).collect(Collectors.toSet());
		int                         start       = 0, size = 1000;
		while (true) {
			List<ProductMatchesDTO> pageData = industryLabelRefProductMapper.selectMatchedProducts(chainId, refLabelIds, start, size);
			if (pageData.isEmpty()) {
				log.info("{}:{}, 产品匹配结果已导入完成", chainId, chainName);
				break;
			}
			dwdAllMapper.insertOrUpdateProductBatch(pageData, date);
			dwdAllMapper.insertOrUpdateProductLabelRelationBatch(pageData, date);
			dwdAllMapper.insertOrUpdateEnterpriseLabelRelationBatch(pageData, date);
//			dwdAllMapper.insertOrUpdateEnterpriseChainRelationBatch(pageData, date, chainName);
			if (pageData.size() < size) {
				log.info("{}:{}, 产品匹配结果已导入完成", chainId, chainName);
				break;
			}
			start += size;
			log.info("{}:{}, 产品匹配结果导入进度:{}-{}", chainId, chainName, start, size);
		}
	}

	@Override
	public List<ProductMatchesDTO> exportMatchesData(Long chainId, Boolean isLimitScore, List<Long> filterLabelIds) {

		List<RecordIndustryChainNodeDO> industryChainNodeDOS = industryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getChainId, chainId)
		);
//		List<RecordIndustryLabelDO> industryLabelDOS = industryLabelMapper.selectList(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
//				.in(filterLabelIds != null && !filterLabelIds.isEmpty(), RecordIndustryLabelDO::getBizId, filterLabelIds)
//				.eq(RecordIndustryLabelDO::getChainId, chainId)
//		);
		List<RecordIndustryLabelDO>                  industryLabelDOS                  = industryLabelMapper.getLabelByChainId(chainId);
		Set<Long>                                    nodeIds                           = industryChainNodeDOS.stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet());
		Set<Long>                                    labelIds                          = industryLabelDOS.stream().map(RecordIndustryLabelDO::getBizId).collect(Collectors.toSet());
		List<RecordIndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOS = new ArrayList<>();
		if (!nodeIds.isEmpty() && !labelIds.isEmpty()) {
			industryChainNodeLabelRelationDOS = chainNodeRefAtomNodeMapper.selectNodeRefLabelByNodeIdsAndLabelIds(labelIds, nodeIds);
//			industryChainNodeLabelRelationDOS = industryChainNodeLabelRelationMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeLabelRelationDO.class)
//					.in(RecordIndustryChainNodeLabelRelationDO::getChainNodeId, nodeIds)
//					.in(RecordIndustryChainNodeLabelRelationDO::getIndustryLabelId, labelIds)
//			);
		}

		Map<Long, RecordIndustryChainNodeDO> nodeDOMap  = industryChainNodeDOS.stream().collect(Collectors.toMap(RecordIndustryChainNodeDO::getBizId, e -> e, (e1, e2) -> e1));
		Map<String, RecordIndustryLabelDO>   labelDOMap = industryLabelDOS.stream().collect(Collectors.toMap(RecordIndustryLabelDO::getLabelName, e -> e, (e1, e2) -> e1));
		Map<Long, List<Long>> labelRelationMap = industryChainNodeLabelRelationDOS.stream()
				.collect(Collectors.groupingBy(RecordIndustryChainNodeLabelRelationDO::getIndustryLabelId, Collectors.mapping(RecordIndustryChainNodeLabelRelationDO::getChainNodeId, toList())));

		List<ProductMatchesDTO> res  = new ArrayList<>();
		Page<ProductMatchesDTO> list = null;
		int                     cnt  = 1;
		do {
			list = recordAgentProductMatchesMapper.getResultMatchPage(
					new Page<>(cnt++, BATCH_SIZE, false),
					chainId
			);
			if (list.getRecords().isEmpty()) {
				break;
			}
			//过滤掉无统一信用代码的企业
			List<String> uniCodes = list.getRecords().stream()
					.filter(e -> e.getEnterpriseUniCode() != null && !e.getEnterpriseUniCode().isEmpty())
					.map(ProductMatchesDTO::getEnterpriseUniCode).collect(toList());
			if (uniCodes.isEmpty()) {
				continue;
			}
			//过滤黑名单企业并获取企业名称
			Map<String, RecordAppEnterpriseIndustryChainSuspectedDO> suspectedDOMap = enterpriseIndustryChainSuspectedMapper.selectPositive(chainId, uniCodes).stream()
					.collect(Collectors.toMap(RecordAppEnterpriseIndustryChainSuspectedDO::getEnterpriseUniCode, e -> e, (e1, e2) -> e1));
			list.setRecords(list.getRecords().stream().filter(e -> suspectedDOMap.containsKey(e.getEnterpriseUniCode()))
					.peek(e -> e.setEnterpriseName(suspectedDOMap.get(e.getEnterpriseUniCode()).getEnterpriseName()))
					.collect(toList()));
			//过滤-过滤无效的标签
			list.setRecords(list.getRecords().stream().filter(e -> labelDOMap.containsKey(e.getIndustryLabelName()))
					.peek(e -> e.setIndustryLabelId(labelDOMap.get(e.getIndustryLabelName()).getBizId().toString()))
					.collect(toList()));
			//设置节点ID及产业链ID
			for (ProductMatchesDTO e : list.getRecords()) {
				e.setIndustryChainId(chainId.toString());
				List<Long> tmpNodeIds = labelRelationMap.get(Long.valueOf(e.getIndustryLabelId()));
				if (tmpNodeIds == null || tmpNodeIds.isEmpty()) {
					log.error("产业链节点关系数据异常(无关联节点),产业链ID:{},标签ID:{}", chainId, e.getIndustryLabelId());
					continue;
				}
				if (tmpNodeIds.size() > 1) {
					log.error("产业链节点关系数据异常(超过1个节点),产业链ID:{},标签ID:{}", chainId, e.getIndustryLabelId());
				}
				RecordIndustryChainNodeDO nodeDO = nodeDOMap.get(tmpNodeIds.get(0));
				e.setIndustryChainNodeId(nodeDO.getBizId().toString());
				e.setIndustryChainNodeName(nodeDO.getNodeName());
			}
			//过滤无关联关系的节点
			list.setRecords(list.getRecords().stream().filter(e -> e.getIndustryChainNodeId() != null).collect(toList()));
			//过滤分数
			list.setRecords(list.getRecords().stream()
					.filter(e -> {
						BigDecimal score = new BigDecimal(e.getMatchedProductScore());
						if (!nodeDOMap.containsKey(Long.valueOf(e.getIndustryChainNodeId()))) {
							return false;
						}
						BigDecimal nodeScore = nodeDOMap.get(Long.valueOf(e.getIndustryChainNodeId())).getThresholdScore();
						return !isLimitScore || score.compareTo(nodeScore) > 0;

					})
					.collect(toList()));
			res.addAll(list.getRecords());
			log.info("产业链数据导出中,产业链ID:{},已导出数据量:{},进度:{}/{}", chainId, res.size(), list.getCurrent(), (list.getTotal() + list.getSize() - 1) / list.getSize());
		} while (true);


		return res;
	}

	@Override
	public List<ProductMatchesDTO> exportTypicalData(RecordIndustryChainDO chainDO, List<Long> filterLabelIds) {
		log.info("典型企业-产业链导出中 start");
		return recordAppTypicalEnterpriseMapper.queryByChainId(chainDO.getBizId())
				.stream()
				.filter(enterpriseDO -> filterLabelIds == null || filterLabelIds.isEmpty()
						|| filterLabelIds.contains(enterpriseDO.getIndustryLabelId()))
				.map(e -> {
					ProductMatchesDTO matchesDTO = new ProductMatchesDTO();
					matchesDTO.setProductId(MD5Util.getMd5Id(e.getIndustryLabelName() + e.getEnterpriseId()));
					matchesDTO.setProductName(e.getIndustryLabelName());
					matchesDTO.setEnterpriseId(e.getEnterpriseId());
					matchesDTO.setEnterpriseName(e.getEnterpriseName());
					matchesDTO.setEnterpriseUniCode(e.getEnterpriseUniCode());
					matchesDTO.setIndustryLabelId(e.getIndustryLabelId().toString());
					matchesDTO.setIndustryLabelName(e.getIndustryLabelName());
					matchesDTO.setMatchedProductScore("1.0");
					matchesDTO.setIndustryChainNodeId(e.getIndustryNodeId().toString());
					matchesDTO.setIndustryChainNodeName(e.getIndustryNodeName());
					matchesDTO.setIndustryChainId(chainDO.getBizId().toString());
					matchesDTO.setDataSource("典型企业");
					matchesDTO.setDeleted(0);
					return matchesDTO;
				}).collect(toList());
	}

	public List<ProductMatchesDTO> exportDissociatedData(RecordIndustryChainDO chainDO, List<Long> filterLabelIds) {
		log.info("游离企业-产业链导出中 start");
		List<RecordProductMatchesDissociatedDO> dissociatedDOS = recordProductMatchesDissociatedMapper.selectList(Wrappers.lambdaQuery(RecordProductMatchesDissociatedDO.class)
				.eq(RecordProductMatchesDissociatedDO::getChainId, chainDO.getBizId())
				.eq(RecordProductMatchesDissociatedDO::getStatus, 1)
				.in(filterLabelIds != null && !filterLabelIds.isEmpty(), RecordProductMatchesDissociatedDO::getLabelId, filterLabelIds)
		);
		dissociatedDOS = dissociatedDOS.stream().filter(e ->
				e.getEnterpriseUniCode() != null && !e.getEnterpriseUniCode().isEmpty()
						&& e.getEnterpriseId() != null && !e.getEnterpriseId().isEmpty()
						&& e.getEnterpriseName() != null && !e.getEnterpriseName().isEmpty()
						&& e.getLabelId() != null
		).collect(toList());
		List<ProductMatchesDTO> res = dissociatedDOS.stream().map(e -> {
			ProductMatchesDTO matchesDTO = new ProductMatchesDTO();
			matchesDTO.setProductId(e.getProductId());
			matchesDTO.setProductName(e.getProductName());
			matchesDTO.setProductUrl(e.getProductUrl());
			matchesDTO.setProductPurpose(e.getProductPurpose());
			matchesDTO.setProductDescription(e.getProductDescription());
			matchesDTO.setEnterpriseId(e.getEnterpriseId());
			matchesDTO.setEnterpriseName(e.getEnterpriseName());
			matchesDTO.setEnterpriseUniCode(e.getEnterpriseUniCode());
			matchesDTO.setIndustryLabelId(e.getLabelId().toString());
			matchesDTO.setIndustryLabelName(e.getLabelName());
			matchesDTO.setMatchedProductScore(e.getMatchedScore().toString());
			matchesDTO.setIndustryChainNodeId(e.getNodeId().toString());
			matchesDTO.setIndustryChainNodeName(e.getNodeName());
			matchesDTO.setIndustryChainId(e.getChainId().toString());
			matchesDTO.setExtraReason(e.getMatchReason());
			matchesDTO.setDataSource("游离企业-" + e.getDataSource());
			matchesDTO.setDeleted(0);
			return matchesDTO;
		}).collect(toList());
		log.info("游离企业-产业链导出中 end");
		return res;
	}

	@Override
	public List<ProductMatchesDTO> mergeData(List<ProductMatchesDTO> matchesDTOS, List<ProductMatchesDTO> typicalList) {
		log.info("产业链导出中,合并 start");
		Set<String> existSet = matchesDTOS.stream().map(ProductMatchesDTO::getProductLabelRelation).collect(Collectors.toSet());
		for (ProductMatchesDTO matchesDTO : typicalList) {
			if (!existSet.contains(matchesDTO.getProductLabelRelation())) {
				matchesDTOS.add(matchesDTO);
			}
		}
		log.info("产业链导出中,合并 end");
		return matchesDTOS;
	}

	private void importChainToDW(RecordIndustryChainDO chainFrom) {
		Long chainId = chainFrom.getBizId();
		List<RecordIndustryChainNodeDO> industryChainNodeDOS = industryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getChainId, chainId)
		);
		List<RecordIndustryLabelDO> industryLabelDOS = industryLabelMapper.getLabelByChainId(chainId);
		Set<Long>                   nodeIds          = industryChainNodeDOS.stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet());
		Set<Long>                   labelIds         = industryLabelDOS.stream().map(RecordIndustryLabelDO::getBizId).collect(Collectors.toSet());
//		List<RecordIndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOS = new ArrayList<>();
//		if (!nodeIds.isEmpty() && !labelIds.isEmpty()) {
//			industryChainNodeLabelRelationDOS = industryChainNodeLabelRelationMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeLabelRelationDO.class)
//					.in(RecordIndustryChainNodeLabelRelationDO::getChainNodeId, nodeIds)
//					.in(RecordIndustryChainNodeLabelRelationDO::getIndustryLabelId, labelIds)
//			);
//		}
		List<RecordIndustryChainNodeLabelRelationDO> nodeRefIndustryLabels = new ArrayList<>();
		if (!nodeIds.isEmpty() && !labelIds.isEmpty()) {
			log.info("nodeIds:{}", nodeIds);
			log.info("labelIds:{}", labelIds);
			nodeRefIndustryLabels = chainNodeRefAtomNodeMapper.selectNodeRefLabelByNodeIdsAndLabelIds(labelIds, nodeIds);
		}

		//适配格式
		DwdIndustryChainDO           chainDO      = DwdIndustryChainDO.adapter(chainFrom);
		List<DwdIndustryChainNodeDO> chainNodeDOS = industryChainNodeDOS.stream().map(e -> DwdIndustryChainNodeDO.adapter(e, chainDO.getIndustryChainName())).collect(toList());
		List<DwdIndustryLabelDO>     labelDOS     = industryLabelDOS.stream().map(e -> DwdIndustryLabelDO.adapter(e, chainId, chainDO.getIndustryChainName())).collect(toList());
		Map<Long, String>            labelNameMap = industryLabelDOS.stream().collect(Collectors.toMap(RecordIndustryLabelDO::getBizId, RecordIndustryLabelDO::getLabelName));
		Map<Long, String>            nodeNameMap  = industryChainNodeDOS.stream().collect(Collectors.toMap(RecordIndustryChainNodeDO::getBizId, RecordIndustryChainNodeDO::getNodeName));
		List<DwdIndustryChainIndustryLabelRelationDO> relationDOS = nodeRefIndustryLabels.stream()
				.map(e -> DwdIndustryChainIndustryLabelRelationDO.adapter(
						e,
						chainFrom.getBizId(), chainFrom.getChainName(),
						nodeNameMap.get(e.getChainNodeId()), labelNameMap.get(e.getIndustryLabelId())
				)).collect(toList());
		logicDeleteByChain(chainId);
		//开始插入
		dwdIndustryChainMapper.insertOnDuplicate(Collections.singletonList(chainDO));
		dwdIndustryChainNodeMapper.insertOnDuplicate(chainNodeDOS);
		dwdIndustryLabelMapper.insertOnDuplicate(labelDOS);
		dwdIndustryChainIndustryLabelRelationMapper.insertOnDuplicate(relationDOS);
	}

	private void logicDeleteByChain(Long chainId) {
		//删除节点
		List<Integer> nodeIds = dwdIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(DwdIndustryChainNodeDO.class)
				.eq(DwdIndustryChainNodeDO::getIndustryChainId, chainId)).stream().map(DwdIndustryChainNodeDO::getId).collect(toList());
		if (!nodeIds.isEmpty()) {
			dwdIndustryChainIndustryLabelRelationMapper.delete(Wrappers.lambdaQuery(DwdIndustryChainIndustryLabelRelationDO.class)
					.in(DwdIndustryChainIndustryLabelRelationDO::getIndustryChainNodeId, nodeIds));
		}
		dwdIndustryChainNodeMapper.delete(Wrappers.lambdaQuery(DwdIndustryChainNodeDO.class)
				.eq(DwdIndustryChainNodeDO::getIndustryChainId, chainId));
		//删除标签
		List<Integer> labelIds = dwdIndustryLabelMapper.selectList(Wrappers.lambdaQuery(DwdIndustryLabelDO.class)
				.eq(DwdIndustryLabelDO::getIndustryChainId, chainId)).stream().map(DwdIndustryLabelDO::getId).collect(toList());
		if (!labelIds.isEmpty()) {
			dwdIndustryChainIndustryLabelRelationMapper.delete(Wrappers.lambdaQuery(DwdIndustryChainIndustryLabelRelationDO.class)
					.in(DwdIndustryChainIndustryLabelRelationDO::getIndustryLabelId, labelIds)
					.eq(DwdIndustryChainIndustryLabelRelationDO::getIndustryChainId, chainId));
		}
		dwdIndustryLabelMapper.delete(Wrappers.lambdaQuery(DwdIndustryLabelDO.class)
				.eq(DwdIndustryLabelDO::getIndustryChainId, chainId));
		//删除产业链
		dwdIndustryChainMapper.delete(Wrappers.lambdaQuery(DwdIndustryChainDO.class)
				.eq(DwdIndustryChainDO::getId, chainId));
	}

	public static void main(String[] args) {
		String result = "aaa\u200Baaa\u200B".replaceAll("\\p{C}", "");
		System.out.println(result);
	}
}
