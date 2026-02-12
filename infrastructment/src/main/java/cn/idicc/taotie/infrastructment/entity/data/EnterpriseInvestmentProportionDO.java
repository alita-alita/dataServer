package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 企业研发投入
 * </p>
 *
 * @author wd
 * @since 2023-06-01
 */
@Data
@TableName("enterprise_investment_proportion")
public class EnterpriseInvestmentProportionDO extends BaseDO{

    private static final long serialVersionUID = 4072048351174550696L;

    /**
     * 行政区划code
     */
    @TableField("code")
    private String code;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 地区
     */
    @TableField("area")
    private String area;

    /**
     * 统一社会信用代码
     */
    @TableField("unified_social_credit_code")
    private String unifiedSocialCreditCode;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 研发投入金额，单位：元
     */
    @TableField("amount")
    private BigDecimal amount;
}
