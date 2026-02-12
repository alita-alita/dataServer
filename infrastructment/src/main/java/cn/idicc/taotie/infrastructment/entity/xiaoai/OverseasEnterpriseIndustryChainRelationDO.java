package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外企业产业链关系表
 * </p>
 *
 * @author MengDa
 * @since 2025-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_enterprise_industry_chain_relation")
public class OverseasEnterpriseIndustryChainRelationDO extends DataSyncBaseDO {

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
	 * 企业id
	 */
	private String enterpriseMd5;


}
