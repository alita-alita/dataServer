package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("inst_investment_organization")
public class InstInvestmentOrganizationDO extends BaseDO {

	/**
	 * 社会信用代码
	 */
	private String uniCode;

	/**
	 * 投资机构名称
	 */
	private String organizationName;

	/**
	 * 电话
	 */
	private String mobile;

	/**
	 * 主投轮次
	 */
	private String primaryRounds;

	/**
	 * 区域代码
	 */
	private String regionCode;

	/**
	 * 简介
	 */
	private String introduction;

	/**
	 * 地址
	 */
	private String address;

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


}
