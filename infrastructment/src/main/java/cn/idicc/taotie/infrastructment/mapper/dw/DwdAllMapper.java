package cn.idicc.taotie.infrastructment.mapper.dw;


import cn.idicc.taotie.infrastructment.dto.DwdEnterpriseDTO;
import cn.idicc.taotie.infrastructment.dto.ProductMatchesDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author junwang
 * @description 针对表【dwd】的数据库操作Mapper
 * @createDate 2025-01-03 09:49:59
 * @Entity cn.idicc.industryagent.entity.DwdAdminRegionDO
 */
public interface DwdAllMapper {


	@Insert({
			"<script>",
			"INSERT INTO dwd_product (id, product_name, product_description,product_purpose,enterprise_id, enterprise_name, enterprise_uni_code, data_source, gmt_modify, deleted) VALUES ",
			"<foreach collection='list' item='item' separator=','>",
			"(#{item.productId}, #{item.productName}, #{item.productDescription},#{item.productPurpose},#{item.enterpriseId}, #{item.enterpriseName}, #{item.enterpriseUniCode}, #{item.dataSource}, #{date}, 0)",
			"</foreach>",
			"ON DUPLICATE KEY UPDATE product_description=VALUES(product_description),product_purpose=VALUES(product_purpose),enterprise_name=VALUES(enterprise_name), gmt_modify=VALUES(gmt_modify), deleted=VALUES(deleted)",
			"</script>"
	})
	void insertOrUpdateProductBatch(@Param("list") List<ProductMatchesDTO> productList, @Param("date") String date);


	@Insert({
			"<script>",
			"INSERT INTO dwd_product_industry_label_relation (id, industry_label_id, industry_label_name, product_id, product_name, data_source, gmt_modify, deleted) VALUES ",
			"<foreach collection='list' item='item' separator=','>",
			"(#{item.productLabelRelation}, #{item.industryLabelId}, #{item.industryLabelName}, #{item.productId}, #{item.productName}, #{item.dataSource}, #{date}, 0)",
			"</foreach>",
			"ON DUPLICATE KEY UPDATE industry_label_name=VALUES(industry_label_name), product_name=VALUES(product_name), gmt_modify=VALUES(gmt_modify), deleted=VALUES(deleted)",
			"</script>"
	})
	void insertOrUpdateProductLabelRelationBatch(@Param("list") List<ProductMatchesDTO> productList, @Param("date") String date);


	@Update({
			"<script>",
			"UPDATE dwd_product_industry_label_relation",
			"SET deleted = 1",
			"WHERE gmt_modify  &lt; #{date}",
			"AND industry_label_id IN (select id from dwd_industry_label where industry_chain_id = #{chainId})",
			"</script>"
	})
	void deleteProductLabelRelationBatch(@Param("chainId") Long chainId, @Param("date") String date);

	@Insert({
			"<script>",
			"INSERT INTO dwd_enterprise_industry_label_relation (id, industry_label_id, industry_label_name, enterprise_id, enterprise_name, uni_code, data_source, gmt_modify, deleted) VALUES ",
			"<foreach collection='list' item='item' separator=','>",
			"(#{item.enterpriseLabelRelationId}, #{item.industryLabelId}, #{item.industryLabelName}, #{item.enterpriseId}, #{item.enterpriseName}, #{item.enterpriseUniCode}, #{item.dataSource}, #{date}, 0)",
			"</foreach>",
			"ON DUPLICATE KEY UPDATE industry_label_name=VALUES(industry_label_name), enterprise_name=VALUES(enterprise_name), gmt_modify=VALUES(gmt_modify), deleted=VALUES(deleted)",
			"</script>"
	})
	void insertOrUpdateEnterpriseLabelRelationBatch(@Param("list") List<ProductMatchesDTO> productList, @Param("date") String date);

	@Update({
			"<script>",
			"UPDATE dwd_enterprise_industry_label_relation",
			"SET deleted = 1",
			"WHERE gmt_modify &lt; #{date}",
			"AND industry_label_id IN (select id from dwd_industry_label where industry_chain_id = #{chainId})",
			"</script>"
	})
	void deleteEnterpriseLabelRelationBatch(@Param("chainId") Long chainId, @Param("date") String date);


	@Insert({
			"<script>",
			"INSERT INTO dwd_enterprise_industry_chain_relation (id, industry_chain_id, industry_chain_name, industry_chain_node_id, industry_chain_node_name, enterprise_id, enterprise_name, uni_code, data_source, gmt_modify, deleted) VALUES ",
			"<foreach collection='list' item='item' separator=','>",
			"(#{item.enterpriseChainRelationId}, #{item.industryChainId}, #{chainName}, #{item.industryChainNodeId}, #{item.industryChainNodeName}, #{item.enterpriseId}, #{item.enterpriseName}, #{item.enterpriseUniCode}, #{item.dataSource}, #{date}, 0)",
			"</foreach>",
			"ON DUPLICATE KEY UPDATE industry_chain_name=VALUES(industry_chain_name),industry_chain_node_name=VALUES(industry_chain_node_name),enterprise_name=VALUES(enterprise_name),gmt_modify=VALUES(gmt_modify), deleted=VALUES(deleted)",
			"</script>"
	})
	void insertOrUpdateEnterpriseChainRelationBatch(@Param("list") List<ProductMatchesDTO> productList, @Param("date") String date, @Param("chainName") String chainName);

	@Update({
			"<script>",
			"UPDATE dwd_enterprise_industry_chain_relation",
			"SET deleted = 1",
			"WHERE gmt_modify &lt; #{date}",
			"AND industry_chain_id = #{chainId}",
			"</script>"
	})
	void deleteEnterpriseChainRelationBatch(@Param("chainId") Long chainId, @Param("date") String date);


	@Insert({
			"<script>",
			"INSERT INTO dwd_enterprise_manual_industry_chain_key_enterprise (id, industry_chain_name, industry_chain_id, industry_chain_node_id, industry_chain_node_name, industry_labels, key_enterprise_name, key_enterprise_id, key_enterprise_uni_code, data_source,gmt_modify, deleted) VALUES ",
			"<foreach collection='list' item='item' separator=','>",
			"(#{item.enterpriseChainRelationId}, #{chainName}, #{item.industryChainId}, #{item.industryChainNodeId}, #{item.industryChainNodeName},#{item.industryLabelName}, #{item.enterpriseName}, #{item.enterpriseId}, #{item.enterpriseUniCode}, #{item.dataSource}, #{date}, 0)",
			"</foreach>",
			"ON DUPLICATE KEY UPDATE industry_chain_name=VALUES(industry_chain_name),industry_chain_node_name=VALUES(industry_chain_node_name),industry_labels=VALUES(industry_labels),key_enterprise_name=VALUES(key_enterprise_name),gmt_modify=VALUES(gmt_modify), deleted=VALUES(deleted)",
			"</script>"
	})
	void insertOrUpdateTypicalEnterpriseBatch(@Param("list") List<ProductMatchesDTO> productList, @Param("date") String date, @Param("chainName") String chainName);

	@Update({
			"<script>",
			"UPDATE dwd_enterprise_manual_industry_chain_key_enterprise",
			"SET deleted = 1",
			"WHERE gmt_modify &lt; #{date}",
			"AND industry_chain_id = #{chainId}",
			"</script>"
	})
	void deleteTypicalEnterpriseBatch(@Param("chainId") Long chainId, @Param("date") String date);

	@Select({
			"<script>",
			"select id, uni_code, enterprise_name from dwd_enterprise " +
			"WHERE enterprise_name IN ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
			"#{item}",
			"</foreach>",
			"</script>"
	})
	List<DwdEnterpriseDTO> queryByNames(@Param("list") List<String> enterpriseNameList);
}




