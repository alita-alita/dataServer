package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/1/3
 * @Description: 企业股东信息
 * @version: 1.0
 */
@Data
@TableName("enterprise_shareholder")
public class EnterpriseShareholderDO extends DataSyncBaseDO {

	/**
	 * 企业id
	 */
	@TableField("enterprise_id")
	private Long enterpriseId;

	/**
	 * 股东姓名
	 */
	@TableField("shareholder")
	private String shareholder;

	/**
	 * 持股比例
	 */
	@TableField("shareholding_ratio")
	private String shareholdingRatio;

	/**
	 * 认缴出资额
	 */
	@TableField("subscribed_capital_contribution")
	private String subscribedCapitalContribution;

	/**
	 * 认缴出资日期
	 */
	@TableField("subscribed_date")
	private String subscribedDate;

	/**
	 * 实缴出资额
	 */
	@TableField("paid_in_capital_contribution")
	private String paidInCapitalContribution;

	/**
	 * 实缴出资日期
	 */
	@TableField("paid_in_date")
	private String paidInDate;

	/**
	 * 最终受益股份
	 */
	@TableField("ultimate_beneficiary_shares")
	private String ultimateBeneficiaryShares;
}
