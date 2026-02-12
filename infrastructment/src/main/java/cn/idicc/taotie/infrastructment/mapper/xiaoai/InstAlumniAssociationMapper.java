package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.xiaoai.InstAlumniAssociationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InstAlumniAssociationMapper extends BaseMapper<InstAlumniAssociationDO> {

	List<InstAlumniAssociationDO> getRefAcademicByUniCode(@Param("uniCode") String uniCode);

}
