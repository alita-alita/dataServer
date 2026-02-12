package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 行业协会
 *
 * @TableName inst_industry_association
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("inst_industry_association")
public class InstIndustryAssociationDO extends DataSyncBaseDO {

	/**
	 * 协会id
	 */
	private String associationMd5;

	/**
	 * 协会名称
	 */
	private String associationName;

	/**
	 * 协会统一社会信用代码
	 */
	private String uniCode;

	/**
	 * 协会对应的官网网址
	 */
	private String url;

	/**
	 * 成立日期
	 */
	private Date registerDate;

	/**
	 * 成立日期文本
	 */
	private String registerDateStr;

	/**
	 * 协会简介
	 */
	private String introduction;

	/**
	 * 协会产业领域id(多个逗号隔开
	 */
	private String investmentIndustryChainIds;

	/**
	 * 产业领域(多个逗号隔开
	 */
	private String investmentIndustryChainNames;

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
	 * 区域代码
	 */
	private String regionCode;

	/**
	 * 协会注册地址
	 */
	private String registerAddress;

	/**
	 * 联系电话
	 */
	private String mobile;

	/**
	 * 数据来源
	 */
	private String dataSource;
}