package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainCategoryDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainCategoryDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lwj
* @description 针对表【industry_chain_category(产业链分类表)】的数据库操作Service
* @createDate 2024-12-17 10:00:10
*/
public interface RecordIndustryChainCategoryService extends IService<RecordIndustryChainCategoryDO> {

    List<RecordIndustryChainCategoryDTO> getAllList(String name);
}
