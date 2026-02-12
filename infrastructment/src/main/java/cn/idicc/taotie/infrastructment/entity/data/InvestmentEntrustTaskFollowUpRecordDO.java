package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/3/27
 * @Description:
 * @version: 1.0
 */
@Data
@TableName("investment_entrust_task_follow_up_record")
public class InvestmentEntrustTaskFollowUpRecordDO extends BaseDO {

    /**
     * 招商委托任务表主键id
     */
    @TableField("investment_entrust_task_id")
    private Long investmentEntrustTaskId;

    /**
     * 跟进日期
     */
    @TableField("follow_up_date")
    private LocalDateTime followUpDate;

    /**
     * 跟进人
     */
    @TableField("follow_up_person")
    private String followUpPerson;

    /**
     * 企业对接人
     */
    @TableField("enterprise_contact_person")
    private String enterpriseContactPerson;

    /**
     * 企业联系方式
     */
    @TableField("enterprise_contact_information")
    private String enterpriseContactInformation;

    /**
     * 跟进概况
     */
    @TableField("follow_up_overview")
    private String followUpOverview;
}
