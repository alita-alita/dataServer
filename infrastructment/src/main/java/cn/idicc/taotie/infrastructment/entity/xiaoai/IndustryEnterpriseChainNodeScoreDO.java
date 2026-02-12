package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("industry_enterprise_chain_node_score")
public class IndustryEnterpriseChainNodeScoreDO extends DataSyncBaseDO {

	/**
	 * 产业链节点id
	 */
	private Long chainNodeId;

	/**
	 * 企业社会信用代码
	 */
	private String uniCode;

	/**
	 * 企业所属地区行政代码
	 */
	private String adminRegionCode;

	/**
	 * 企业在产业链节点上评分，评分越高在展示时越靠前
	 */
	private BigDecimal enterpriseChainNodeScore;


}
