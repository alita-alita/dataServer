package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_competitive_product")
public class EnterpriseCompetitiveProductDO extends DataSyncBaseDO {

	/**
	 * 企业名称
	 */
	@TableField("enterprise_name")
	private String enterpriseName;

	/**
	 * 企业社会统一信用代码
	 */
	@TableField("enterprise_uni_code")
	private String enterpriseUniCode;

	/**
	 * 产品名
	 */
	@TableField("product_name")
	private String productName;

	/**
	 * 成立日期（字符串类型）
	 */
	@TableField("establishment_date")
	private String establishmentDate;

	/**
	 * 所属城市
	 */
	@TableField("city")
	private String city;

	/**
	 * 产品介绍
	 */
	@TableField("product_description")
	private String productDescription;

	/**
	 * 所属企业名称
	 */
	@TableField("product_enterprise_name")
	private String productEnterpriseName;

	/**
	 * 数据来源
	 */
	@TableField("data_source")
	private String dataSource;


}
