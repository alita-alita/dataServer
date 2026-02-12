package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外地区或国家表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_area_country")
public class OverseasAreaCountryDO extends DataSyncBaseDO {

	/**
	 * 行政区划表md5
	 */
	private String countryMd5;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 国家
	 */
	private String countryEn;

	/**
	 * 国家代码
	 */
	private String countryCode;

}
