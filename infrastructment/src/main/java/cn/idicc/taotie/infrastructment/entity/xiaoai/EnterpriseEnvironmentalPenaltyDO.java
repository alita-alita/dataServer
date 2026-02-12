package cn.idicc.taotie.infrastructment.entity.xiaoai;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_environmental_penalty")
public class EnterpriseEnvironmentalPenaltyDO extends DataSyncBaseDO {

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
	 * 没收金额(元)
	 */
	private String confiscationAmount;

	/**
	 * 处罚单位
	 */
	private String penaltyAuthority;

	/**
	 * 处罚日期
	 */
	private Date penaltyDate;

	/**
	 * 违法行为类型
	 */
	private String violationType;

	/**
	 * 发布日期
	 */
	private Date publicationDate;

	/**
	 * 处罚依据
	 */
	private String penaltyBasis;


	/**
	 * 数据来源
	 */
	private String dataSource;


}
