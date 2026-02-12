package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 人才资质表
 * </p>
 *
 * @author MengDa
 * @since 2024-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("talent_qualification")
public class TalentQualificationDO extends DataSyncBaseDO {

	/**
	 * 人才唯一标识
	 */
	private String talentMd5;

	/**
	 * 资质类型 0 资质 1 奖项
	 */
	private Integer qualificationType;

	/**
	 * 资质名称
	 */
	private String qualificationName;

	/**
	 * 资质层级 0 国家级 1 省级 2 市级
	 */
	private Integer qualificationLevel;

	/**
	 * 认定机构
	 */
	private String certifyingAuthority;

	/**
	 * 资质发布日期
	 */
	private LocalDate publishDate;

	/**
	 * 失效日期
	 */
	private LocalDate endDate;

	/**
	 * 行业领域
	 */
	private String field;
}
