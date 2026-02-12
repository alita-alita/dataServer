package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.AtomNodeRefIndustryLabelDO;
import cn.idicc.taotie.infrastructment.response.icm.AtomLabelChainRefBO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AtomNodeRefIndustryLabelService extends IService<AtomNodeRefIndustryLabelDO> {

	int deleteByRefId(Long refId);

	int deleteByAtomId(Long atomNodeId);

	AtomNodeRefIndustryLabelDO selectOne(Long atomNodeId, Long industryLabelId);

	void doRefLabel(Long atomNodeId, List<Long> industryLabelId);

	void doAtomNodeAddOrCancelIndustryLabelRefActions(Long atomNodeId);

	/**
	 * 根据标签id查询‘原子节点-标签-产业链’信息
	 *
	 * @param labelIds
	 * @return
	 */
	List<AtomLabelChainRefBO> queryByLabelIds(List<Long> labelIds);

	/**
	 * 根据原子节点id查询‘原子节点-标签-产业链’信息
	 *
	 * @param atomNodeIds
	 * @return
	 */
	List<AtomLabelChainRefBO> queryByAtomNodeIds(List<Long> atomNodeIds);
}
