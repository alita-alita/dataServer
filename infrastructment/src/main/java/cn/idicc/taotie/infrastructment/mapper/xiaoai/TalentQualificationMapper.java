package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.xiaoai.TalentQualificationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人才资质表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2024-10-08
 */
@Mapper
public interface TalentQualificationMapper extends BaseMapper<TalentQualificationDO> {

	List<String> getBytalentMd5(@Param("names") List<String> qualificationNames, @Param("talentMd5s") List<String> talentMd5s);
}
