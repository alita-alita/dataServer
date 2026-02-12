package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("project_bidding")
public class ProjectBiddingDO extends DataSyncBaseDO {

	/**
	 * 项目名称
	 */
	@TableField("project_name")
	private String projectName;

	/**
	 * 项目编号
	 */
	@TableField("project_code")
	private String projectCode;

	/**
	 * 项目描述
	 */
	@TableField("project_description")
	private String projectDescription;

	/**
	 * 项目内容（longtext）
	 */
	@TableField("project_content")
	private String projectContent;

	/**
	 * 招标主体
	 */
	@TableField("purchaser_name")
	private String purchaserName;

	/**
	 * 发布日期（date）
	 */
	@TableField("publish_date")
	private Date publishDate;

	/**
	 * 发布日期文本
	 */
	@TableField("publish_date_str")
	private String publishDateStr;

	/**
	 * 中标金额（decimal(20,4)）
	 */
	@TableField("project_amount")
	private String projectAmount;

	/**
	 * 中标金额文本
	 */
	@TableField("project_amount_str")
	private String projectAmountStr;

	/**
	 * 中标企业名称
	 */
	@TableField("winner_enterprise_name")
	private String winnerEnterpriseName;

	/**
	 * 被持有企业名称（非空）
	 */
	@TableField("enterprise_name")
	private String enterpriseName;

	/**
	 * 被持有企业社会统一信用代码
	 */
	@TableField("uni_code")
	private String uniCode;

	/**
	 * 单位角色
	 */
	@TableField("unit_role")
	private String unitRole;

	/**
	 * 省份（非空，默认空字符串）
	 */
	@TableField("province")
	private String province;

	/**
	 * 城市
	 */
	@TableField("city")
	private String city;

	/**
	 * 区县
	 */
	@TableField("area")
	private String area;

	/**
	 * 舆情文章链接
	 */
	@TableField("news_url")
	private String newsUrl;

	/**
	 * 来源
	 */
	@TableField("data_source")
	private String dataSource;


}
