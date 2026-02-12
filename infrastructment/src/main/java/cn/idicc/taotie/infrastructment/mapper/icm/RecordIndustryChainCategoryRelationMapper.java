package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainCategoryRelationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lwj
* @description 针对表【industry_chain_category_relation(产业链分类关系表)】的数据库操作Mapper
* @createDate 2024-12-17 10:00:22
* @Entity cn.idicc.pangu.entity.IndustryChainCategoryRelationDO
*/
public interface RecordIndustryChainCategoryRelationMapper extends BaseMapper<RecordIndustryChainCategoryRelationDO> {

    default List<RecordIndustryChainCategoryRelationDO> selectByChainIds(List<Long> chainIds) {
        return this.selectList(Wrappers.lambdaQuery(RecordIndustryChainCategoryRelationDO.class)
                .eq(RecordIndustryChainCategoryRelationDO::getDeleted, false)
                .in(RecordIndustryChainCategoryRelationDO::getChainId, chainIds)
        );
    }

    default List<RecordIndustryChainCategoryRelationDO> selectByCategoryId(Long categoryId){
        return this.selectList(Wrappers.lambdaQuery(RecordIndustryChainCategoryRelationDO.class)
                .eq(RecordIndustryChainCategoryRelationDO::getDeleted, false)
                .eq(RecordIndustryChainCategoryRelationDO::getCategoryId, categoryId));
    }

    Integer physicsBatchDeleteByChainId(@Param("chain_id") Long chainId);
}




