package cn.idicc.taotie.infrastructment.entity.data;

import cn.hutool.core.date.DatePattern;
import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/2/22
 * @Description: 招商线索表实体类
 * @version: 1.0
 */
@Data
@TableName("investment_attraction_clue")
public class InvestmentAttractionClueDO extends BaseDO {

    /**
     * 线索状态 0待指派 1已指派
     */
    @TableField("clue_state")
    private Integer clueState;

    /**
     * 被指派人
     */
    @TableField("be_assign_person")
    private String beAssignPerson;

    /**
     * 被指派人id
     */
    @TableField("be_assign_person_id")
    private Long beAssignPersonId;

    /**
     * 是否委托
     */
    @TableField("entrust_or_not")
    private Boolean entrustOrNot;

    /**
     * 是否有投资意向
     */
    @TableField("hava_investment_intention")
    private Boolean havaInvestmentIntention;

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
     * 意向日期
     */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    @TableField("intention_date")
    private LocalDateTime intentionDate;

    /**
     * 推荐日期
     */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    @TableField("recommend_date")
    private LocalDateTime recommendDate;

    /**
     * 系统推荐外部纳入意向操作人
     */
    @TableField("intention_person")
    private String intentionPerson;

    /**
     * 系统推荐外部纳入意向操作人id
     */
    @TableField("intention_person_id")
    private Long intentionPersonId;

    /**
     * 推荐理由详细信息
     */
    @TableField("recommendation_reason_detail")
    private String recommendationReasonDetail;

    /**
     * 线索来源 1系统推荐纳入意向线索 2用户在招商推荐外部纳入意向线索
     */
    @TableField("clue_source")
    private Integer clueSource;

    /**
     * 是否系统推荐 0否1是
     */
    @TableField("is_system_recommend")
    private Boolean isSystemRecommend;

    /**
     * 指派人
     */
    @TableField("assign_person")
    private String assignPerson;

    /**
     * 指派人id
     */
    @TableField("assign_person_id")
    private Long assignPersonId;

    /**
     * 指派日期
     */
    @TableField("assign_date")
    private LocalDateTime assignDate;

    /**
     * 指派备注
     */
    @TableField("assign_remark")
    private String assignRemark;

    /**
     * 线索处理状态 0待处理 1处理中 2已完成
     */
    @TableField("clue_deal_state")
    private Integer clueDealState;

    /**
     * 是否添加走访记录
     */
    @TableField("is_add_visit")
    private Boolean isAddVisit;

    /**
     * 走访日期
     */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    @TableField("visit_date")
    private LocalDateTime visitDate;

    /**
     * 走访人
     */
    @TableField("visit_person")
    private String visitPerson;

    /**
     * 走访人id
     */
    @TableField("visit_person_id")
    private Long visitPersonId;

    /**
     * 是否结束 0否 1是
     */
    @TableField("is_end")
    private Boolean isEnd;
}
