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
 * 同步任务执行记录
 * </p>
 *
 * @author MengDa
 * @since 2025-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sync_task_instance_log")
public class SyncTaskInstanceLogDO extends BaseDO {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务实例ID
     */
    private Long instanceId;

    /**
     * python job ID
     */
    private String jobId;

    /**
     * job日志
     */
    private String jobLog;

    /**
     * 状态
     */
    private Integer status;

}
