package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: WangZi
 * @Date: 2022/12/24
 * @Description: 企业
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("enterprise")
public class EnterpriseDO extends BaseDO {

    /**
     * 状态 0:禁用 1:启用
     */
    @TableField("status")
    private Boolean status;

    /**
     * 登记状态
     */
    @TableField("register_status")
    private String registerStatus;

    /**
     * 企业名称
     */
    @TableField("enterprise_name")
    private String enterpriseName;

    /**
     * 英文名
     */
    @TableField("english_name")
    private String englishName;

    /**
     * 统一社会信用代码
     */
    @TableField("unified_social_credit_code")
    private String unifiedSocialCreditCode;

    /**
     * 纳税人识别号
     */
    @TableField("taxpayer_identification_number")
    private String taxpayerIdentificationNumber;

    /**
     * 注册号
     */
    @TableField("registration_number")
    private String registrationNumber;

    /**
     * 组织机构代码
     */
    @TableField("organize_code")
    private String organizeCode;

    /**
     * 注册资本
     */
    @TableField("registered_capital")
    private String registeredCapital;

    /**
     * 实缴资本
     */
    @TableField("paid_up_capital")
    private String paidUpCapital;

    /**
     * 参保人数
     */
    @TableField("insured_persons_number")
    private String insuredPersonsNumber;

    /**
     * 经营范围
     */
    @TableField("business_scope")
    private String businessScope;

    /**
     * 股票代码
     */
    @TableField("stock_code")
    private String stockCode;

    /**
     * 上市类型
     */
    @TableField("type_of_listing")
    private String typeOfListing;

    /**
     * 法人
     */
    @TableField("legal_person")
    private String legalPerson;

    /**
     * 成立日期
     */
    @TableField("register_date")
    private String registerDate;

    /**
     * 核准日期
     */
    @TableField("approve_date")
    private String approveDate;

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
     * 区县
     */
    @TableField("area")
    private String area;

    /**
     * 企业地址
     */
    @TableField("enterprise_address")
    private String enterpriseAddress;

    /**
     * 最新年报地址
     */
    @TableField("address_of_latest_annual_report")
    private String addressOfLatestAnnualReport;

    /**
     * 企业类型
     */
    @TableField("enterprise_type")
    private String enterpriseType;

    /**
     * 企业评分
     */
    @TableField("score_of_enterprise")
    private Integer scoreOfEnterprise;

    /**
     * 电话
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 更多电话
     */
    @TableField("more_mobile")
    private String moreMobile;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 更多邮箱
     */
    @TableField("more_email")
    private String moreEmail;

    /**
     * 网址
     */
    @TableField("website")
    private String website;

    /**
     * 国标行业门类
     */
    @TableField("national_standard_industry")
    private String nationalStandardIndustry;

    /**
     * 国标行业门类id
     */
    @TableField("national_standard_industry_id")
    private Long nationalStandardIndustryId;

    /**
     * 国标行业大类
     */
    @TableField("national_standard_industry_big")
    private String nationalStandardIndustryBig;

    /**
     * 国标行业大类id
     */
    @TableField("national_standard_industry_big_id")
    private Long nationalStandardIndustryBigId;

    /**
     * 国标行业中类
     */
    @TableField("national_standard_industry_middle")
    private String nationalStandardIndustryMiddle;

    /**
     * 国标行业中类id
     */
    @TableField("national_standard_industry_middle_id")
    private Long nationalStandardIndustryMiddleId;

    /**
     * 国标行业小类
     */
    @TableField("national_standard_industry_small")
    private String nationalStandardIndustrySmall;

    /**
     * 国标行业小类id
     */
    @TableField("national_standard_industry_small_id")
    private Long nationalStandardIndustrySmallId;

    /**
     * 是否拥有专利 0否1是
     */
    @TableField("hava_patent")
    private Boolean havaPatent;

    /**
     * 经营状态
     */
    @TableField("operating_status")
    private String operatingStatus;

    /**
     * 企业规模
     */
    @TableField("scale")
    private String scale;

    /**
     * 企业简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 企业360数据
     */
    @TableField("data360")
    private String data360;
}
