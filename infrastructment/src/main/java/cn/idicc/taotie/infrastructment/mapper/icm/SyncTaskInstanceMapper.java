package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskInstanceDO;
import cn.idicc.taotie.infrastructment.response.icm.task.TaskInstanceDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 同步任务实例 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-02-11
 */
public interface SyncTaskInstanceMapper extends BaseMapper<SyncTaskInstanceDO> {

    IPage<TaskInstanceDTO> page(@Param("page")IPage<?> page,@Param("task_id")Long taskId);
}
