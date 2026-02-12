package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 企业主要人员表
 *
 * @TableName enterprise_main_staff
 */
@Data
@TableName("enterprise_main_staff")
public class EnterpriseMainStaffDO extends DataSyncBaseDO {
	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业社会统一信用代码
	 */
	private String uniCode;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 学历
	 */
	private String educationLevel;

	/**
	 * 职务
	 */
	private String position;

	/**
	 * 薪酬
	 */
	private String salary;

	/**
	 * 持股数
	 */
	private String sharesHeldCount;

	/**
	 * 人才id
	 */
	private String talentId;

	/**
	 * 持股比例
	 */
	private String shareholdingRatio;

	/**
	 * 最终受益股份
	 */
	private String ultimateBeneficiaryShares;

	/**
	 * 本届任期
	 */
	private String currentTerm;

	/**
	 * 公告日期
	 */
	private Date announcementDate;

	/**
	 * 数据来源
	 */
	private String dataSource;

}