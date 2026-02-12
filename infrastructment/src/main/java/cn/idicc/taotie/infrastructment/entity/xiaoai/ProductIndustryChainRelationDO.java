package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_industry_chain_relation")
public class ProductIndustryChainRelationDO extends DataSyncBaseDO {

	/**
	 * md5
	 */
	private String productMd5;

	/**
	 * 产业链ID
	 */
	private Long industryChainId;

	/**
	 * 节点ID
	 */
	private Long industryChainNodeId;

}
