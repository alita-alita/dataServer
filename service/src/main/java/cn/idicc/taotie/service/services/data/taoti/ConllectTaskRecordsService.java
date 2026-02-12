package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.entity.spider.CollectTaskRecords;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface ConllectTaskRecordsService {
    /**
     * 添加采集任务记录
     */
    Integer addConllectTaskRecords(CollectTaskRecords collectTaskRecords);

    /**
     * 采集任务记录列表
     * @return
     */
    List<CollectTaskRecords> listConllectTaskRecords(CollectTaskRecords collectTaskRecordsType);
}
