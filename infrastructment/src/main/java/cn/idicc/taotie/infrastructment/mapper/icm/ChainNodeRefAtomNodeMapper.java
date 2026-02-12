package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.ChainNodeRefAtomNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeLabelRelationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ChainNodeRefAtomNodeMapper extends BaseMapper<ChainNodeRefAtomNodeDO> {

	int deleteByAtomId(@Param("atomNodeId") Long atomNodeId);

	List<RecordIndustryChainNodeLabelRelationDO> selectNodeRefLabelByNodeIdsAndLabelIds(
			@Param("labelIds") Set<Long> labelIds,
																	 @Param("nodeIds") Set<Long> nodeIds);

	List<RecordIndustryChainNodeLabelRelationDO> getRelationByNodeIds(@Param("nodeIds") Set<Long> nodeIds);

	long countByNodeIdsAndLabelIds(@Param("labelIds") Set<Long> labelIds,
								   @Param("nodeIds") Set<Long> nodeIds);

}
