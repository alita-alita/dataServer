package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/6/8
 * @Description: 招商企业和推荐模型记录关联关系表
 * @version: 1.0
 */
@Data
@TableName("investment_enterprise_relation_model")
public class InvestmentEnterpriseRelationModelDO extends BaseDO {

    /**
     * 招商企业表主键id
     */
    @TableField("investment_enterprise_id")
    private Long investmentEnterpriseId;

    /**
     * 招商推荐记录主键id
     */
    @TableField("investment_enterprise_recommend_id")
    private Long investmentEnterpriseRecommendId;

    /**
     * 推荐类型：1亲商招商 2资源招商 3链主招商 4政策招商 5舆情招商 6AI+招商
     */
    @TableField("type")
    private Integer type;

    /**
     * 关联亲商姓名
     */
    @TableField("relation_user_name")
    private String relationUserName;

    /**
     * 关联亲商姓名唯一标识
     */
    @TableField("relation_user_name_only_logo")
    private String relationUserNameOnlyLogo;

    /**
     * 亲商模式推荐关联关系
     */
    @TableField("association_relationship")
    private String associationRelationship;

    /**
     * 资源需求
     */
    @TableField("resource_needs")
    private String resourceNeeds;

    /**
     * 关联本地企业社会统一信用代码
     */
    @TableField("associate_local_enterprise_code")
    private String associateLocalEnterpriseCode;

    /**
     * 关联本地企业名称
     */
    @TableField("associate_local_enterprise_name")
    private String associateLocalEnterpriseName;

    /**
     * 供应关系
     */
    @TableField("supply_relation")
    private String supplyRelation;

    /**
     * 关联政策
     */
    @TableField("associate_policy")
    private String associatePolicy;

    /**
     * 关联资讯url
     */
    @TableField("associate_information_url")
    private String associateInformationUrl;

    /**
     * 对外投资满意度
     */
    @TableField("outside_invest_satisfaction")
    private String outsideInvestSatisfaction;

    /**
     * 推荐日期
     */
    @TableField("recommended_date")
    private LocalDateTime recommendedDate;
}
