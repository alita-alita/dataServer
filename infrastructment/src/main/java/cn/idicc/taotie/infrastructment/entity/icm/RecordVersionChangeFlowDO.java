package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 版本状态变更表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_version_change_flow")
public class RecordVersionChangeFlowDO extends BaseDO {

    /**
     * 版本
     */
    private String version;

    /**
     * 之前状态
     */
    private Integer fromState;

    /**
     * 现在状态
     */
    private Integer toState;

    /**
     * 详情
     */
    private String flowDesc;
}
