package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外园区产业链关系表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_industry_park_industry_chain_relation")
public class OverseasIndustryParkIndustryChainRelationDO extends DataSyncBaseDO {

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 产业链id
	 */
	private Long industryChainId;

	/**
	 * 产业链节点id
	 */
	private Long industryChainNodeId;

	/**
	 * 产业园区id
	 */
	private String industryParkMd5;

	/**
	 * 该关联产业链是否是产业园区的主导产业链，1代表是，0代表否
	 */
	private Integer isLeadingIndustryChain;

	/**
	 * 该关联节点是否是产业园区的主导节点，1代表是，0代表否
	 */
	private Integer isLeadingIndustryChainNode;
}
