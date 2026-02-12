package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.InputIdBaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangZi
 * @Date: 2023/5/10
 * @Description: 招商企业实体类
 * @version: 1.0
 */
@Data
@TableName("investment_enterprise")
public class InvestmentEnterpriseDO extends InputIdBaseDO {

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
     * 分配状态 0未分配 1跟进中 2已失效
     */
    @TableField("allocation_state")
    private Integer allocationState;

    /**
     * 失效时间
     */
    @TableField("failure_time")
    private LocalDateTime failureTime;

    /**
     * 是否完结
     */
    @TableField("is_end")
    private Boolean isEnd;
}
