package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外产业链区域指标表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_industry_chain_region_index")
public class OverseasIndustryChainRegionIndexDO extends DataSyncBaseDO {

	/**
	 * chain id
	 */
	private Long industryChainId;

	/**
	 * chain node id
	 */
	private Long industryChainNodeId;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 地区
	 */
	private String area;

	/**
	 * 地区md5
	 */
	private String regionMd5;

	/**
	 * 企业数量统计
	 */
	private Long enterpriseCount;


}
