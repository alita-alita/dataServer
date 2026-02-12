package cn.idicc.taotie.infrastructment.mapper.xiaoai;

import cn.idicc.taotie.infrastructment.entity.data.IndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.xiaoai.TalentIndustryChainRelationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人才产业链关联表 Mapper 接口
 * </p>
 *
 * @author MengDa
 * @since 2024-10-08
 */
@Mapper
public interface TalentIndustryChainRelationMapper extends BaseMapper<TalentIndustryChainRelationDO> {

	List<IndustryChainDO> selectByTalent(@Param("talent_md5")String talentMd5);

}
