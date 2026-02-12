package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainCategoryDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.List;

/**
* @author lwj
* @description 针对表【industry_chain_category(产业链分类表)】的数据库操作Mapper
* @createDate 2024-12-17 10:00:10
* @Entity cn.idicc.pangu.entity.IndustryChainCategoryDO
*/
public interface RecordIndustryChainCategoryMapper extends BaseMapper<RecordIndustryChainCategoryDO> {

    default List<RecordIndustryChainCategoryDO> selectList(String name){
       if(!StringUtils.isBlank(name)) {
           return this.selectList(Wrappers.lambdaQuery(RecordIndustryChainCategoryDO.class)
                   .eq(RecordIndustryChainCategoryDO::getDeleted, false)
                   .like(RecordIndustryChainCategoryDO::getCategoryName, "%" + name + "%"));
       }else{
           return this.selectList(Wrappers.lambdaQuery(RecordIndustryChainCategoryDO.class)
                   .eq(RecordIndustryChainCategoryDO::getDeleted, false));
       }
    }
}




