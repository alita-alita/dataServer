package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 海外产业园区产业链指标表
 * </p>
 *
 * @author MengDa
 * @since 2025-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("overseas_industry_park_industry_chain_index")
public class OverseasIndustryParkIndustryChainIndexDO extends DataSyncBaseDO {


	private String country;

	/**
	 * 产业链id
	 */
	private Integer industryChainId;

	/**
	 * 产业园区id
	 */
	private String industryParkMd5;

	/**
	 * 产业链公司数量
	 */
	private Integer industryChainEnterpriseCount;

	/**
	 * 产业链环节总数（和园区无关）
	 */
	private Integer industryChainNodeTotalCount;

	/**
	 * 园区覆盖产业链环节数
	 */
	private Integer industryParkNodeCount;

	/**
	 * 园区涉及产业链节点id（逗号隔开）
	 */
	private String industryParkNodeIds;

}
