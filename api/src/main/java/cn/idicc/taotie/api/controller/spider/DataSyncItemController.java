package cn.idicc.taotie.api.controller.spider;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.entity.spider.DataSyncItemDO;
import cn.idicc.taotie.infrastructment.request.spider.DataSyncItemRequest;
import cn.idicc.taotie.infrastructment.response.data.DataSyncDiffRecordDetailResponse;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.services.data.taoti.DataSyncItemService;
import cn.idicc.taotie.service.task.SparkTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 数据同步任务数据项表 - 控制器
 *
 * @author guyongliang
 * @date 2026-01-27
 */
@RestController
@RequestMapping("/dataSyncItem")
public class DataSyncItemController {

    @Autowired
    private DataSyncItemService dataSyncItemService;

    /**
     * 新增数据同步任务数据项
     *
     * @param request 请求对象
     * @return 主键ID
     */
    @PermissionRelease
    @PostMapping("/save")
    public ApiResult save(@Validated @RequestBody DataSyncItemRequest request) {
        return ApiResult.success(dataSyncItemService.save(request));
    }

    /**
     * 删除数据同步任务数据项
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @PermissionRelease
    @GetMapping("/delete/{id}")
    public ApiResult delete(@PathVariable Long id) {
        return ApiResult.success(dataSyncItemService.delete(id));
    }

    /**
     * 更新数据同步任务数据项
     *
     * @param request 请求对象
     * @return 是否成功
     */
    @PermissionRelease
    @PostMapping("/update")
    public ApiResult update(@Validated @RequestBody DataSyncItemRequest request) {
        return ApiResult.success(dataSyncItemService.update(request));
    }

    /**
     * 根据ID查询数据同步任务数据项
     *
     * @param id 主键ID
     * @return 响应对象
     */
    @PermissionRelease
    @GetMapping("/get/{id}")
    public ApiResult getById(@PathVariable Long id) {
        return ApiResult.success(dataSyncItemService.getById(id));
    }

    /**
     * 查询所有数据同步任务数据项
     *
     * @return 响应对象列表
     */
    @PermissionRelease
    @GetMapping("/listAll")
    public ApiResult listAll() {
        return ApiResult.success(dataSyncItemService.listAll());
    }

    /**
     * 分页查询数据同步任务数据项
     *
     * @param itemName 数据项名称
     * @param pageNum  页码
     * @return 分页结果
     */
    @PermissionRelease
    @GetMapping("/pageList")
    public ApiResult pageList(@RequestParam(value = "itemName", required = false) String itemName,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ApiResult.success(dataSyncItemService.pageList(itemName, pageNum, pageSize));
    }

    /**
     * 提交任务
     *
     * @param itemId 数据项ID
     * @param chainId 产业链ID
     * @param operateType 操作类型 0差异计算1平台->集市3集市->平台4平台->生产
     * @return 响应对象
     */
    @PermissionRelease
    @GetMapping("/submitTask")
    public ApiResult<?> submitTask(@RequestParam(value = "itemId") Long itemId,
                                   @RequestParam(value = "chainId") Long chainId,
                                   @RequestParam(value = "operateType") Integer operateType) {
        return ApiResult.success(dataSyncItemService.submitDataFlowTask(itemId, chainId, operateType));
    }

    /**
     * 查询差异数据记录
     */
    @PermissionRelease
    @GetMapping("/queryDiffDataPage")
    public ApiResult<?> queryDiffDataPage(@RequestParam(value = "itemId") Long itemId,
                                          @RequestParam(value = "chainId") Long chainId,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ApiResult.success(dataSyncItemService.queryDiffDataPage(itemId, chainId, pageNum, pageSize));
    }

}