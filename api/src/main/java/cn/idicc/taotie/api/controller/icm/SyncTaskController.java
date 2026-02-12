package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.request.icm.task.JobPageRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.SubmitTaskRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.TaskInstancePageRequest;
import cn.idicc.taotie.infrastructment.request.icm.task.TaskPageRequest;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.icm.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: MengDa
 * @Date: 2025/2/12
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping({"/record/admin/sync"})
public class SyncTaskController {

    @Autowired
    private TaskService taskService;

    @PermissionRelease
    @PostMapping("/submit")
    public ApiResult<?> submitTask(@RequestBody SubmitTaskRequest request) {
        taskService.runTask(request);
        return ApiResult.success();
    }

    @PermissionRelease
    @PostMapping("/task/page")
    public ApiResult<?> submitTask(@RequestBody TaskPageRequest request) {
        return ApiResult.success(taskService.taskPage(request));
    }

    @PermissionRelease
    @PostMapping("/instance/page")
    public ApiResult<?> submitTask(@RequestBody TaskInstancePageRequest request) {
        return ApiResult.success(taskService.taskInstancePage(request));
    }

    @PermissionRelease
    @PostMapping("/job/page")
    public ApiResult<?> submitTask(@RequestBody JobPageRequest request) {
        return ApiResult.success(taskService.taskInstanceJobPage(request));
    }

    @PermissionRelease
    @GetMapping("/job/log")
    public ApiResult<?> submitTask(@RequestParam(name = "jobId") String jobId,
                                   @RequestParam(name = "offset",required = false) Integer offset,
                                   @RequestParam(name = "type",required = false) String type) {
        return ApiResult.success(taskService.queryLogByJobId(jobId,offset,type));
    }
}
