package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.DataSyncTaskLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据同步执行日志表 - Mapper接口
 *
 * @author taotie
 * @date 2026-01-27
 */
@Mapper
public interface DataSyncTaskLogMapper extends BaseMapper<DataSyncTaskLogDO> {
}