package cn.idicc.taotie.api.controller.icm;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.error.ErrorCode;
import cn.idicc.taotie.infrastructment.request.IdsRequest;
import cn.idicc.taotie.infrastructment.request.icm.*;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.infrastructment.utils.ExcelUtils;
import cn.idicc.taotie.service.services.icm.RecordProductMatchesDissociatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: MengDa
 * @Date: 2025/5/7
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping({"/record/admin/product/dissociated"})
public class RecordProductMatchesDissociatedController {

    @Autowired
    private RecordProductMatchesDissociatedService recordProductMatchesDissociatedService;

    @PermissionRelease
    @GetMapping("/refresh")
    public ApiResult<?> refreshAiMatchesResult(@RequestParam Long chainId) {
        recordProductMatchesDissociatedService.refreshAiMatchesResult(chainId);
        return ApiResult.success();
    }

    @PermissionRelease
    @PostMapping("/pageList")
    public ApiResult<?> pageList(@RequestBody RecordProductMatchesDissociatedQueryRequest request) {
        return ApiResult.success(recordProductMatchesDissociatedService.pageList(request));
    }

    @PermissionRelease
    @PostMapping("/ignore")
    public ApiResult<?> ignore(@RequestBody IdsRequest request) {
        recordProductMatchesDissociatedService.batchIgnore(request.getIds());
        return ApiResult.success();
    }

    @PermissionRelease
    @PostMapping("/notIgnore")
    public ApiResult<?> notIgnore(@RequestBody IdsRequest request) {
        recordProductMatchesDissociatedService.batchNotIgnore(request.getIds());
        return ApiResult.success();
    }

    @PermissionRelease
    @PostMapping("/manualMount")
    public ApiResult<?> manualMount(@RequestBody RecordProductMatchesDissociatedUpdateRequest request) {
        recordProductMatchesDissociatedService.manualMount(request);
        return ApiResult.success();
    }

    @PermissionRelease
    @PostMapping("/updateProductInfo")
    public ApiResult<?> updateProductInfo(@RequestBody RecordProductMatchesDissociatedUpdateRequest request) {
        recordProductMatchesDissociatedService.updateProductInfo(request);
        return ApiResult.success();
    }

    /**
     * 模板下载
     */
    @GetMapping("/templateDownload")
    @PermissionRelease
    public void templateDownload(HttpServletResponse response) {
        ExcelUtils.exportTemplate(response, "游离企业", RecordProductMatchesDissociatedTemplateDownload.class,true);
    }


    /**
     * 导入名录
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping( "/import")
    @PermissionRelease
    public ApiResult<?> importData(MultipartFile file, RecordProductMatchesDissociatedQueryRequest request)throws Exception{
        recordProductMatchesDissociatedService.importData(file,request);
        return ApiResult.success();
    }

}
