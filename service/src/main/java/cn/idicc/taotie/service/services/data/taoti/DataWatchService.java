package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.request.data.DataWatchQueryRequest;
import cn.idicc.taotie.infrastructment.response.data.DataWatchDailyCountStatDTO;

import java.util.List;
import java.util.Map;

public interface DataWatchService {

    Map<String, Object> queryTotalCount();

    List<DataWatchDailyCountStatDTO> queryChartData(DataWatchQueryRequest request);
}
