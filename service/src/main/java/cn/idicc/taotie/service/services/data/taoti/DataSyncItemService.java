package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.taotie.infrastructment.entity.spider.DataSyncItemDO;
import cn.idicc.taotie.infrastructment.request.spider.DataSyncItemRequest;
import cn.idicc.taotie.infrastructment.response.data.DataSyncDiffRecordDetailResponse;
import cn.idicc.taotie.infrastructment.response.data.DataSyncItemResponse;
import cn.idicc.taotie.infrastructment.response.result.ApiResult;
import cn.idicc.taotie.service.result.CommonPageDTO;
import cn.idicc.taotie.service.task.SparkTaskExecutor;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 数据同步任务数据项表 - 服务接口
 *
 * @author guyongliang
 * @date 2026-01-27
 */
public interface DataSyncItemService {

    /**
     * 新增数据同步任务数据项
     *
     * @param request 请求对象
     * @return 主键ID
     */
    Long save(DataSyncItemRequest request);

    /**
     * 删除数据同步任务数据项
     *
     * @param id 主键ID
     * @return 是否成功
     */
    Boolean delete(Long id);

    /**
     * 更新数据同步任务数据项
     *
     * @param request 请求对象
     * @return 是否成功
     */
    Boolean update(DataSyncItemRequest request);

    /**
     * 根据ID查询数据同步任务数据项
     *
     * @param id 主键ID
     * @return 响应对象
     */
    DataSyncItemResponse getById(Long id);

    /**
     * 查询所有数据同步任务数据项
     *
     * @return 响应对象列表
     */
    List<DataSyncItemResponse> listAll();

    /**
     * 分页查询数据同步任务数据项
     *
     * @param itemName 数据项名称
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    CommonPageDTO<DataSyncItemResponse> pageList(String itemName, Integer pageNum, Integer pageSize);

    /**
     * 提交任务
     *
     * @param itemId 数据项ID
     * @param chainId 产业链ID
     * @param operateType 操作类型 0差异计算1平台->集市3集市->平台4平台->生产
     * @return 响应对象
     */
    Boolean submitDataFlowTask(Long itemId, Long chainId, Integer operateType);

    /**
     * 查询差异数据记录
     *
     * @param itemId 数据项ID
     * @param chainId 产业链ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    CommonPageDTO<DataSyncDiffRecordDetailResponse> queryDiffDataPage(Long itemId, Long chainId, Integer pageNum, Integer pageSize);
}