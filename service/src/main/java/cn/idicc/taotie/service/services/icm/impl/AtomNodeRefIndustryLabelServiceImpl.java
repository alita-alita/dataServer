package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.*;
import cn.idicc.taotie.infrastructment.enums.RecordChainStateEnum;
import cn.idicc.taotie.infrastructment.mapper.icm.AtomNodeRefIndustryLabelMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAgentProductMatchesMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAppProductMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordProductMatchesAiCheckMapper;
import cn.idicc.taotie.infrastructment.response.icm.AtomLabelChainRefBO;
import cn.idicc.taotie.service.services.icm.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AtomNodeRefIndustryLabelServiceImpl
		extends ServiceImpl<AtomNodeRefIndustryLabelMapper, AtomNodeRefIndustryLabelDO> implements AtomNodeRefIndustryLabelService {

	private static final Logger logger = LoggerFactory.getLogger(AtomNodeRefIndustryLabelServiceImpl.class);

	@Autowired
	private AtomNodeRefIndustryLabelMapper atomNodeRefIndustryLabelMapper;

	@Autowired
	private RecordAgentProductMatchesMapper recordAgentProductMatchesMapper;

	@Autowired
	private RecordAiCheckRecordService recordAiCheckRecordService;

	@Autowired
	private RecordIndustryLabelService recordIndustryLabelService;

	@Autowired
	private RecordAppEnterpriseIndustryChainSuspectedService recordAppEnterpriseIndustryChainSuspectedService;

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	@Autowired
	private RecordAppProductMapper recordAppProductMapper;

	@Autowired
	private ChainNodeRefAtomNodeService       chainNodeRefAtomNodeService;
	@Autowired
	private RecordProductMatchesAiCheckMapper recordProductMatchesAiCheckMapper;

	@Override
	public int deleteByRefId(Long refId) {
		return atomNodeRefIndustryLabelMapper.deleteByRefId(refId);
	}

	@Override
	public int deleteByAtomId(Long atomNodeId) {
		return atomNodeRefIndustryLabelMapper.deleteByAtomId(atomNodeId);
	}

	@Override
	public AtomNodeRefIndustryLabelDO selectOne(Long atomNodeId, Long industryLabelId) {
		return atomNodeRefIndustryLabelMapper.selectOne(atomNodeId, industryLabelId);
	}

	@Override
	public void doRefLabel(Long atomNodeId, List<Long> industryLabelId) {
		if (industryLabelId.isEmpty()) {
			//清理原子节点下所有的关系
			List<Map<String, Object>> existRows = atomNodeRefIndustryLabelMapper.selectLabelByAtomId(atomNodeId);
			if (!existRows.isEmpty()) {
				doAtomNodeAddOrCancelIndustryLabelRefActions(atomNodeId);
			}
			this.deleteByAtomId(atomNodeId);
			return;
		}

		List<AtomNodeRefIndustryLabelDO> exists = atomNodeRefIndustryLabelMapper
				.selectList(Wrappers.lambdaQuery(AtomNodeRefIndustryLabelDO.class)
						.eq(AtomNodeRefIndustryLabelDO::getAtomNodeId, atomNodeId));

		//对比request和exists找出新增的和删除的关系
		//提取已存在的标签ID集合
		Set<Long> existLabelIds = exists.stream()
				.map(AtomNodeRefIndustryLabelDO::getIndustryLabelId)
				.collect(Collectors.toSet());

		//提取请求中的标签ID集合
		Set<Long> requestLabelIds = new HashSet<>(industryLabelId);

		//找出需要新增的关系（在请求中存在但在数据库中不存在）
		Set<Long> toAddLabelIds = requestLabelIds.stream()
				.filter(labelId -> !existLabelIds.contains(labelId))
				.collect(Collectors.toSet());

		//找出需要删除的关系（在数据库中存在但请求中不存在）
		Set<Long> toDeleteIds = exists.stream()
				.filter(row -> !requestLabelIds.contains(row.getIndustryLabelId()))
				.map(AtomNodeRefIndustryLabelDO::getId)
				.collect(Collectors.toSet());
		for (Long labelId : toAddLabelIds) {
			AtomNodeRefIndustryLabelDO atomNodeRefIndustryLabelDO = new AtomNodeRefIndustryLabelDO();
			atomNodeRefIndustryLabelDO.setAtomNodeId(atomNodeId);
			atomNodeRefIndustryLabelDO.setIndustryLabelId(labelId);
			atomNodeRefIndustryLabelMapper.insert(atomNodeRefIndustryLabelDO);
		}
		for (Long labelId : toDeleteIds) {
			atomNodeRefIndustryLabelMapper.deleteByRefId(labelId);
		}
		if (!toDeleteIds.isEmpty() || !toAddLabelIds.isEmpty()) {
			doAtomNodeAddOrCancelIndustryLabelRefActions(atomNodeId);
		}

		logger.info("原子节点关系处理完成,新增:{}, 删除:{}", toAddLabelIds.size(), toDeleteIds.size());
	}

	/**
	 * 当原子节点发生变更时，则要对关联信息的状态进行更新
	 */
	public void doAtomNodeAddOrCancelIndustryLabelRefActions(Long atomNodeId) {

		List<RecordIndustryChainDO> refChains = chainNodeRefAtomNodeService.findRefChain(atomNodeId, null);
		if (refChains.isEmpty()) {
			logger.info("原子节点:{}, 没有关联产业链", atomNodeId);
			return;
		}

		Set<Long> refChainIds = refChains.stream().map(RecordIndustryChainDO::getBizId).collect(Collectors.toSet());

		// 1. 获取原子节点下所有关联的产业链标签
		List<Map<String, Object>> refLabels = atomNodeRefIndustryLabelMapper.selectLabelByAtomId(atomNodeId);

		for (Map<String, Object> map : refLabels) {
			String labelName = (String) map.get("labelName");
			Long   bizId     = (Long) map.get("labelId");
			// 2. 获取产业链标签关联的产品
			List<RecordAgentProductMatchesDO> productMatchesDOList = recordAgentProductMatchesMapper.selectList(Wrappers.lambdaQuery(RecordAgentProductMatchesDO.class)
					.in(RecordAgentProductMatchesDO::getIndustryChainId, refChainIds)
					.eq(RecordAgentProductMatchesDO::getMatchedProduct, labelName));
			// p.k: 产业链标签重置为初始状态，重新生产
			recordIndustryLabelService.update(Wrappers.lambdaUpdate(RecordIndustryLabelDO.class)
					.set(RecordIndustryLabelDO::getStatus, RecordChainStateEnum.NORMAL.getValue())
					.eq(RecordIndustryLabelDO::getBizId, bizId));

			// 3. 将产品及其企业的状态设置为初始状态
			//p.k: 将产品表的状态更新为初始状态，重新生产
			Set<String> productBizIds = productMatchesDOList.stream().map(RecordAgentProductMatchesDO::getProductId)
					.collect(Collectors.toSet());
			if (!productBizIds.isEmpty()) {
				recordAppProductMapper.update(null, Wrappers.lambdaUpdate(RecordAppProductDO.class)
						.set(RecordAppProductDO::getStatus, RecordChainStateEnum.NORMAL.getValue())
						.in(RecordAppProductDO::getBizId, productBizIds));

				List<RecordAppProductDO> appProductList = recordAppProductMapper.selectList(Wrappers.lambdaQuery(RecordAppProductDO.class)
						.in(RecordAppProductDO::getBizId, productBizIds));
				//p.k: 重置疑似企业,重新生产
				Set<String> refEnterpriseIds = appProductList.stream().map(RecordAppProductDO::getEnterpriseId).collect(Collectors.toSet());
				recordAppEnterpriseIndustryChainSuspectedService.setStatusInitByEnterpriseIds(refEnterpriseIds);
				//删除质检记录
				logger.info("删除质检记录,chainId:{}, productIds:{}", refChains, productBizIds);
				recordProductMatchesAiCheckMapper.deleteByChainIdAndProductId(refChainIds, productBizIds);
				logger.info("删除匹配结果记录,chain id:{}, labelName:{}", refChainIds, labelName);
				recordAgentProductMatchesMapper.deleteByChainIdAndMatchProduct(refChainIds, labelName);
			}

		}


		//重置产业链状态
		logger.info("重置产业链:{} 为初始状态", refChainIds);
		recordIndustryChainService.update(null, Wrappers.lambdaUpdate(RecordIndustryChainDO.class)
				.set(RecordIndustryChainDO::getState, RecordChainStateEnum.NORMAL.getValue())
				.in(RecordIndustryChainDO::getBizId, refChainIds));
	}

    @Override
    public List<AtomLabelChainRefBO> queryByLabelIds(List<Long> labelIds) {
        return atomNodeRefIndustryLabelMapper.queryByLabelIds(labelIds);
    }

    @Override
    public List<AtomLabelChainRefBO> queryByAtomNodeIds(List<Long> atomNodeIds) {
        return atomNodeRefIndustryLabelMapper.queryByAtomNodeIds(atomNodeIds);
    }

}
