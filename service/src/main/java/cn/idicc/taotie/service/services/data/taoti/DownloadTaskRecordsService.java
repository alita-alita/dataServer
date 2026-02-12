package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.entity.spider.DownloadTaskRecords;
import cn.idicc.taotie.infrastructment.request.data.DownloadTaskRecordsReq;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DownloadTaskRecordsService {
    /**
     * 添加下载任务记录
     * @param downloadTaskRecords
     * @return
     */
    Integer addDownloadTaskRecords(DownloadTaskRecords downloadTaskRecords);

    /**
     * 查询下载任务记录
     * @return
     */
    PageInfo<DownloadTaskRecords> listDownloadTaskRecords  (DownloadTaskRecordsReq downloadTaskRecordsReq);

    /**
     * 删除下载任务记录
     * @param downloadTaskRecords
     * @return
     */
    Integer delLogicDownloadTaskRecords(DownloadTaskRecords downloadTaskRecords);


}
