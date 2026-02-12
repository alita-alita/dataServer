package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 企业合作伙伴表
 * </p>
 *
 * @author MengDa
 * @since 2024-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_partner_relationship")
public class EnterprisePartnerRelationshipDO extends DataSyncBaseDO {

	/**
	 * 本方社会统一信用代码
	 */
	private String selfUniCode;

	/**
	 * 本方企业地区
	 */
	private String selfRegionCode;

	/**
	 * 合作企业统一社会信用代码
	 */
	private String counterpartUniCode;

	/**
	 * 合作企业地区
	 */
	private String counterpartRegionCode;

	/**
	 * 合作关系日期
	 */
	private LocalDate relationDate;

	/**
	 * 合作关系(客户 0、供应商 1、战略合作伙伴 2）
	 */
	private Integer relationshipType;

	/**
	 * 合作程度（非常紧密、紧密、一般）
	 */
	private String relationshipDegree;
}
