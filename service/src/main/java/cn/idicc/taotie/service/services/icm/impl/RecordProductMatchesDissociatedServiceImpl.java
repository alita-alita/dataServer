package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.*;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.*;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesDissociatedQueryRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesDissociatedUpdateRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductDissociatedAdoptDTO;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesDissociatedDTO;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.infrastructment.utils.MD5Util;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesDissociatedService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * @Author: MengDa
 * @Date: 2025/5/7
 * @Description:
 * @version: 1.0
 */
@Service
@Slf4j
public class RecordProductMatchesDissociatedServiceImpl implements RecordProductMatchesDissociatedService {

	@Autowired
	private RecordProductMatchesDissociatedMapper recordProductMatchesDissociatedMapper;

	@Autowired
	private RecordAgentProductMatchesMapper recordAgentProductMatchesMapper;

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	@Autowired
	private RecordIndustryLabelMapper recordIndustryLabelMapper;

	@Autowired
	private RecordIndustryChainNodeMapper recordIndustryChainNodeMapper;

	@Resource
	private RecordAppProductMapper recordAppProductMapper;

	@Autowired
	private RecordIndustryChainMapper recordIndustryChainMapper;

	@Autowired
	private ChainNodeRefAtomNodeMapper chainNodeRefAtomNodeMapper;

	// 定义批次大小
	private static final int BATCH_SIZE = 5000;

	@Override
	public void refreshAiMatchesResult(Long chainId) {
		RecordIndustryChainDO chainDO = recordIndustryChainMapper.selectByBizId(chainId);
		List<String> labels = recordIndustryLabelMapper.getLabelByChainId(chainDO.getBizId())
				.stream().map(RecordIndustryLabelDO::getLabelName)
				.collect(Collectors.toList());
		List<RecordIndustryChainNodeDO> nodeDOS = recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getChainId, chainDO.getBizId())
		);
		Map<String, RecordIndustryChainNodeDO> nodeDOMap = nodeDOS.stream().collect(Collectors.toMap(RecordIndustryChainNodeDO::getNodeName, e -> e));
		List<RecordAgentProductMatchesDO> matchesDOS = recordAgentProductMatchesMapper.selectList(Wrappers.lambdaQuery(RecordAgentProductMatchesDO.class)
				.eq(RecordAgentProductMatchesDO::getIndustryChainId, chainDO.getBizId())
				.eq(RecordAgentProductMatchesDO::getStatus, 2)
				.notIn(RecordAgentProductMatchesDO::getMatchedProduct, labels)
		);
		matchesDOS = matchesDOS.stream().filter(e -> nodeDOMap.containsKey(e.getMatchedProduct())).collect(Collectors.toList());
		List<RecordProductMatchesDissociatedDO> dissociatedDOS = matchesDOS.stream().map(e -> {
			RecordAppProductDO productDO = recordAppProductMapper.selectByBizId(e.getProductId());
			if (productDO != null) {
				RecordProductMatchesDissociatedDO dissociatedDO = new RecordProductMatchesDissociatedDO();
				dissociatedDO.setChainId(Long.valueOf(e.getIndustryChainId()));
				dissociatedDO.setEnterpriseId(productDO.getEnterpriseId());
				dissociatedDO.setEnterpriseName(productDO.getEnterpriseName());
				dissociatedDO.setEnterpriseUniCode(productDO.getEnterpriseUniCode());
				dissociatedDO.setProductId(productDO.getBizId());
				dissociatedDO.setProductName(productDO.getProductName());
				dissociatedDO.setProductUrl(productDO.getProductUrl());
				dissociatedDO.setProductDescription(productDO.getProductDescription());
				dissociatedDO.setProductPurpose(productDO.getProductPurpose());
				RecordIndustryChainNodeDO nodeDO = nodeDOMap.get(e.getMatchedProduct());
				dissociatedDO.setNodeId(nodeDO.getBizId());
				dissociatedDO.setNodeName(nodeDO.getNodeName());
				dissociatedDO.setMatchedScore(e.getMatchedProductScore());
				dissociatedDO.setMatchReason(e.getExtraReason());
				dissociatedDO.setDataSource("AI自动生成");
				return dissociatedDO;
			} else {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
		if (!dissociatedDOS.isEmpty()) {
			recordProductMatchesDissociatedMapper.insertBatch(dissociatedDOS);
		}
	}

	@Override
	public IPage<RecordProductMatchesDissociatedDTO> pageList(RecordProductMatchesDissociatedQueryRequest request) {
		IPage<RecordProductMatchesDissociatedDTO> res = recordProductMatchesDissociatedMapper.pageList(
				Page.of(request.getPageNum(), request.getPageSize()),
				request.getChainId(),
				request.getEnterpriseName(),
				request.getProductName(),
				request.getStatus(),
				request.getLabelId(),
				request.getNodeId(),
				request.getMinMatchedScore()
		);
		return res;
	}

	@Override
	public void batchIgnore(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return;
		}
		recordProductMatchesDissociatedMapper.update(null, Wrappers.lambdaUpdate(RecordProductMatchesDissociatedDO.class)
				.in(RecordProductMatchesDissociatedDO::getId, ids)
				.set(RecordProductMatchesDissociatedDO::getStatus, 2)
		);
	}

	@Override
	public void batchNotIgnore(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return;
		}
		recordProductMatchesDissociatedMapper.update(null, Wrappers.lambdaUpdate(RecordProductMatchesDissociatedDO.class)
				.in(RecordProductMatchesDissociatedDO::getId, ids)
				.set(RecordProductMatchesDissociatedDO::getStatus, 0)
		);
	}

	@Override
	public void manualMount(RecordProductMatchesDissociatedUpdateRequest request) {
		if (request == null
				|| request.getId() == null
				|| request.getLabelId() == null
				|| request.getNodeId() == null
		) {
			return;
		}
		Long count = chainNodeRefAtomNodeMapper.countByNodeIdsAndLabelIds(Sets.newHashSet(request.getLabelId()),
				Sets.newHashSet(request.getNodeId()));
		if (count == 0) {
			throw new BizException("不存在的关联关系");
		}
		RecordIndustryLabelDO labelDO = recordIndustryLabelMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
				.eq(RecordIndustryLabelDO::getBizId, request.getLabelId())
				.last("limit 1")
		);
		RecordIndustryChainNodeDO nodeDO = recordIndustryChainNodeMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getBizId, request.getNodeId())
				.last("limit 1")
		);
		if (labelDO == null || nodeDO == null) {
			throw new BizException("不存在的节点或标签");
		}
		recordProductMatchesDissociatedMapper.update(null, Wrappers.lambdaUpdate(RecordProductMatchesDissociatedDO.class)
				.eq(RecordProductMatchesDissociatedDO::getId, request.getId())
				.set(RecordProductMatchesDissociatedDO::getStatus, 1)
				.set(RecordProductMatchesDissociatedDO::getLabelId, labelDO.getBizId())
				.set(RecordProductMatchesDissociatedDO::getLabelName, labelDO.getLabelName())
				.set(RecordProductMatchesDissociatedDO::getNodeId, nodeDO.getBizId())
				.set(RecordProductMatchesDissociatedDO::getNodeName, nodeDO.getNodeName())
		);
	}

	@Override
	public void updateProductInfo(RecordProductMatchesDissociatedUpdateRequest request) {
		if (request == null || request.getId() == null) {
			return;
		}
		RecordProductMatchesDissociatedDO dissociatedDO = recordProductMatchesDissociatedMapper.selectById(request.getId());
		if (dissociatedDO == null) {
			return;
		}
		String productId = MD5Util.getMd5Id(dissociatedDO.getProductName() + dissociatedDO.getEnterpriseId());
		recordProductMatchesDissociatedMapper.update(null, Wrappers.lambdaUpdate(RecordProductMatchesDissociatedDO.class)
				.eq(RecordProductMatchesDissociatedDO::getId, request.getId())
				.set(RecordProductMatchesDissociatedDO::getProductId, productId)
				.set(request.getProductName() != null && !request.getProductName().isEmpty(), RecordProductMatchesDissociatedDO::getProductName, request.getProductName())
				.set(request.getProductDescription() != null && !request.getProductDescription().isEmpty(), RecordProductMatchesDissociatedDO::getProductDescription, request.getProductDescription())
				.set(request.getProductUrl() != null && !request.getProductUrl().isEmpty(), RecordProductMatchesDissociatedDO::getProductUrl, request.getProductUrl())
				.set(request.getProductPurpose() != null && !request.getProductPurpose().isEmpty(), RecordProductMatchesDissociatedDO::getProductPurpose, request.getProductPurpose())
		);
	}

	@Override
	public void importData(MultipartFile file, RecordProductMatchesDissociatedQueryRequest request) {

		List<RecordProductDissociatedAdoptDTO> adoptDTOList = null;

		try {
			adoptDTOList = ExcelUtils.readMultipartFile(file, RecordProductDissociatedAdoptDTO.class);
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
		if (adoptDTOList.isEmpty()) {
			throw new BizException("execl解析无数据");
		}

		RecordIndustryChainDO chainDO = recordIndustryChainMapper.selectByBizId(request.getChainId());
		List<RecordIndustryChainNodeDO> industryChainNodeDOS = recordIndustryChainNodeMapper.selectList(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getChainId, chainDO.getBizId()));
		List<RecordIndustryLabelDO>                  industryLabelDOS                  = recordIndustryLabelMapper.getLabelByChainId(chainDO.getBizId());
		List<RecordIndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOS = new ArrayList<>();
		if (!industryChainNodeDOS.isEmpty() && !industryLabelDOS.isEmpty()) {
			industryChainNodeLabelRelationDOS = chainNodeRefAtomNodeMapper.selectNodeRefLabelByNodeIdsAndLabelIds(
					industryLabelDOS.stream().map(RecordIndustryLabelDO::getBizId).collect(Collectors.toSet())
					, industryChainNodeDOS.stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet())
			);
		}
		//label -> node
		Map<Long, Set<Long>> labelRelationMap = industryChainNodeLabelRelationDOS.stream()
				.collect(Collectors.groupingBy(RecordIndustryChainNodeLabelRelationDO::getIndustryLabelId, Collectors.mapping(RecordIndustryChainNodeLabelRelationDO::getChainNodeId, toSet())));
		Map<Long, RecordIndustryChainNodeDO> nodeDOMap  = industryChainNodeDOS.stream().collect(Collectors.toMap(RecordIndustryChainNodeDO::getBizId, e -> e));
		Map<Long, RecordIndustryLabelDO>     labelDOMap = industryLabelDOS.stream().collect(Collectors.toMap(RecordIndustryLabelDO::getBizId, e -> e));
		// 数据校验
		StringBuilder errorMsgSb = new StringBuilder();
		for (int index = 0; index < adoptDTOList.size(); index++) {
			StringBuilder                    msgSb         = new StringBuilder();
			RecordProductDissociatedAdoptDTO excelAdoptDTO = adoptDTOList.get(index);
			// 校验逻辑
			if (StringUtils.isEmpty(excelAdoptDTO.getEnterpriseName())) {
				msgSb.append("企业名称 为空,");
			}

			if (StringUtils.isEmpty(excelAdoptDTO.getEnterpriseUniCode()) || excelAdoptDTO.getEnterpriseUniCode().equals("-")) {
				msgSb.append("企业社会统一信用代码 为空,");
			}
			if (StringUtils.isEmpty(excelAdoptDTO.getProductName())) {
				msgSb.append("产品名称 为空,");
			}
			if (excelAdoptDTO.getNodeId() == null) {
				msgSb.append("节点ID 为空,");
			}
			if (excelAdoptDTO.getLabelId() == null) {
				msgSb.append("标签ID 为空,");
			}
			if (excelAdoptDTO.getNodeId() != null
					&& excelAdoptDTO.getLabelId() != null
					&& labelRelationMap.containsKey(excelAdoptDTO.getLabelId())
					&& !labelRelationMap.get(excelAdoptDTO.getLabelId()).contains(excelAdoptDTO.getNodeId())) {
				msgSb.append("标签与节点的关联关系不存在,");
			}
			if (msgSb.length() != 0) {
				msgSb.insert(0, "第" + (index + 1) + "行数据错误：");
				log.error("游离企业: 数据校验异常: {}", msgSb.toString());
				errorMsgSb.append(msgSb).append("。\n");
			}
		}
		if (errorMsgSb.length() != 0) {
			throw new BizException(errorMsgSb.toString());
		}
		ArrayList<RecordProductMatchesDissociatedDO> dissociatedDOS = new ArrayList<>();
		adoptDTOList.forEach(e -> {
			RecordProductMatchesDissociatedDO dissociatedDO = new RecordProductMatchesDissociatedDO();
			dissociatedDO.setChainId(chainDO.getBizId());
			dissociatedDO.setEnterpriseId(MD5Util.getMd5Id(e.getEnterpriseUniCode()));
			dissociatedDO.setEnterpriseName(e.getEnterpriseName());
			dissociatedDO.setEnterpriseUniCode(e.getEnterpriseUniCode());
			dissociatedDO.setProductId(MD5Util.getMd5Id(e.getProductName() + dissociatedDO.getEnterpriseId()));
			dissociatedDO.setProductName(e.getProductName());
			dissociatedDO.setProductUrl(e.getProductUrl());
			dissociatedDO.setProductDescription(e.getProductDescription());
			dissociatedDO.setProductPurpose(e.getProductPurpose());
			dissociatedDO.setNodeId(e.getNodeId());
			dissociatedDO.setLabelId(e.getLabelId());
			//补充数据
			if (labelDOMap.containsKey(e.getLabelId()) && nodeDOMap.containsKey(e.getNodeId())) {
				dissociatedDO.setNodeName(labelDOMap.get(e.getLabelId()).getLabelName());
				dissociatedDO.setLabelName(nodeDOMap.get(e.getNodeId()).getNodeName());
				dissociatedDO.setMatchedScore(new BigDecimal("1.0"));
				dissociatedDO.setStatus(1);
				dissociatedDO.setDataSource("人工导入");
				dissociatedDOS.add(dissociatedDO);
			}
		});
		if (!dissociatedDOS.isEmpty()) {
			log.info("游离企业: 文件处理成功，开始保存数据");
			Integer total = batchSave(dissociatedDOS);
			log.info("游离企业: 存入成功:{}行数据", total);
		} else {
			log.error("游离企业: 导入数据异常为空");
		}
	}

	public Integer batchSave(List<RecordProductMatchesDissociatedDO> dissociatedDOS) {
		int successCount = 0;
		if (dissociatedDOS == null || dissociatedDOS.isEmpty()) {
			log.error("游离企业: Input list is empty or null, no records to insert.");
			throw new BizException("游离企业导入异常，有效数据为空");
		}

		List<List<RecordProductMatchesDissociatedDO>> lists = Lists.partition(dissociatedDOS, BATCH_SIZE);
		Integer                                       total = 0;
		for (int i = 0; i < lists.size(); i++) {
			try {
				List<RecordProductMatchesDissociatedDO> tmpList = lists.get(i);
				if (tmpList != null && !tmpList.isEmpty()) {
					total += recordProductMatchesDissociatedMapper.insertBatch(tmpList);
				}
			} catch (Exception e) {
				log.error("游离企业：{}/{} 导入异常:", i + 1, lists.size(), e);
			} finally {
				log.info("游离企业: 导入 {}/{}", i + 1, lists.size());
			}
		}
		return total;
	}

}
