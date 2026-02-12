package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 政策产业链关系表
 * </p>
 *
 * @author MengDa
 * @since 2025-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("policy_industry_chain_relation")
public class PolicyIndustryChainRelationDO extends DataSyncBaseDO {

	/**
	 * 产业链id
	 */
	private Long chainId;

	/**
	 * 产业链节点id
	 */
	private Long nodeId;

	/**
	 * 产业政策id
	 */
	private String policyMd5;
}
