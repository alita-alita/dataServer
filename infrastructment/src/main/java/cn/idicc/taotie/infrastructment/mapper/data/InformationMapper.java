package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.InformationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description:
 * @version: 1.0
 */
@Mapper
public interface InformationMapper extends BaseMapper<InformationDO> {
}
