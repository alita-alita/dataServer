package cn.idicc.taotie.api.controller;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.pangu.service.TestRpcService;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAppEnterpriseIndustryChainSuspectedMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: WangZi
 * @Date: 2023/8/31
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference(interfaceClass = TestRpcService.class, check = false,group = "${dubbo.registry.group}",version = "1.0.0")
    private TestRpcService testRpcService;

    @Autowired
    private RecordAppEnterpriseIndustryChainSuspectedMapper recordAppEnterpriseIndustryChainSuspectedMapper;

    @GetMapping("/test")
    @PermissionRelease
    @TokenByPass
    public APIResponse<?> test() {
        String test = testRpcService.test();
        return APIResponseBuilder.success(test);
    }
}
