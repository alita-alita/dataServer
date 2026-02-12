package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 海外公司基础信息表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_enterprise")
public class OverseasEnterpriseDO extends DataSyncBaseDO {

	/**
	 * 主键md5
	 */
	private String enterpriseMd5;

	/**
	 * 登记状态（注销、吊销）
	 */
	private String registerStatus;

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 英文名
	 */
	private String englishName;

	/**
	 * 企业名称
	 */
	private String enterpriseNameOri;

	/**
	 * 统一社会信用代码
	 */
	private String uniCode;

	/**
	 * 注册国家
	 */
	private String countryOfRegistration;

	/**
	 * 华人企业
	 */
	private Boolean isChineseEnterprise;

	/**
	 * 纳税人识别号
	 */
	private String taxpayerIdentificationNumber;

	/**
	 * 注册号
	 */
	private String registrationNumber;

	/**
	 * 组织机构代码
	 */
	private String organizeCode;

	/**
	 * 注册资本
	 */
	private String registeredCapital;

	/**
	 * 实缴资本
	 */
	private String paidUpCapital;

	/**
	 * 参保人数
	 */
	private String insuredPersonNumber;

	/**
	 * 参保人数所属年报
	 */
	private String annualReport;

	/**
	 * 经营范围
	 */
	private String businessScope;

	/**
	 * 法人
	 */
	private String legalPerson;

	/**
	 * 成立日期
	 */
	private LocalDate registerDate;

	/**
	 * 核准日期
	 */
	private LocalDate approveDate;

	/**
	 * 经营期限
	 */
	private String businessTerm;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区县
	 */
	private String area;

	/**
	 * 经济开发区
	 */
	private String developmentZone;

	/**
	 * 区域id
	 */
	private String regionMd5;

	/**
	 * 地址经纬度
	 */
	private String lngLat;

	/**
	 * 企业地址
	 */
	private String enterpriseAddress;

	/**
	 * 企业类型
	 */
	private String enterpriseType;

	/**
	 * 电话
	 */
	private String mobile;

	/**
	 * 更多电话
	 */
	private String moreMobile;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 更多邮箱
	 */
	private String moreEmail;

	/**
	 * 网址
	 */
	private String website;

	/**
	 * 行业编码
	 */
	private String industryCode;

	/**
	 * 行业
	 */
	private String industry;

	/**
	 * 目的
	 */
	private String purpose;

	/**
	 * 公司市值
	 */
	private String companyValue;

	/**
	 * 曾用名
	 */
	private String formerName;

	/**
	 * 企业规模
	 */
	private String scale;

	/**
	 * 企业简介
	 */
	private String introduction;


}
