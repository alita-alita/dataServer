package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.data.xiaoai.AdministrativeDivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/admin/administrativeDivision"})
public class AdministrativeDivisionController {

    @Autowired
    private AdministrativeDivisionService administrativeDivisionService;

    /**
     * 获取行政区划列表
     *
     * @return
     */
    @PermissionRelease
    @GetMapping("/getAll")
    public ApiResult<?> getAll() {
        return ApiResult.success(administrativeDivisionService.getAll());
    }

    /**
     * 清除redis缓存
     *
     * @return
     */
    @PermissionRelease
    @GetMapping("/clearRedis")
    public ApiResult<?> clearRedis() {
        administrativeDivisionService.clearRedis();
        return ApiResult.success(true);
    }

}
