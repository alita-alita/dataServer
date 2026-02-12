/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.AtomNodeRefIndustryLabelDO;
import cn.idicc.taotie.infrastructment.entity.icm.ChainNodeRefAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.ChainNodeRefAtomNodeMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryLabelMapper;
import cn.idicc.taotie.infrastructment.request.icm.RecordIndustryLabelAddRequest;
import cn.idicc.taotie.infrastructment.response.icm.AtomLabelChainRefBO;
import cn.idicc.taotie.infrastructment.response.icm.IndustryChainAtomNodeDTO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryLabelDTO;
import cn.idicc.taotie.service.result.CommonPageDTO;
import cn.idicc.taotie.service.services.icm.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * rdc标签表ServiceImpl
 *
 * @author wd
 * @version $Id: IndustryLabelServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 */
@Slf4j
@Service
public class RecordIndustryLabelServiceImpl extends ServiceImpl<RecordIndustryLabelMapper, RecordIndustryLabelDO> implements RecordIndustryLabelService {


	@Autowired
	private RecordIndustryLabelMapper industryLabelMapper;

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	@Autowired
	private RecordIndustryLabelMapper recordIndustryLabelMapper;

	@Autowired
	private RecordIndustryChainNodeService recordIndustryChainNodeService;

	@Autowired
	private ChainNodeRefAtomNodeService chainNodeRefAtomNodeService;

	@Autowired
	private AtomNodeRefIndustryLabelService atomNodeRefIndustryLabelService;
	@Autowired
	private ChainNodeRefAtomNodeMapper      chainNodeRefAtomNodeMapper;

	/**
	 * 添加产业链标签
	 *
	 * @param param
	 * @return
	 */
	@Override
	public void add(RecordIndustryLabelAddRequest param) {
		//TODO 状态验证
//		if (!recordIndustryChainService.allowExit(param.getChainId())) {
//			throw new BizException("当前版本状态不允许编辑");
//		}

		//TODO 0528 特殊字符
		//aaa​aaa​
		param.setLabelName(param.getLabelName().trim());
		param.setLabelName(param.getLabelName().replaceAll("\\p{C}", ""));
		RecordIndustryLabelDO existRecordDO = exits(param);
		if (existRecordDO != null && !existRecordDO.getDeleted()) {
			throw new BizException("已存在同名标签");
		}
		if (existRecordDO != null) {
			//存在被删除的标签，直接更新
			existRecordDO.setLabelDesc(param.getLabelDesc());
			existRecordDO.setStatus(RecordChainStateEnum.NORMAL.getValue());
			existRecordDO.setDeleted(Boolean.FALSE);
//			existRecordDO.setChainId(null);
			industryLabelMapper.updateById(param.getLabelName(),
					param.getLabelDesc(), 0, existRecordDO.getId());
		} else {
			addByDB(param);
		}
	}

	@Override
	public int deleteById(Long labelId) {

		int effectRows = industryLabelMapper.delete(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
				.eq(RecordIndustryLabelDO::getBizId, labelId));
		if (effectRows == 0) {
			return effectRows;
		}
		AtomNodeRefIndustryLabelDO existRef = atomNodeRefIndustryLabelService.getOne(Wrappers.lambdaQuery(AtomNodeRefIndustryLabelDO.class)
				.eq(AtomNodeRefIndustryLabelDO::getIndustryLabelId, labelId)
				.last("limit 1"));
		if (existRef != null) {
			atomNodeRefIndustryLabelService.doAtomNodeAddOrCancelIndustryLabelRefActions(existRef.getAtomNodeId());
		}
		return effectRows;
	}

	@Override
	public int updateById(Long labelId, String labelName, String labelDesc) {

		RecordIndustryLabelDO old = industryLabelMapper.selectOne(
				Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
						.eq(RecordIndustryLabelDO::getBizId, labelId)
						.last("limit 1")
		);

		int effectRows = industryLabelMapper.update(null,
				Wrappers.lambdaUpdate(RecordIndustryLabelDO.class)
						.set(RecordIndustryLabelDO::getLabelName, labelName)
						.set(RecordIndustryLabelDO::getLabelDesc, labelDesc)
						.eq(RecordIndustryLabelDO::getBizId, labelId));
		if (effectRows == 0) {
			return effectRows;
		}
		if (!old.getLabelDesc().equals(labelDesc)) {
			AtomNodeRefIndustryLabelDO existRef = atomNodeRefIndustryLabelService.getOne(Wrappers.lambdaQuery(AtomNodeRefIndustryLabelDO.class)
					.eq(AtomNodeRefIndustryLabelDO::getIndustryLabelId, labelId)
					.last("limit 1"));
			if (existRef != null) {
				atomNodeRefIndustryLabelService.doAtomNodeAddOrCancelIndustryLabelRefActions(existRef.getAtomNodeId());
			}
		} else {
			log.info("产业链标签定义未发生变更，无需重置关联数据的状态");
		}
		return effectRows;
	}

	/**
	 * 查询是否存在重复名称的有效标签
	 *
	 * @return
	 */
	public RecordIndustryLabelDO exits(RecordIndustryLabelAddRequest param) {
		return industryLabelMapper.selectOneByLabelName(param.getLabelName());
	}

	@Override
	public CommonPageDTO<RecordIndustryLabelDTO> pageList(String searchKeyword, Integer pageNum, Integer pageSize) {

		Page<RecordIndustryLabelDO> page = new Page<>(pageNum, pageSize);

		LambdaQueryWrapper<RecordIndustryLabelDO> queryWrapper =
				Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
						.like(StringUtils.isNotEmpty(searchKeyword), RecordIndustryLabelDO::getLabelName, searchKeyword)
						.orderByDesc(RecordIndustryLabelDO::getId);

		page = industryLabelMapper.selectPage(page, queryWrapper);
		// 查询标签关联的产业链和原子节点信息
		List<Long> labelIds = page.getRecords().stream().map(RecordIndustryLabelDO::getBizId).collect(Collectors.toList());
		Map<Long, List<AtomLabelChainRefBO>> labelMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(labelIds)) {
			labelMap = atomNodeRefIndustryLabelService.queryByLabelIds(labelIds).stream().collect(Collectors.groupingBy(AtomLabelChainRefBO::getLabelId));
		}
		Map<Long, List<AtomLabelChainRefBO>> finalLabelMap = labelMap;
		List<RecordIndustryLabelDTO> data = page.getRecords().stream()
				.map(RecordIndustryLabelDTO::adapt)
				.peek(item -> item.setAtomLabelChainRefList(finalLabelMap.get(item.getId())))
				.collect(Collectors.toList());
		return CommonPageDTO.of(data, page.getSize(), page.getCurrent(), page.getTotal());
	}

	/**
	 * 添加产业链标签到数据库
	 *
	 * @param param
	 * @return
	 */
	private RecordIndustryLabelDO addByDB(RecordIndustryLabelAddRequest param) {
		RecordIndustryLabelDO recordIndustryLabelDO = new RecordIndustryLabelDO();
		recordIndustryLabelDO.setLabelName(param.getLabelName());
//		recordIndustryLabelDO.setChainId(param.getChainId());
		recordIndustryLabelDO.setLabelDesc(param.getLabelDesc());
		Long maxBizId = recordIndustryLabelMapper.getMaxBizId();
		recordIndustryLabelDO.setBizId(maxBizId == null ? 1 : maxBizId + 1);
		industryLabelMapper.insert(recordIndustryLabelDO);
		return recordIndustryLabelDO;
	}

	@Override
	public List<RecordIndustryLabelDO> listAllLabelRefByChainId(Long chainId) {
		List<RecordIndustryChainNodeDO> chainNodeDOS = recordIndustryChainNodeService.list(
				Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
						.eq(RecordIndustryChainNodeDO::getChainId, chainId)
						.eq(RecordIndustryChainNodeDO::getIsLeaf, 1)
		);
		Set<Long> nodeIds = chainNodeDOS.stream().map(RecordIndustryChainNodeDO::getBizId).collect(Collectors.toSet());

		List<ChainNodeRefAtomNodeDO> refAtomNodes = chainNodeRefAtomNodeMapper.selectList(
				Wrappers.lambdaQuery(ChainNodeRefAtomNodeDO.class)
						.in(ChainNodeRefAtomNodeDO::getNodeId, nodeIds)
		);
		Set<Long> atomNodeIds = refAtomNodes.stream().map(ChainNodeRefAtomNodeDO::getAtomNodeId).collect(Collectors.toSet());

		List<AtomNodeRefIndustryLabelDO> refIndustryLabels = atomNodeRefIndustryLabelService
				.list(Wrappers.lambdaQuery(AtomNodeRefIndustryLabelDO.class)
						.in(AtomNodeRefIndustryLabelDO::getAtomNodeId, atomNodeIds));

		Set<Long> labelIds = refIndustryLabels.stream().map(AtomNodeRefIndustryLabelDO::getIndustryLabelId)
				.collect(Collectors.toSet());

		return super.list(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
				.in(RecordIndustryLabelDO::getBizId, labelIds));
	}
}
