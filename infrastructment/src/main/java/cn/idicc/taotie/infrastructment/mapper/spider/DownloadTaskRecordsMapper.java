package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.DownloadTaskRecords;
import cn.idicc.taotie.infrastructment.request.data.DownloadTaskRecordsReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DownloadTaskRecordsMapper {
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
    List<DownloadTaskRecords> listDownloadTaskRecords();


    Integer delLogicDownloadTaskRecords(Integer downloadTaskRecordsId);




}
