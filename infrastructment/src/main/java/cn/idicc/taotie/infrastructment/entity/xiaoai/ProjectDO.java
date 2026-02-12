package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author MengDa
 * @since 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project")
public class ProjectDO extends DataSyncBaseDO {

	/**
	 * md5
	 */
	private String projectMd5;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 项目编号
	 */
	private String projectCode;

	/**
	 * 项目标签（高价值专利培育项目 1 早小创业类公司项目 2）
	 */
	private Integer projectLabel;

	/**
	 * 项目描述
	 */
	private String projectDescription;

	/**
	 * 项目内容
	 */
	private String projectContent;

	/**
	 * 开工日期
	 */
	private Date startDate;

	/**
	 * 路演时间/评定时间
	 */
	private Date roadshowDate;

	/**
	 * 项目金额（人民币万元）
	 */
	private BigDecimal projectAmount;

	/**
	 * 企业统一社会信用代码
	 */
	private String enterpriseUniCode;

	/**
	 * 企业地区Code
	 */
	private String enterpriseCode;

	/**
	 * 项目关联专利
	 */
	private String relatedPatent;

	/**
	 * 价值描述
	 */
	private String projectValue;

	/**
	 * 专利持有人
	 */
	private String patentHolder;

	/**
	 * 项目荣誉/专利荣誉
	 */
	private String projectHonor;

	/**
	 * 行业领域
	 */
	private String industryField;

	/**
	 * 舆情文章链接
	 */
	private String newsUrl;

	/**
	 * 舆情id
	 */
	private String newsId;
}
