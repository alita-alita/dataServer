package cn.idicc.taotie.api.controller.sync;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.provider.api.service.EnterpriseSyncRpcService;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseProductService;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据更新
 *
 * @Author: MengDa
 * @Date: 2025/3/13
 * @Description:
 * @version: 1.0
 */
@Log4j2
@RestController
@RequestMapping("/sync/admin")
public class SyncController {

    @Autowired
    private EnterpriseSyncRpcService enterpriseSyncRpcService;


    @Autowired
    private EnterpriseProductService enterpriseProductService;

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 同步企业指标信息
     * 从数据库更新到ES
     *
     * @return
     */
    @PermissionRelease
    @GetMapping("/update/development/index")
    public ApiResult<?> updateAllDevelopmentIndex() {
        enterpriseSyncRpcService.updateAllDevelopmentIndex();
        return ApiResult.success(true);
    }

    /**
     * 同步企业企业籍贯信息
     *
     * @return
     */
    @TokenByPass
    @PermissionRelease
    @GetMapping("/update/ancestor")
    public ApiResult<?> updateAllAncestor() {
        enterpriseSyncRpcService.updateAllAncestor();
        return ApiResult.success(true);
    }

    /**
     * 同步企业校友信息
     *
     * @return
     */
    @TokenByPass
    @PermissionRelease
    @GetMapping("/update/academia")
    public ApiResult<?> updateAllAcademia() {
        enterpriseSyncRpcService.updateAllAcademia();
        return ApiResult.success(true);
    }

    /**
     * 同步产品ES
     *
     * @param chainId
     * @param uniCode
     * @return
     */
    @PermissionRelease
    @TokenByPass
    @GetMapping("/update/enterpriseProduct")
    public ApiResult<?> updateAllEnterpriseProduct(Long chainId, String uniCode) {
        enterpriseProductService.doSyncDataToEs(chainId, uniCode);
        return ApiResult.success(true);
    }

    @PermissionRelease
    @TokenByPass
    @GetMapping("/update/enterpriserEs")
    public ApiResult<?> updateEnterpriseById(@RequestParam("enterpriseId") Long id){
        enterpriseService.doSyncDataToEs(id);
        return ApiResult.success(true);
    }
}
