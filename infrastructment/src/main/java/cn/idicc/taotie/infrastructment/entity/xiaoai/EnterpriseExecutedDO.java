package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_executed")
public class EnterpriseExecutedDO extends DataSyncBaseDO {

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业社会统一信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 案号
	 */
	private String caseNumber;

	/**
	 * 被执行人
	 */
	private String executedPerson;

	/**
	 * 执行标的(元)
	 */
	private String executionAmount;

	/**
	 * 执行法院
	 */
	private String executionCourt;

	/**
	 * 立案日期
	 */
	private Date filingDate;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 疑似申请执行人
	 */
	private String suspectedApplicant;

	/**
	 * 数据来源
	 */
	private String dataSource;


}
