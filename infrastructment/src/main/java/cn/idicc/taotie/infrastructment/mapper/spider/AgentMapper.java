package cn.idicc.taotie.infrastructment.mapper.spider;


import cn.idicc.taotie.infrastructment.entity.icm.RecordAppTypicalEnterpriseDO;
import cn.idicc.taotie.infrastructment.entity.spider.AgentDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhanghong
 * @date 2025-07-15
 */
@Mapper
public interface AgentMapper extends BaseMapper<AgentDO> {
    IPage<RecordAppTypicalEnterpriseDO> selectPage(Page<Object> objectPage, LambdaQueryWrapper<AgentDO> eq);
}
