package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 商会会员表
 * </p>
 *
 * @author MengDa
 * @since 2024-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inst_commerce_association_member")
public class InstCommerceAssociationMemberDO extends DataSyncBaseDO {

	/**
	 * 商会md5
	 */
	private String commerceMd5;

	/**
	 * 关联人类型人才θ族群企业 1无关联人2
	 */
	private Integer relatedType;

	/**
	 * 关联人统一社会信用代码
	 */
	private String relatedUniCode;

	/**
	 * 关联人名称(冗余字段)
	 */
	private String relatedName;

	/**
	 * 人才md5
	 */
	private String relatedTalentMd5;

	/**
	 * 人才企业职务(冗余字段)
	 */
	private String relatedTalentOccupation;

	/**
	 * 人才籍贯code(冗余字段)
	 */
	private String relatedTalentAncestorCode;


	/**
	 * 会员社会统一信用代码
	 */
	private String memberUniCode;

	/**
	 * 会员类型(企业θ、族群企业1)
	 */
	private Integer memberType;

	/**
	 * 入会时间
	 */
	private LocalDate joinDate;

	/**
	 * 退会时间
	 */
	private LocalDate leaveDate;

	/**
	 * 职务（会员、理事）
	 */
	private String position;

	/**
	 * 对应企业省份(冗余字段)
	 */
	private String memberProvince;

	/**
	 * 对应企业城市(冗余字段)
	 */
	private String memberCity;

	/**
	 * 对应企业区县(冗余字段)
	 */
	private String memberArea;
}
