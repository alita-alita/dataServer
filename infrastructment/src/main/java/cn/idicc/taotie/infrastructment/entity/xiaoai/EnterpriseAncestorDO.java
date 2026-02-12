package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业籍贯表
 * </p>
 *
 * @author MengDa
 * @since 2024-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_ancestor")
public class EnterpriseAncestorDO extends DataSyncBaseDO {

	/**
	 * 社会统一信用代码
	 */
	private String uniCode;

	/**
	 * 籍贯Code
	 */
	private String ancestorCode;

	/**
	 * 关联人类型 人才 0 族群企业 1 无关联人 2
	 */
	private Integer relatedType;

	/**
	 * 关联人
	 */
	private String relatedMember;

	/**
	 * 关联人职务
	 */
	private String relatedMemberOccupation;

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
