package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_investment_promotion_clue_sum")
public class EnterpriseInvestmentPromotionClueSumDO extends BaseDO {

	/**
	 * Recommended enterprise id
	 */
	private String recommendedEnterpriseId;

	/**
	 * Enterprise name
	 */
	private String recommendedEnterpriseName;

	/**
	 * Enterprise unified social credit code
	 */
	private String recommendedUniCode;

	/**
	 * Industry chain name
	 */
	private String industryChainName;

	/**
	 * Industry chain id
	 */
	private Long industryChainId;

	/**
	 * Recommendation date
	 */
	private Date recommendDate;

	/**
	 * Intended province
	 */
	private String recommendProvince;

	/**
	 * Intended city
	 */
	private String recommendCity;

	/**
	 * Intended area/county
	 */
	private String recommendArea;

	/**
	 * Region code (000000 for nationwide recommendation)
	 */
	private String recommendRegionCode;

	/**
	 * Product application scenario analysis
	 */
	private String productApplicationAnalysis;

	/**
	 * Eco-cluster analysis
	 */
	private String ecoClusterAnalysis;

	/**
	 * Product and local resource analysis
	 */
	private String productLocalResourceAnalysis;

	/**
	 * Touchpoint clue summary
	 */
	private String touchpointClueSummary;

	/**
	 * Negotiation strategy suggestions
	 */
	private String negotiationStrategySuggestions;

	/**
	 * Company touchpoint methods
	 */
	private String companyTouchpointMethods;

	/**
	 * Enterprise R&D strength analysis
	 */
	private String enterpriseRndStrengthAnalysis;

	/**
	 * Enterprise financial strength analysis
	 */
	private String enterpriseFinancialStrengthAnalysis;

	/**
	 * Enterprise strategic planning analysis
	 */
	private String enterpriseStrategyPlanningAnalysis;

	/**
	 * Enterprise government cooperation analysis
	 */
	private String enterpriseGovCooperationAnalysis;

	/**
	 * Enterprise contact information
	 */
	private String enterpriseContactInfo;

	/**
	 * Partner contact information
	 */
	private String partnerContactInfo;

	/**
	 * Association contact information
	 */
	private String associationContactInfo;

	/**
	 * Investor contact information
	 */
	private String investorContactInfo;


}
