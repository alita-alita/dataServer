package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.InputIdBaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/5/8
 * @Description: 招商企业推荐记录实体类
 * @version: 1.0
 */
@Data
@TableName("investment_enterprise_recommend")
public class InvestmentEnterpriseRecommendDO extends InputIdBaseDO {

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 入驻机构id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 推荐日期
     */
    @TableField("recommended_date")
    private LocalDateTime recommendedDate;

    /**
     * 推荐理由详细信息
     */
    @TableField("recommendation_reason_detail")
    private String recommendationReasonDetail;

    /**
     * 审核状态 0待审核 1已发布 2未通过 3已下线
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 批次号
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 推荐类型：0后台添加 1亲商招商 2资源招商 3链主招商 4政策招商 5舆情招商 6AI+招商
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
     * 对外投资意愿
     */
    @TableField("outside_invest_satisfaction")
    private String outsideInvestSatisfaction;
}
