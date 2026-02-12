package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 海外产业园区基本信息表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_industry_park")
public class OverseasIndustryParkDO extends DataSyncBaseDO {

	/**
	 * 主键md5
	 */
	private String parkMd5;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 产业园区名称
	 */
	private String industryParkName;

	/**
	 * 产业园区英文名称
	 */
	private String industryParkNameEn;

	/**
	 * 园区原始名称
	 */
	private String industryParkNameOri;

	/**
	 * 成立年份
	 */
	private String establishYear;

	/**
	 * 工业园区主任
	 */
	private String director;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 传真
	 */
	private String fax;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 官网
	 */
	private String website;

	/**
	 * 园区类型（集聚区、产业园区）
	 */
	private Integer parkType;

	/**
	 * 产业园区详细地址
	 */
	private String address;

	/**
	 * 园区办公室地址
	 */
	private String officeAddress;

	/**
	 * 产业园区占地面积，单位:亩
	 */
	private String landArea;

	/**
	 * 闲置空间
	 */
	private String freeSpace;

	/**
	 * 租售价格
	 */
	private String priceForSaleRent;

	/**
	 * 工厂租赁价格(下限)
	 */
	private BigDecimal factoryRentPriceMin;

	/**
	 * 工厂租赁价格(上限)
	 */
	private BigDecimal factoryRentPriceMax;

	/**
	 * 租赁价格单位（同一个国家；单位需要统一
	 */
	private String rentPriceUnit;

	/**
	 * 租赁押金
	 */
	private String rentDeposit;

	/**
	 * 租赁期限
	 */
	private String rentTerm;

	/**
	 * 土地销售价格（下限）
	 */
	private BigDecimal landSalePriceMin;

	/**
	 * 土地销售价格（上限）
	 */
	private BigDecimal landSalePriceMax;

	/**
	 * 销售价格单位（同一个国家；单位需要统一）
	 */
	private String salePriceUnit;

	/**
	 * 运营机构
	 */
	private String operatingEnterpriseName;

	/**
	 * 运营机构id
	 */
	private String operatingEnterpriseId;

	/**
	 * 产业园区注册企业数量
	 */
	private Integer enterpriseCount;

	/**
	 * 产业园区华人企业
	 */
	private Integer chineseEnterpriseCount;

}
