package cn.idicc.taotie.api.controller.spider;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.dto.spider.AgentDTO;
import cn.idicc.taotie.infrastructment.request.spider.AgentAddRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentQueryPageRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentTestRequest;
import cn.idicc.taotie.infrastructment.request.spider.AgentUpdateRequest;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.data.taoti.AgentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 智能体管理
 *
 * @author zhanghong
 * @date 2025-07-15
 */
@RestController
@RequestMapping({"/agent"})
public class AgentController {

    @Autowired
    private AgentService agentService;

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    @PermissionRelease
    @GetMapping("/getListPage")
    public ApiResult<IPage<AgentDTO>> getListPage(AgentQueryPageRequest request) {
        return ApiResult.success(agentService.getListPage(request));
    }

    /**
     * 详情查询
     *
     * @param id
     * @return
     */
    @PermissionRelease
    @GetMapping("/getDetail")
    public ApiResult<AgentDTO> getDetail(@RequestParam Long id) {
        return ApiResult.success(agentService.detail(id));
    }

    /**
     * 新增
     *
     * @param param
     * @return
     */
    @PermissionRelease
    @PostMapping("/add")
    public ApiResult<?> add(@RequestBody @Validated AgentAddRequest param) {
        agentService.save(param);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param param
     * @return
     */
    @PermissionRelease
    @PostMapping("/update")
    public ApiResult<?> update(@RequestBody @Validated AgentUpdateRequest param) {
        agentService.update(param);
        return ApiResult.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PermissionRelease
    @GetMapping("/delete")
    public ApiResult<?> delete(@RequestParam Long id) {
        agentService.delete(id);
        return ApiResult.success();
    }

    /**
     * 智能体测试接口
     *
     * @param param
     * @return
     */
    @PermissionRelease
    @PostMapping("/test")
    public ApiResult<?> test(@RequestBody @Validated AgentTestRequest param) {
        return ApiResult.success(agentService.testAgent(param));
    }


    ;

}
