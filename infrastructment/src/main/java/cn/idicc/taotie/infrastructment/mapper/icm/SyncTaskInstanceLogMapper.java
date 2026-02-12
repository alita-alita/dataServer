package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskInstanceLogDO;
import cn.idicc.taotie.infrastructment.response.icm.task.TaskInstanceJobDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 同步任务执行记录 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-02-11
 */
public interface SyncTaskInstanceLogMapper extends BaseMapper<SyncTaskInstanceLogDO> {

    IPage<TaskInstanceJobDTO> page(@Param("page")IPage<?> page,@Param("instance_id")Long instanceId,@Param("task_id")Long taskId);
}
