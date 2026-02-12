package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外产业园区企业关联表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_industry_park_enterprise_relation")
public class OverseasIndustryParkEnterpriseRelationDO extends DataSyncBaseDO {

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 产业园区id
	 */
	private String industryParkMd5;

	/**
	 * 企业id
	 */
	private String enterpriseMd5;

	/**
	 * 企业社会统一信用代码
	 */
	private String registrationNumber;
}
