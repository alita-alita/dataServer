package cn.idicc.taotie.infrastructment.entity.dw;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 推荐企业池
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dws_enterprise_investment_promotion")
public class DwsEnterpriseInvestmentPromotionDO {


	/**
	 * 表主键id = MD5( recommended_enterprise_id + "#" + clue_type + "#" + industry_chain_id + "#" + recommend_region_code )
	 */
	private String id;

	/**
	 * 被推荐企业id
	 */
	private String recommendedEnterpriseId;

	/**
	 * 企业名称
	 */
	private String recommendedEnterpriseName;

	/**
	 * 企业统一社会信用代码
	 */
	private String recommendedUniCode;

	/**
	 * 产业链名称
	 */
	private String industryChainName;

	/**
	 * 关联产业链id
	 */
	private Integer industryChainId;

	/**
	 * 意向省份
	 */
	private String recommendProvince;

	/**
	 * 意向城市
	 */
	private String recommendCity;

	/**
	 * 意向区县
	 */
	private String recommendArea;

	/**
	 * 区域代码
	 */
	private String recommendRegionCode;

	/**
	 * 线索类型：0：链式招商，1：校友招商，2：乡贤招商，3：早小招商，4：人才招商，5：行业协会，6：投资机构
	 */
	private Byte clueType;

	/**
	 * 数据来源
	 */
	private String dataSource;

	/**
	 * 需要规避的地区
	 */
	private String excludeRegionCode;

	/**
	 * 删除的理由
	 */
	private String deleteReason;

	private Integer deleted;

}
