package cn.idicc.taotie.infrastructment.mapper.data;

import cn.idicc.taotie.infrastructment.entity.data.InvestmentRecommendationAuditDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: WangZi
 * @Date: 2023/3/23
 * @Description: 招商推荐审核记录mapper
 * @version: 1.0
 */
@Mapper
public interface InvestmentRecommendationAuditMapper extends BaseMapper<InvestmentRecommendationAuditDO> {

    void physicsDeleteByRelationIds(@Param("relationIds") List<Long> relationIds);

}
