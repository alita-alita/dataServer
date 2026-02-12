package cn.idicc.taotie.api.controller.data;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.response.result.APIResponse;
import cn.idicc.taotie.infrastructment.response.result.APIResponseBuilder;
import cn.idicc.taotie.service.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: MengDa
 * @Date: 2023/8/14
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping("/dataSync")
public class ConsumedController {

    @Resource(name = "redisClientInteger")
    private RedisClient<String,Integer> redisClient;

    @GetMapping(value = "/getConsumedInfo/{batch}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRelease
    public APIResponse getConsumedInfo(@PathVariable("batch") String batch) {
        return APIResponseBuilder.success(redisClient.getMap(batch));
    }
}
