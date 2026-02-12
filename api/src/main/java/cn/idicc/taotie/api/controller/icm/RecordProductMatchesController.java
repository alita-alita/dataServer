package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckRecordStateEnum;
import cn.idicc.taotie.infrastructment.request.icm.RecordAiCheckRecordQueryRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordProductMatchesAiCheckQueryRequest;
import cn.idicc.taotie.infrastructment.response.icm.RecordProductMatchesAiCheckDTO;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.icm.RecordAiCheckRecordService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesAiCheckService;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: MengDa
 * @Date: 2025/1/6
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping({"/record/admin/product/matches"})
public class RecordProductMatchesController {

    @Autowired
    private RecordProductMatchesService productMatchesService;

    @PermissionRelease
    @PostMapping("/page")
    public ApiResult<?> page(@RequestBody RecordProductMatchesAiCheckQueryRequest request) {
        return ApiResult.success(productMatchesService.pageList(request));
    }

    @PermissionRelease
    @PostMapping("/update")
    public ApiResult<?> update(@RequestBody RecordProductMatchesAiCheckDTO request) {
        return ApiResult.success(productMatchesService.updateRecord(request));
    }
}
