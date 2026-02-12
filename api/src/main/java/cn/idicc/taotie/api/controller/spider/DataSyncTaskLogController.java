package cn.idicc.taotie.api.controller.spider;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.entity.spider.DataSyncItemDO;
import cn.idicc.taotie.infrastructment.request.icm.task.SubmitTaskRequest;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.data.taoti.DataSyncTaskLogService;
import cn.idicc.taotie.service.task.SparkTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据同步执行日志表 - 控制器
 *
 * @author taotie
 * @date 2026-01-27
 */
@RestController
@RequestMapping("/dataSyncTaskLog")
public class DataSyncTaskLogController {

    @Autowired
    private DataSyncTaskLogService dataSyncTaskLogService;

    /**
     * 根据数据项ID和产业链ID查询
     *
     * @param itemId 数据项ID
     * @return 响应对象
     */
    @PermissionRelease
    @GetMapping("/query")
    public ApiResult query(@RequestParam(value = "itemId") Long itemId, @RequestParam(value = "chainId") Long chainId) {
        return ApiResult.success(dataSyncTaskLogService.getDataSyncTaskLog(itemId, chainId));
    }

    /**
     * 根据数据项ID和产业链ID查询
     *
     * @return 响应对象
     */
    @PermissionRelease
    @GetMapping("/queryAll")
    public ApiResult queryAll(@RequestParam(value = "chainId") Long chainId) {
        return ApiResult.success(dataSyncTaskLogService.queryAll(chainId));
    }

}