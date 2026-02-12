package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.SyncTaskDO;
import cn.idicc.taotie.infrastructment.request.icm.task.JobPageRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.TaskInstancePageRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.TaskPageRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.SubmitTaskRequest;
import cn.idicc.taotie.infrastructment.response.icm.task.TaskInstanceDTO;
import cn.idicc.taotie.infrastructment.response.icm.task.TaskInstanceJobDTO;
import cn.idicc.taotie.service.task.logs.TaskLogDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/2/11
 * @Description:
 * @version: 1.0
 */
public interface TaskService {

    IPage<SyncTaskDO> taskPage(TaskPageRequest request);
    IPage<TaskInstanceDTO> taskInstancePage(TaskInstancePageRequest request);
    IPage<TaskInstanceJobDTO> taskInstanceJobPage(JobPageRequest request);
    List<TaskLogDTO> queryLogByJobId(String jobId,Integer offset,String type);
    void runTask(SubmitTaskRequest request);
}
