package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.CollectTaskRecords;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConllectTaskRecordsMapper {
    /**
     * 添加采集任务记录
     */
    Integer addConllectTaskRecords(CollectTaskRecords collectTaskRecords);

    /**
     * 采集记录列表
     */
    List<CollectTaskRecords> listConllectTaskRecords(Integer collectTaskRecordsType);
}
