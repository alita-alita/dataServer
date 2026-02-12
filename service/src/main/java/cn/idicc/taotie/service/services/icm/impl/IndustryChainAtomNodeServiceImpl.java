package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.*;
import cn.idicc.taotie.infrastructment.mapper.icm.IndustryChainAtomNodeMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAgentProductMatchesMapper;
import cn.idicc.taotie.service.services.icm.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IndustryChainAtomNodeServiceImpl extends ServiceImpl<IndustryChainAtomNodeMapper, IndustryChainAtomNodeDO> implements IndustryChainAtomNodeService {

	private static final Logger logger = LoggerFactory.getLogger(IndustryChainAtomNodeServiceImpl.class);

	@Autowired
	private IndustryChainAtomNodeMapper industryChainAtomNodeMapper;

	@Autowired
	private AtomNodeRefIndustryLabelService atomNodeRefIndustryLabelService;

	@Autowired
	private RecordAgentProductMatchesMapper recordAgentProductMatchesMapper;

	@Autowired
	private RecordIndustryChainNodeService recordIndustryChainNodeService;

	@Autowired
	private ChainNodeRefAtomNodeService chainNodeRefAtomNodeService;

	@Autowired
	private RecordIndustryLabelService recordIndustryLabelService;

	@Override
	public IndustryChainAtomNodeDO selectOne(String atomNodeName) {
		return industryChainAtomNodeMapper.selectOne(atomNodeName);
	}

	@Override
	public int updateAndRefActions(Long atomNodeId, String atomNodeName, String nodeDesc) {

		IndustryChainAtomNodeDO existRecord = super.getById(atomNodeId);
		if (existRecord == null) {
			return 0;
		}

		boolean updateSuccess = super.update(Wrappers.lambdaUpdate(IndustryChainAtomNodeDO.class)
				.set(IndustryChainAtomNodeDO::getAtomNodeName, atomNodeName)
				.set(IndustryChainAtomNodeDO::getNodeDesc, nodeDesc)
				.eq(IndustryChainAtomNodeDO::getId, atomNodeId));

		if (updateSuccess && !nodeDesc.equals(existRecord.getNodeDesc())) {
			atomNodeRefIndustryLabelService.doAtomNodeAddOrCancelIndustryLabelRefActions(atomNodeId);
		}
		return 1;
	}

	@Override
	public int deleteById(Long id) {
		ChainNodeRefAtomNodeDO chainNodeRefAtomNodeDO = chainNodeRefAtomNodeService.getOne(
				Wrappers.lambdaQuery(ChainNodeRefAtomNodeDO.class)
						.eq(ChainNodeRefAtomNodeDO::getAtomNodeId, id)
						.last("limit 1"));

		if (chainNodeRefAtomNodeDO != null) {
			Long chainNodeId = chainNodeRefAtomNodeDO.getNodeId();
			logger.info("删除与原子节点{}关联的产业链节点关系:{}", chainNodeRefAtomNodeDO.getAtomNodeId(), chainNodeId);
			chainNodeRefAtomNodeService.deleteByAtomId(chainNodeRefAtomNodeDO.getAtomNodeId());
			logger.info("设置产业链节点:{}名称为【已删除】", chainNodeId);
			recordIndustryChainNodeService.update(null,
					Wrappers.lambdaUpdate(RecordIndustryChainNodeDO.class)
							.set(RecordIndustryChainNodeDO::getNodeDesc, null)
							.set(RecordIndustryChainNodeDO::getNodeName, "已删除")
							.eq(RecordIndustryChainNodeDO::getBizId, chainNodeId));
		}

		int effectRows = industryChainAtomNodeMapper.deleteById(id);
		if (effectRows > 0) {
			atomNodeRefIndustryLabelService.doAtomNodeAddOrCancelIndustryLabelRefActions(id);
		}
		return effectRows;
	}

	@Override
	public String highDangerActionConfirm(Long atomNodeId) {
		//1. 检查关联的产业链标签
		//2. 检查产业链标签关联的企业

		List<AtomNodeRefIndustryLabelDO> refLabels = atomNodeRefIndustryLabelService.list(
				Wrappers.lambdaQuery(AtomNodeRefIndustryLabelDO.class)
						.eq(AtomNodeRefIndustryLabelDO::getAtomNodeId, atomNodeId)
		);
		if (refLabels.isEmpty()) {
			return null;
		}

		Set<Long> refLabelIds = refLabels.stream().map(AtomNodeRefIndustryLabelDO::getIndustryLabelId).collect(Collectors.toSet());

		List<RecordIndustryLabelDO> labels = recordIndustryLabelService.list(Wrappers.lambdaQuery(RecordIndustryLabelDO.class)
				.in(RecordIndustryLabelDO::getBizId, refLabelIds));

		Set<String> labelNames = labels.stream().map(RecordIndustryLabelDO::getLabelName).collect(Collectors.toSet());

		List<RecordAgentProductMatchesDO> productMatchesDOList = recordAgentProductMatchesMapper.selectList(
				Wrappers.lambdaQuery(RecordAgentProductMatchesDO.class)
						.in(RecordAgentProductMatchesDO::getMatchedProduct, labelNames)
		);

		StringBuilder sb = new StringBuilder();
		sb.append("本次变更后，将有").append(refLabelIds.size()).append("个产业链标签进行重新挂载。");
		sb.append("本次变更后，将有").append(productMatchesDOList.size()).append("个产品进行重新挂载。");

		return sb.toString();
	}
}
