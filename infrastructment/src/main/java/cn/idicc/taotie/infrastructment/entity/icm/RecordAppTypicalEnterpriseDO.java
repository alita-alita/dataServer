package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 典型企业表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_app_typical_enterprise")
public class RecordAppTypicalEnterpriseDO extends BaseDO {

    /**
     * 企业id
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业社会统一信用代码
     */
    private String enterpriseUniCode;

    /**
     * 产业链标签id
     */
    private Long industryLabelId;

    /**
     * 产业链标签
     */
    private String industryLabelName;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 产业链节点id
     */
    @TableField(exist = false)
    private Long industryNodeId;

    /**
     * 产业链节点
     */
    @TableField(exist = false)
    private String industryNodeName;
}
