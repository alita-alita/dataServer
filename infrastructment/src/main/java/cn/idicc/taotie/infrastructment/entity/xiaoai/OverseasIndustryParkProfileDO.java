package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 海外园区速查档案
 * </p>
 *
 * @author MengDa
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_industry_park_profile")
public class OverseasIndustryParkProfileDO extends DataSyncBaseDO {

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 产业园区id
	 */
	private String industryParkMd5;

	/**
	 * 主题
	 */
	private String topic;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 正文
	 */
	private String content;

	/**
	 * 版本日期
	 */
	private LocalDate versionDate;
}
