package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2022/12/24
 * @Description: 企业和企业标签关联关系
 * @version: 1.0
 */
@Data
@TableName("enterprise_correlation_label")
public class EnterpriseCorrelationLabelDO extends BaseDO {

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 企业标签id
     */
    @TableField("label_id")
    private Long labelId;

    /**
     * 是否确认 0否1是
     */
    @TableField("confirm")
    private Boolean confirm;

    /**
     * 批次号
     */
    @TableField("batch_number")
    private String batchNumber;
}
