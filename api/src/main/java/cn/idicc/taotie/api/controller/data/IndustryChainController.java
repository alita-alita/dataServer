package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wd
 * @Description 产业链controller
 * @date 12/19/22 9:39 AM
 */
@RestController
@RequestMapping({"/admin/industryChain"})
@Slf4j
public class IndustryChainController {

    @Resource
    private IndustryChainService industryChainService;

    /**
     * 获取所有产业链
     *
     * @return
     */
    @PermissionRelease
    @GetMapping("/getAll")
    public ApiResult<?> getAll(@RequestParam(value = "isOnline", required = false, defaultValue = "true") boolean isOnline) {
        return ApiResult.success(industryChainService.getAll(isOnline));
    }
}
