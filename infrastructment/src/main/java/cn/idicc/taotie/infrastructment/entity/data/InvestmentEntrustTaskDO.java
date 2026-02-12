package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/3/27
 * @Description:
 * @version: 1.0
 */
@Data
@TableName("investment_entrust_task")
public class InvestmentEntrustTaskDO extends BaseDO {

    /**
     * 招商线索id
     */
    @TableField("clue_id")
    private Long clueId;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 统一社会信用代码
     */
    @TableField("unified_social_credit_code")
    private String unifiedSocialCreditCode;

    /**
     * 委托机构id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 委托人id
     */
    @TableField("entrust_person_id")
    private Long entrustPersonId;

    /**
     * 委托时间戳
     */
    @TableField("entrust_time_stamp")
    private Long entrustTimeStamp;

    /**
     * 任务到期时间戳
     */
    @TableField("maturity_time_stamp")
    private Long maturityTimeStamp;

    /**
     * 任务完成时剩余时间戳
     */
    @TableField("finish_remain_time_stamp")
    private Long finishRemainTimeStamp;

    /**
     * 跟进状态 0未开始 1进行中 2已完成 3延期
     */
    @TableField("follow_up_status")
    private Integer followUpStatus;

    /**
     * 委托备注
     */
    @TableField("remark")
    private String remark;
}
