package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业学校表
 * </p>
 *
 * @author MengDa
 * @since 2024-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_academia")
public class EnterpriseAcademiaDO extends DataSyncBaseDO {

	/**
	 * 社会统一信用代码
	 */
	private String uniCode;

	/**
	 * 学校MD5
	 */
	private String academicMd5;

	/**
	 * 学校地区
	 */
	private String academicCode;

	/**
	 * 关联人类型人才θ族群企业 1无关联人2
	 */
	private Integer relatedType;

	/**
	 * 关联人
	 */
	private String relatedTalent;

	/**
	 * 关联人社会代码
	 */
	private String relatedUniCode;

	/**
	 * 关联人MD5
	 */
	private String relatedTalentMd5;

	/**
	 * 关联人职务
	 */
	private String relatedTalentOccupation;

	/**
	 * 企业地区Code后期用
	 */
	private String enterpriseCode;

	/**
	 * 对应企业省份(冗余字段)
	 */
	private String enterpriseProvince;

	/**
	 * 对应企业城市(冗余字段)
	 */
	private String enterpriseCity;

	/**
	 * 对应企业区县(冗余字段)
	 */
	private String enterpriseArea;

	/**
	 * 产业链ID
	 */
	private String enterpriseChainIds;
}
