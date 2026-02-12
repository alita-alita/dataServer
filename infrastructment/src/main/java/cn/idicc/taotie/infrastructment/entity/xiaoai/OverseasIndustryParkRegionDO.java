package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外产业园区省市区
 * </p>
 *
 * @author MengDa
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_industry_park_region")
public class OverseasIndustryParkRegionDO extends DataSyncBaseDO {

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 产业园区id
	 */
	private String industryParkMd5;

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
	private String regionMd5;

}
