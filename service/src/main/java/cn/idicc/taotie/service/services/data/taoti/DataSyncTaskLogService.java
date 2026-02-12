package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.response.data.DataSyncTaskLogResponse;

import java.util.List;

/**
 * 数据同步执行日志表 - 服务接口
 *
 * @author taotie
 * @date 2026-01-27
 */
public interface DataSyncTaskLogService {

    DataSyncTaskLogResponse getDataSyncTaskLog(Long itemId, Long chainId);

    List<DataSyncTaskLogResponse> queryAll(Long chainId);
}