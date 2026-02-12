package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.IndustryChainAtomNodeDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IndustryChainAtomNodeMapper extends BaseMapper<IndustryChainAtomNodeDO> {

	IndustryChainAtomNodeDO selectOne(@Param("atomNodeName") String atomNodeName);

	int deleteById(@Param("id") Long id);
}
