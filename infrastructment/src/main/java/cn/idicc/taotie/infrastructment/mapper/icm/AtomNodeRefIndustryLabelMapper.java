package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.AtomNodeRefIndustryLabelDO;
import cn.idicc.taotie.infrastructment.response.icm.AtomLabelChainRefBO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AtomNodeRefIndustryLabelMapper extends BaseMapper<AtomNodeRefIndustryLabelDO> {

	int deleteByRefId(@Param("id") Long refId);

	AtomNodeRefIndustryLabelDO selectOne(@Param("atomNodeId") Long atomNodeId, @Param("industryLabelId") Long industryLabelId);

	int deleteByAtomId(@Param("atomNodeId") Long atomNodeId);

	/**
	 * 根据原子节点id查询标签信息
	 *
	 * @param atomNodeId
	 * @return
	 */
	@Select("select " +
			"    t2.`label_name` as labelName, " +
			"    t2.`biz_id`  as labelId " +
			"from atom_node_ref_industry_label t1 " +
			"inner join `record_industry_label` t2 " +
			"on t1.industry_label_id = t2.biz_id and t2.`deleted` = 0 " +
			"where t1.atom_node_id = #{atomNodeId} ")
	List<Map<String, Object>> selectLabelByAtomId(@Param("atomNodeId") long atomNodeId);

	/**
	 * 根据原子节点id查询‘原子节点-标签-产业链’信息
	 *
	 * @param atomNodeIds
	 * @return
	 */
	@Select("<script>" +
			"SELECT " +
			"t2.id as atomNodeId, " +
			"t2.atom_node_name as atomNodeName, " +
			"t5.biz_id as chainId, " +
			"t5.chain_name as chainName," +
			"t6.biz_id as labelId, " +
			"t6.label_name as labelName " +
			"FROM " +
			"atom_node_ref_industry_label t1 " +
			"INNER JOIN industry_chain_atom_node t2 ON t1.atom_node_id = t2.id " +
			"INNER JOIN record_industry_label t6 on t1.industry_label_id = t6.biz_id " +
			"LEFT JOIN chain_node_ref_atom_node t3 ON t2.id = t3.atom_node_id AND t3.deleted = 0 " +
			"LEFT JOIN record_industry_chain_node t4 ON t3.node_id = t4.biz_id AND t4.deleted = 0 " +
			"LEFT JOIN record_industry_chain t5 on t4.chain_id = t5.biz_id AND t5.deleted = 0 " +
			"WHERE " +
			"t1.deleted = 0 " +
			"AND t2.deleted = 0 " +
			"AND t6.deleted = 0 " +
			"AND t1.atom_node_id IN " +
			"<foreach item='item' index='index' collection='atomNodeIds' open='(' separator=',' close=')'> " +
			"#{item} " +
			"</foreach> " +
			"</script>")
	List<AtomLabelChainRefBO> queryByAtomNodeIds(@Param("atomNodeIds") List<Long> atomNodeIds);

	/**
	 * 根据标签id查询‘原子节点-标签-产业链’信息
	 *
	 * @param labelIds
	 * @return
	 */
	@Select("<script>" +
			"SELECT " +
			"t2.id as atomNodeId, " +
			"t2.atom_node_name as atomNodeName, " +
			"t5.biz_id as chainId, " +
			"t5.chain_name as chainName," +
			"t6.biz_id as labelId, " +
			"t6.label_name as labelName " +
			"FROM " +
			"atom_node_ref_industry_label t1 " +
			"INNER JOIN industry_chain_atom_node t2 ON t1.atom_node_id = t2.id " +
			"INNER JOIN record_industry_label t6 on t1.industry_label_id = t6.biz_id " +
			"LEFT JOIN chain_node_ref_atom_node t3 ON t2.id = t3.atom_node_id AND t3.deleted = 0 " +
			"LEFT JOIN record_industry_chain_node t4 ON t3.node_id = t4.biz_id AND t4.deleted = 0 " +
			"LEFT JOIN record_industry_chain t5 on t4.chain_id = t5.biz_id AND t5.deleted = 0 " +
			"WHERE " +
			"t1.deleted = 0 " +
			"AND t2.deleted = 0 " +
			"AND t6.deleted = 0 " +
			"AND t1.industry_label_id IN " +
			"<foreach item='item' index='index' collection='labelIds' open='(' separator=',' close=')'> " +
			"#{item} " +
			"</foreach> " +
			"</script>")
	List<AtomLabelChainRefBO> queryByLabelIds(@Param("labelIds") List<Long> labelIds);
}
