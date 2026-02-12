package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: WangZi
 * @Date: 2022/12/24
 * @Description: 企业标签
 * @version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("enterprise_label")
public class EnterpriseLabelDO extends BaseDO {

    /**
     * 企业标签名称
     */
    @TableField("label_name")
    private String labelName;

    /**
     * 企业类型id
     */
    @TableField("label_type_id")
    private Long labelTypeId;

    /**
     * 排序字段
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否资质标签  0否1是
     */
    @TableField("is_qualification")
    private Boolean isQualification;
}
