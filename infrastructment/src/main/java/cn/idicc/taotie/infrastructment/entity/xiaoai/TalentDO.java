package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人才表
 * </p>
 *
 * @author MengDa
 * @since 2024-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("talent")
public class TalentDO extends DataSyncBaseDO {

	/**
	 * 唯一MD5
	 */
	private String talentMd5;

	/**
	 * 人才姓名
	 */
	private String talentName;

	/**
	 * 人才性别
	 */
	private String talentGender;

	/**
	 * 人才籍贯
	 */
	private String talentAncestorHome;

	/**
	 * 人才籍贯地区行政代码
	 */
	private String talentAncestorHomeRegionCode;

	/**
	 * 人才出生地
	 */
	private String talentBirthPlace;

	/**
	 * 人才出生地行政区划代码
	 */
	private String talentBirthPlaceRegionCode;

	/**
	 * 人才简历
	 */
	private String talentResume;

	/**
	 * 人才数据来源（上市公司披露、人工提取、舆情分析、商会通讯录）
	 */
	private String talentDataSource;

	/**
	 * 人才年龄
	 */
	private String talentAge;

	/**
	 * 人才学历
	 */
	private String talentDegree;

	/**
	 * 人才电话号码
	 */
	private String talentTelephoneNumber;

	/**
	 * 研究方向/创新方向
	 */
	private String researchField;

	/**
	 * 产业
	 */
	private String chainNames;
}
