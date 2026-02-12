package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_admin_penalty")
public class EnterpriseAdminPenaltyDO extends DataSyncBaseDO {

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业社会统一信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 决定文书号
	 */
	private String docNumber;

	/**
	 * 违法事实
	 */
	private String violationFacts;

	/**
	 * 处罚结果
	 */
	private String penaltyResult;

	/**
	 * 处罚金额（元）
	 */
	private String penaltyAmount;

	/**
	 * 处罚单位
	 */
	private String penaltyAuthority;

	/**
	 * 处罚日期
	 */
	private Date penaltyDate;

	/**
	 * 数据来源
	 */
	private String dataSource;


}
