/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.icm.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.dao.icm.RecordIndustryChainNodeDao;
import cn.idicc.taotie.infrastructment.entity.icm.ChainNodeRefAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.IndustryChainAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.enums.BooleanEnum;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainNodeMapper;
import cn.idicc.taotie.infrastructment.request.icm.SaveRecordIndustryChainNodeRequest;
import cn.idicc.taotie.infrastructment.request.icm.UpdateNodeScoreRequest;
import cn.idicc.taotie.infrastructment.response.icm.OrgRecordIndustryChainNodeDTO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainNodeDTO;
import cn.idicc.taotie.service.services.icm.ChainNodeRefAtomNodeService;
import cn.idicc.taotie.service.services.icm.IndustryChainAtomNodeService;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainNodeService;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author wd
 * @version $Id: ChainNodeServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 * @description 产业链节点ServiceImpl
 */
@Slf4j
@Service
@RefreshScope
public class RecordIndustryChainNodeServiceImpl extends ServiceImpl<RecordIndustryChainNodeMapper, RecordIndustryChainNodeDO> implements RecordIndustryChainNodeService {

	@Autowired
	private RecordIndustryChainNodeDao industryChainNodeDao;

	@Autowired
	private RecordIndustryChainNodeMapper industryChainNodeMapper;

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	@Autowired
	private IndustryChainAtomNodeService industryChainAtomNodeService;

	@Autowired
	private ChainNodeRefAtomNodeService chainNodeRefAtomNodeService;

	@Autowired
	private TransactionTemplate transactionTemplate;


	@Value("${showMaxNum:500}")
	private Integer showMaxNumber;

	/**
	 * 图谱中本地企业显示占比
	 */
	@Value("${local_enterprise_limit_percentage:40}")
	public Integer localEnterpriseLimitPercentage;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(SaveRecordIndustryChainNodeRequest request) {
		if (!recordIndustryChainService.allowExit(request.getChainId())) {
			throw new BizException("当前版本状态不允许编辑");
		}

		//0528 特殊字符
		//aaa​aaa​
		request.setNodeName(request.getNodeName().trim());
		request.setNodeName(request.getNodeName().replaceAll("\\p{C}", ""));
		// 第一步，数据验证
		validData(request);
		RecordIndustryChainNodeDO chainNodeDO = new RecordIndustryChainNodeDO(request.getChainId(),
				request.getNodeName(),
				request.getNodeOrder(),
				request.getNodeLevel(),
				request.getNodeParent(),
				request.getIsLeaf(),
				request.getShowMaxNumber(),
				request.getAbscissaValue(),
				request.getOrdinateValue(),
				request.getLineInfo(), request.getNodeDesc(), request.getThresholdScore());
		if (request.getChainNodeId() == null) {
			Long maxBizId = industryChainNodeMapper.getMaxBizId();
			request.setChainNodeId(maxBizId == null ? 1 : maxBizId + 1);
			chainNodeDO.setBizId(request.getChainNodeId());
		} else {
			RecordIndustryChainNodeDO nodeDO = industryChainNodeDao.getByNodeId(request.getChainNodeId());
			chainNodeDO.setId(nodeDO.getId());
		}
		// 使用事务模板
		transactionTemplate.execute(new TransactionCallback<Boolean>() {
			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				boolean commit = true;
				try {
					if (request.getIsLeaf() == 1) {
						IndustryChainAtomNodeDO refNode = industryChainAtomNodeService.getById(Long.parseLong(request.getAtomNodeValue()));
						if (refNode == null) {
							throw new BizException("选择的原子节点不存在");
						}
						chainNodeDO.setNodeName(refNode.getAtomNodeName());
						chainNodeDO.setNodeDesc(refNode.getNodeDesc());
					}
					// 第二步，保存产业链节点数据
					chainNodeDO.setBizId(request.getChainNodeId());
					industryChainNodeDao.saveOrUpdate(chainNodeDO);
					// 第三步，如果是叶子节点，则记录原子节点关系
					if (request.getIsLeaf() == 1) {
						List<ChainNodeRefAtomNodeDO> existRef = chainNodeRefAtomNodeService.list(Wrappers.lambdaQuery(ChainNodeRefAtomNodeDO.class)
								.eq(ChainNodeRefAtomNodeDO::getNodeId, chainNodeDO.getBizId()));
						//防止出现非正常数据，出现一个产业链节点关联多个原子节点，把非指定数据删除
						boolean found = false;
						for (ChainNodeRefAtomNodeDO ref : existRef) {
							if (ref.getAtomNodeId().equals(Long.parseLong(request.getAtomNodeValue()))) {
								found = true;
							} else {
								chainNodeRefAtomNodeService.removeById(ref.getId());
							}
						}
						//属于变更或新增
						if (!found) {
							ChainNodeRefAtomNodeDO newRef = new ChainNodeRefAtomNodeDO();
							newRef.setNodeId(request.getChainNodeId());
							newRef.setAtomNodeId(Long.parseLong(request.getAtomNodeValue()));
							chainNodeRefAtomNodeService.save(newRef);
						}
					} else {
						//挂载节点变更为目录节点，要删除关联关系
						chainNodeRefAtomNodeService.remove(Wrappers.lambdaQuery(ChainNodeRefAtomNodeDO.class)
								.eq(ChainNodeRefAtomNodeDO::getNodeId, request.getChainNodeId()));
					}
					recordIndustryChainService.update(null,
							Wrappers.lambdaUpdate(RecordIndustryChainDO.class)
									.set(RecordIndustryChainDO::getState, RecordChainStateEnum.NORMAL.getValue())
									.eq(RecordIndustryChainDO::getBizId, request.getChainId()));
				} catch (Exception e) {
					log.error("新增或更新产业链节点saveOrUpdate异常,{}", e.getMessage());
					commit = false;
					throw new BizException(e);
				} finally {
					if (!commit) {
						log.warn("新增或更新产业链节点saveOrUpdate异常,事物回滚，request={}", request);
						status.setRollbackOnly();
					}
				}
				return commit;
			}
		});
	}

	@Override
	public void updateThresholdScore(UpdateNodeScoreRequest request) {
		List<RecordIndustryChainNodeDO> allNodes = new ArrayList<>();
		queryChildNodes(request.getChainNodeId(), allNodes);
		RecordIndustryChainNodeDO ownSelf = industryChainNodeMapper.selectOne(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.eq(RecordIndustryChainNodeDO::getBizId, request.getChainNodeId())
		);
		if (ownSelf == null) {
			throw new BizException("节点不存在");
		}
		allNodes.add(ownSelf);
		for (RecordIndustryChainNodeDO nodeDO : allNodes) {
			RecordIndustryChainNodeDO chainNodeDO = new RecordIndustryChainNodeDO();
			chainNodeDO.setId(nodeDO.getId());
			chainNodeDO.setBizId(nodeDO.getBizId());
			chainNodeDO.setThresholdScore(BigDecimal.valueOf(request.getThresholdScore()));
			industryChainNodeMapper.updateById(chainNodeDO);
		}
	}


	/**
	 * 数据验证
	 *
	 * @param request
	 */
	private void validData(SaveRecordIndustryChainNodeRequest request) {
		if (BooleanEnum.YES.getCode().equals(request.getIsLeaf())) {
			// 如果是挂载企业，则必须填写最大显示个数、产业链标签
			if (request.getShowMaxNumber() == null) {
				throw new BizException("最大显示个数不能为空");
			} else if (request.getShowMaxNumber() > showMaxNumber) {
				throw new BizException("最大显示个数不能大于" + showMaxNumber);
			}
		}
		// 校验节点名称是否已经存在
		if (request.getChainNodeId() == null) {
			List<RecordIndustryChainNodeDO> chainNodeDOS = industryChainNodeDao.selectByChanIdAndChanNodeName(request.getChainId(), request.getNodeName());
			if (CollectionUtil.isNotEmpty(chainNodeDOS)) {
				throw new BizException("节点名称已存在");
			}
		} else {
			RecordIndustryChainNodeDO oldNodeDO = industryChainNodeDao.getByNodeId(request.getChainNodeId());
			if (!oldNodeDO.getNodeName().equals(request.getNodeName())) {
				List<RecordIndustryChainNodeDO> chainNodeDOS = industryChainNodeDao.selectByChanIdAndChanNodeName(request.getChainId(), request.getNodeName());
				if (CollectionUtil.isNotEmpty(chainNodeDOS)) {
					throw new BizException("节点名称已存在");
				}
			}
			// 变更节点类型
			if (!request.getIsLeaf().equals(oldNodeDO.getIsLeaf())) {
				if (BooleanEnum.YES.getCode().equals(request.getIsLeaf())) {
					// 目录节点变更为挂载节点
					List<RecordIndustryChainNodeDO> nodeDOS = industryChainNodeDao.queryByNodeParent(request.getChainNodeId());
					if (CollectionUtil.isNotEmpty(nodeDOS)) {
						throw new BizException("当前节点下有子节点，不可变更为挂载企业节点");
					}
					long count = industryChainNodeDao.count(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
							.eq(RecordIndustryChainNodeDO::getNodeParent, request.getNodeParent())
							.eq(RecordIndustryChainNodeDO::getIsLeaf, 0)
							.notIn(RecordIndustryChainNodeDO::getBizId, request.getChainNodeId()));
					if (count > 0) {
						throw new BizException("当前父节点下有目录节点，不可变更为挂载企业节点");
					}
				} else {
					// 挂载节点变更为目录节点，则要校验当前父节点下有是否有挂载节点
					long count = industryChainNodeDao.count(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
							.eq(RecordIndustryChainNodeDO::getNodeParent, request.getNodeParent())
							.eq(RecordIndustryChainNodeDO::getIsLeaf, 1)
							.notIn(RecordIndustryChainNodeDO::getBizId, request.getChainNodeId()));
					if (count > 0) {
						throw new BizException("当前父节点下有挂载节点，不可变更为目录节点");
					}
				}
			}
		}
	}


	@Override
	public OrgRecordIndustryChainNodeDTO queryChainNodeTreeByChainId(Long chainId) {
		List<RecordIndustryChainNodeDO> nodes = industryChainNodeDao.listByChainId(chainId);
		if (CollectionUtils.isNotEmpty(nodes)) {
			return queryChainNodeTreeByNodes(0L, nodes);
		}
		return null;
	}

	/**
	 * 节点树数据查询
	 *
	 * @param parentNodeId
	 * @param nodes
	 * @return
	 */
	public OrgRecordIndustryChainNodeDTO queryChainNodeTreeByNodes(Long parentNodeId,
																   List<RecordIndustryChainNodeDO> nodes) {
		List<OrgRecordIndustryChainNodeDTO> chainNodeDTOS = nodes.stream().map(OrgRecordIndustryChainNodeDTO::adapter).collect(toList());
		// 获取所有挂载企业节点id
		List<Long> leafNodeIds = chainNodeDTOS.stream()
				.filter(e -> BooleanEnum.YES.getCode().equals(e.getIsLeaf())).map(e -> e.getId())
				.collect(toList());
		//获取父节点
		OrgRecordIndustryChainNodeDTO chainNodeDO = chainNodeDTOS.stream().filter(m -> m.getNodeParent().equals(parentNodeId)).map(
				(m) -> {
					m.setChildNodes(getChildrenList(m, chainNodeDTOS));
					m.setLeafNodeCount(CollectionUtil.isNotEmpty(leafNodeIds) ? leafNodeIds.size() : 0);
					return m;
				}
		).collect(toList()).get(0);
		return chainNodeDO;
	}

	/**
	 * 设置子节点，并统计挂载企业节点的企业总数
	 *
	 * @param tree
	 * @param list
	 * @return
	 */
	private List<OrgRecordIndustryChainNodeDTO> getChildrenList(OrgRecordIndustryChainNodeDTO tree,
																List<OrgRecordIndustryChainNodeDTO> list) {
		return list.stream().filter(item -> Objects.equals(item.getNodeParent(), tree.getId())).map(
				(item) -> {
					item.setChildNodes(getChildrenList(item, list));
					return item;
				}
		).collect(toList());
	}

	/**
	 * 获取产业链节点详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public RecordIndustryChainNodeDTO detail(Long id) {
		// 查询产业链节点详情
		RecordIndustryChainNodeDO  chainNodeDO  = industryChainNodeDao.getByNodeId(id);
		RecordIndustryChainNodeDTO chainNodeDTO = RecordIndustryChainNodeDTO.adapt(chainNodeDO);
		if (chainNodeDO.getIsLeaf() == 1) {
			ChainNodeRefAtomNodeDO refAtomNode = chainNodeRefAtomNodeService.getOne(Wrappers.lambdaQuery(ChainNodeRefAtomNodeDO.class)
					.eq(ChainNodeRefAtomNodeDO::getNodeId, chainNodeDO.getBizId())
					.last("limit 1"));
			if (refAtomNode != null) {
				chainNodeDTO.setAtomNodeId(refAtomNode.getAtomNodeId());
			}
		}
		// 查询节点关联的产业链数据
//		List<RecordIndustryChainNodeLabelRelationDO> nodeLabelRelationDOS = industryChainNodeLabelRelationService.listByChainNodeIds(Lists.newArrayList(id));
//		if (CollectionUtils.isNotEmpty(nodeLabelRelationDOS)) {
//			List<Long> labelIds = nodeLabelRelationDOS.stream().map(RecordIndustryChainNodeLabelRelationDO::getIndustryLabelId).collect(toList());
		// 查询标签名称
//			chainNodeDTO.setRecordIndustryLabelDTOS(
//					recordIndustryLabelService.listByLabelIds(labelIds).stream().map(RecordIndustryLabelDTO::adapt).collect(toList()));
//		}
		return chainNodeDTO;
	}

	@Override
	public void delete(Long id) {
		// 根节点不能删除
		RecordIndustryChainNodeDO chainNodeDO = industryChainNodeDao.getByNodeId(id);
		if (chainNodeDO == null) {
			throw new BizException("节点数据不存在");
		}
		if (0L == chainNodeDO.getNodeParent()) {
			throw new BizException("根节点不能删除");
		}
		// 查询节点及其所有子节点数据
		List<RecordIndustryChainNodeDO> chainNodeDOS = Lists.newArrayList();
		chainNodeDOS.add(chainNodeDO);
		this.queryChildNodes(id, chainNodeDOS);
		List<Long> bizIds = chainNodeDOS.stream().map(RecordIndustryChainNodeDO::getBizId).collect(toList());
		transactionTemplate.execute(new TransactionCallback<Boolean>() {
			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				boolean commit = true;
				try {
					// 如果节点类型=挂载企业，则删除节点标签关系
//					List<Long> leafNodeIds = chainNodeDOS.stream().filter(e -> BooleanEnum.YES.getCode().equals(e.getIsLeaf())).map(RecordIndustryChainNodeDO::getBizId).collect(toList());
//					if (CollectionUtils.isNotEmpty(leafNodeIds)) {
//						// 查询节点标签关系
//						List<RecordIndustryChainNodeLabelRelationDO> chainNodeLabelRelationDOS = industryChainNodeLabelRelationService.listByChainNodeIds(leafNodeIds);
//						// 批量更新节点标签关系状态为删除
//						List<Long> chainLabelRelationIds = chainNodeLabelRelationDOS.stream().map(BaseDO::getId).collect(toList());
//						industryChainNodeLabelRelationService.physicsBatchDeleteByIds(chainLabelRelationIds);
//					}
					// 批量更新节点状态为删除
					industryChainNodeMapper.physicsBatchDeleteByBizIds(bizIds);
					chainNodeRefAtomNodeService.remove(Wrappers.lambdaQuery(ChainNodeRefAtomNodeDO.class)
							.in(ChainNodeRefAtomNodeDO::getNodeId, bizIds));
				} catch (Exception e) {
					log.error("删除产业链节点异常,{}", e);
					commit = false;
				} finally {
					if (!commit) {
						log.warn("删除产业链节点异常,事物回滚，节点id={}", id);
						status.setRollbackOnly();
					}
				}
				return commit;
			}
		});

	}

	/**
	 * 递归查询子节点数据
	 *
	 * @param nodeParentId
	 * @param chainNodeDOS
	 */
	@Override
	public void queryChildNodes(Long nodeParentId, List<RecordIndustryChainNodeDO> chainNodeDOS) {
		// 查询节点信息
		List<RecordIndustryChainNodeDO> nodeDOS = industryChainNodeDao.queryByNodeParent(nodeParentId);
		if (CollectionUtils.isNotEmpty(nodeDOS)) {
			chainNodeDOS.addAll(nodeDOS);
			nodeDOS.forEach(e -> queryChildNodes(e.getBizId(), chainNodeDOS));
		}
	}

	@Override
	public List<OrgRecordIndustryChainNodeDTO> queryChainNodeLeafListByChainId(Long chainId) {
		List<RecordIndustryChainNodeDO> nodes = industryChainNodeDao.listLeafByChainIdAndVersion(chainId);
		return nodes.stream().map(OrgRecordIndustryChainNodeDTO::adapter).collect(toList());
	}

	@Override
	public List<RecordIndustryChainNodeDO> list(Long chainId) {
		return industryChainNodeDao.list(chainId);
	}

}
