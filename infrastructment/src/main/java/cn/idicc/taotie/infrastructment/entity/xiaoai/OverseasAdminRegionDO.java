package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外行政区划表
 * </p>
 *
 * @author MengDa
 * @since 2025-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_admin_region")
public class OverseasAdminRegionDO extends DataSyncBaseDO {

	/**
	 * 主键md5
	 */
	private String regionMd5;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 省份（英语）
	 */
	private String provinceEn;

	/**
	 * 省份（原国家语言）
	 */
	private String provinceOri;

	/**
	 * 城市
	 */
	private String city;

	private String cityEn;

	private String cityOri;

	/**
	 * 区县
	 */
	private String area;

	private String areaEn;

	private String areaOri;

	/**
	 * 大区（华东区、华中区等）
	 */
	private String regionCluster;

	/**
	 * 父节点代码
	 */
	private String parentRegionMd5;

}
