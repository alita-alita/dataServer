package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 同步任务表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2025-02-11
 */
public interface SyncTaskMapper extends BaseMapper<SyncTaskDO> {

    Integer updateTaskLock(@Param("id")Long id,@Param("is_lock")Boolean isLock);
}
