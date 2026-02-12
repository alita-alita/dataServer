package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 海外产业链典型企业龙头企业表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_enterprise_industry_chain_key_enterprise")
public class OverseasEnterpriseIndustryChainKeyEnterpriseDO extends DataSyncBaseDO {


	/**
	 * 国家
	 */
	private String country;

	/**
	 * 产业链id
	 */
	private Long    industryChainId;
	/**
	 * 产业链节点id
	 */
	private Long    industryChainNodeId;
	/**
	 * 企业id
	 */
	private String  keyEnterpriseMd5;
	/**
	 * 典型类型（龙头企业 1、重点企业 2）
	 */
	private Integer keyType;


}
