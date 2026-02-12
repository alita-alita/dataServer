package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 研究机构
 * </p>
 *
 * @author MengDa
 * @since 2024-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inst_research_institution")
public class InstResearchInstitutionDO extends DataSyncBaseDO {

	/**
	 * 归属机构统一社会信用代码
	 */
	private String uniCode;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 区
	 */
	private String area;

	/**
	 * 地区行政代码
	 */
	private String code;

	/**
	 * 介绍
	 */
	private String intro;

	/**
	 * 成立日期
	 */
	private Date registerDate;

	/**
	 * 机构类型（实验室、研究中心）
	 */
	private String institutionType;

	/**
	 * 研究领域
	 */
	private String researchField;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 名称md5
	 */
	private String nameMd5;
}
