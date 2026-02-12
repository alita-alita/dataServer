package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 同步任务表
 * </p>
 *
 * @author MengDa
 * @since 2025-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sync_task")
public class SyncTaskDO extends BaseDO {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String nameDesc;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 所需参数备注
     */
    private String needParams;

    /**
     * 是否锁住
     */
    private Boolean isLock;
}
