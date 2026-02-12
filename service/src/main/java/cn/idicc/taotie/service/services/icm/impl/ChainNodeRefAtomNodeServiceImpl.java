package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.taotie.infrastructment.entity.icm.ChainNodeRefAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.mapper.icm.ChainNodeRefAtomNodeMapper;
import cn.idicc.taotie.service.services.icm.ChainNodeRefAtomNodeService;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainNodeService;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChainNodeRefAtomNodeServiceImpl extends ServiceImpl<ChainNodeRefAtomNodeMapper, ChainNodeRefAtomNodeDO> implements ChainNodeRefAtomNodeService {

	@Autowired
	private ChainNodeRefAtomNodeMapper chainNodeRefAtomNodeMapper;

	@Autowired
	private RecordIndustryChainNodeService recordIndustryChainNodeService;

	@Autowired
	private RecordIndustryChainService recordIndustryChainService;

	/***
	 * 找出原子节点被引用的所有产业链
	 */
	@Override
	public List<RecordIndustryChainDO> findRefChain(Long atomNodeId, Long chainNodeId) {

		//获取所有的关联节点
		List<ChainNodeRefAtomNodeDO> allRefNodeIds =
				chainNodeRefAtomNodeMapper.selectList(Wrappers.lambdaQuery(ChainNodeRefAtomNodeDO.class)
						.eq(ChainNodeRefAtomNodeDO::getAtomNodeId, atomNodeId)
						.ne(chainNodeId != null, ChainNodeRefAtomNodeDO::getNodeId, chainNodeId));

		if(allRefNodeIds.isEmpty()){
			return new ArrayList<>();
		}

		Set<Long> refNodeIdSet = allRefNodeIds.stream().map(ChainNodeRefAtomNodeDO::getNodeId).collect(Collectors.toSet());


		List<RecordIndustryChainNodeDO> nodes = recordIndustryChainNodeService.list(Wrappers.lambdaQuery(RecordIndustryChainNodeDO.class)
				.in(RecordIndustryChainNodeDO::getBizId, refNodeIdSet));

		Set<Long> refChainIdSet = nodes.stream().map(RecordIndustryChainNodeDO::getChainId).collect(Collectors.toSet());

		return recordIndustryChainService.list(Wrappers.lambdaQuery(RecordIndustryChainDO.class)
				.in(RecordIndustryChainDO::getBizId, refChainIdSet));
	}

	@Override
	public int deleteByAtomId(Long atomNodeId) {
		return chainNodeRefAtomNodeMapper.deleteByAtomId(atomNodeId);
	}
}
