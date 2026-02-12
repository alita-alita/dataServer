package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterpriseAddRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterprisePageRequest;
import cn.idicc.taotie.infrastructment.request.icm.RecordTypicalEnterpriseUpdateRequest;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.icm.RecordAppTypicalEnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: MengDa
 * @Date: 2025/2/11
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping({"/record/admin/typical"})
@Slf4j
public class RecordTypicalEnterpriseController {

    @Autowired
    private RecordAppTypicalEnterpriseService recordAppTypicalEnterpriseService;

    @PermissionRelease
    @PostMapping("/enterprise/add")
    public ApiResult<?> add(@RequestBody RecordTypicalEnterpriseAddRequest request) {
        return ApiResult.success(recordAppTypicalEnterpriseService.addTypicalEnterprise(request));
    }

    @PermissionRelease
    @PostMapping("/enterprise/update")
    public ApiResult<?> update(@RequestBody RecordTypicalEnterpriseUpdateRequest request) {
        return ApiResult.success(recordAppTypicalEnterpriseService.updateTypicalEnterprise(request));
    }

    @PermissionRelease
    @GetMapping("/enterprise/delete")
    public ApiResult<?> delete(@RequestParam Long id) {
        return ApiResult.success(recordAppTypicalEnterpriseService.deleteById(id));
    }

    @PermissionRelease
    @GetMapping("/enterprise/addAllToSuspected")
    public ApiResult<?> addAllToSuspected(@RequestParam String chainId) {
        return ApiResult.success(recordAppTypicalEnterpriseService.addAllToSuspected(chainId));
    }

    @PermissionRelease
    @PostMapping("/enterprise/page")
    public ApiResult<?> addAllToSuspected(@RequestBody RecordTypicalEnterprisePageRequest request) {
        return ApiResult.success(recordAppTypicalEnterpriseService.pageSearch(request));
    }
}
