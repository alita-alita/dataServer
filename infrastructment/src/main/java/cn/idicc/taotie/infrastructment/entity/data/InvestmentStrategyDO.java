package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: WangZi
 * @Date: 2023/5/31
 * @Description:
 * @version: 1.0
 */
@Data
@TableName("investment_strategy")
public class InvestmentStrategyDO extends BaseDO {

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 机构id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 招商推荐度
     */
    @TableField("recommended_level")
    private Integer recommendedLevel;

    /**
     * 纳税能力
     */
    @TableField("tax_ability")
    private String taxAbility;

    /**
     * 纳税能力评分
     */
    @TableField("tax_ability_score")
    private BigDecimal taxAbilityScore;

    /**
     * 创新能力
     */
    @TableField("innovation_ability")
    private String innovationAbility;

    /**
     * 创新能力评分
     */
    @TableField("innovation_ability_score")
    private BigDecimal innovationAbilityScore;

    /**
     * 企业资质
     */
    @TableField("aptitude")
    private String aptitude;

    /**
     * 企业资质评分
     */
    @TableField("aptitude_score")
    private BigDecimal aptitudeScore;

    /**
     * 就业贡献
     */
    @TableField("employment_contribution")
    private String employmentContribution;

    /**
     * 就业贡献评分
     */
    @TableField("employment_contribution_score")
    private BigDecimal employmentContributionScore;

    /**
     * 业务增长
     */
    @TableField("business_growth")
    private String businessGrowth;

    /**
     * 业务增长评分
     */
    @TableField("business_growth_score")
    private BigDecimal businessGrowthScore;

    /**
     * 资金实力
     */
    @TableField("financial_strength")
    private String financialStrength;

    /**
     * 投资偏好
     */
    @TableField("investment_preference")
    private String investmentPreference;

    /**
     * 投资活跃度
     */
    @TableField("investment_activeness")
    private String investmentActiveness;

    /**
     * 投资动机
     */
    @TableField("investment_motive")
    private String investmentMotive;
}
