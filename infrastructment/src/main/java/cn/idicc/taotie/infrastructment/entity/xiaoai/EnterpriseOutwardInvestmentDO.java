package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/1/3
 * @Description: 企业对外投资企业信息
 * @version: 1.0
 */
@Data
@TableName("enterprise_outward_investment")
public class EnterpriseOutwardInvestmentDO extends DataSyncBaseDO {

	/**
	 * 企业id
	 */
	@TableField("enterprise_id")
	private Long enterpriseId;

	/**
	 * 被投资企业名称
	 */
	@TableField("name_of_enterprise")
	private String nameOfEnterprise;

	/**
	 * 投资比例
	 */
	@TableField("investment_proportion")
	private String investmentProportion;

	/**
	 * 认缴金额
	 */
	@TableField("subscribed_capital_contribution")
	private String subscribedCapitalContribution;

	/**
	 * 成立日期
	 */
	@TableField("register_date")
	private String registerDate;

	/**
	 * 经营状态
	 */
	@TableField("management_forms")
	private String managementForms;
}
