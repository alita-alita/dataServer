package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.DataSyncItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据同步任务数据项表 - Mapper接口
 *
 * @author guyongliang
 * @date 2026-01-27
 */
@Mapper
public interface DataSyncItemMapper extends BaseMapper<DataSyncItemDO> {
}