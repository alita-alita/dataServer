package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/3/23
 * @Description: 招商推荐审核记录实体类
 * @version: 1.0
 */
@Data
@TableName("investment_recommendation_audit")
public class InvestmentRecommendationAuditDO extends BaseDO {

    /**
     * 招商推荐记录id
     */
    @TableField("investment_recommendation_id")
    private Long investmentRecommendationId;

    /**
     * 审核状态 0审核不通过 1审核通过
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 审核意见
     */
    @TableField("audit_opinion")
    private String auditOpinion;
}
