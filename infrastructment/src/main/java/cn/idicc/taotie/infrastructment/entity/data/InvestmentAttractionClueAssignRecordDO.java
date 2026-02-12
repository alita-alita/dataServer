package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/5/12
 * @Description: 招商线索指派记录实体类
 * @version: 1.0
 */
@Data
@TableName("investment_attraction_clue_assign_record")
public class InvestmentAttractionClueAssignRecordDO extends BaseDO {

    /**
     * 线索id
     */
    @TableField("clue_id")
    private Long clueId;

    /**
     * 指派日期
     */
    @TableField("assign_date")
    private LocalDateTime assignDate;

    /**
     * 指派人id
     */
    @TableField("operate_user_id")
    private Long operateUserId;

    /**
     * 指派人名称
     */
    @TableField("operate_user_name")
    private String operateUserName;

    /**
     * 被指派人id
     */
    @TableField("assign_user_id")
    private Long assignUserId;

    /**
     * 被指派人名称
     */
    @TableField("assign_user_name")
    private String assignUserName;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
