package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/2/24
 * @Description: 招商线索跟进记录DO
 * @version: 1.0
 */
@Data
@TableName("investment_attraction_clue_follow_up_record")
public class InvestmentAttractionClueFollowUpRecordDO extends BaseDO {

    /**
     * 招商线索id
     */
    @TableField("clue_id")
    private Long clueId;


    /**
     * 跟进日期
     */
    @TableField("follow_up_date")
    private LocalDateTime followUpDate;

    /**
     * 填写人
     */
    @TableField("fill_in_person")
    private String fillInPerson;

    /**
     * 填写人id
     */
    @TableField("fill_in_person_id")
    private Long fillInPersonId;

    /**
     * 是否有投资意向
     */
    @TableField("hava_investment_intention")
    private Boolean havaInvestmentIntention;

    /**
     * 跟进概述
     */
    @TableField("overview")
    private String overview;
}
