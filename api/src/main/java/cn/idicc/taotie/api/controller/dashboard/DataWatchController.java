package cn.idicc.taotie.api.controller.dashboard;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.request.data.DataWatchQueryRequest;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.data.taoti.DataWatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping({"/admin/dashboard"})
@Slf4j
public class DataWatchController {

    @Resource
    private DataWatchService dataWatchService;

    /**
     * 大屏数据总量
     *
     * @return
     */
    @GetMapping("/queryTotalCount")
    @PermissionRelease
    public ApiResult<?> queryTotalCount() {
        return ApiResult.success(dataWatchService.queryTotalCount());
    }

    /**
     * 图表数据
     *
     * @return
     */
    @PostMapping("/queryChartData")
    @PermissionRelease
    public ApiResult<?> queryChartData(@RequestBody DataWatchQueryRequest request) {
        return ApiResult.success(dataWatchService.queryChartData(request));
    }

}
