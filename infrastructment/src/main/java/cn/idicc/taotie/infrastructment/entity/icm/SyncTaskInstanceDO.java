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
 * 同步任务实例
 * </p>
 *
 * @author MengDa
 * @since 2025-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sync_task_instance")
public class SyncTaskInstanceDO extends BaseDO {

    /**
     * 任务名称
     */
    private Long taskId;

    /**
     * 运行备注
     */
    private String instanceDesc;

    /**
     * 参数
     */
    private String param;

    /**
     * 任务状态
     */
    private String status;


}
