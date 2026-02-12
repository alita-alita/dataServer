package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.DataSyncDiffRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据同步任务差异数据记录表 - Mapper接口
 *
 * @author guyongliang
 * @date 2026-02-03
 */
@Mapper
public interface DataSyncDiffRecordMapper extends BaseMapper<DataSyncDiffRecordDO> {
}