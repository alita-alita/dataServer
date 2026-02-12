package cn.idicc.taotie.service.services.icm.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainCategoryDO;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordIndustryChainCategoryMapper;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainCategoryDTO;
import cn.idicc.taotie.service.services.icm.RecordIndustryChainCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author lwj
* @description 针对表【industry_chain_category(产业链分类表)】的数据库操作Service实现
* @createDate 2024-12-17 10:00:10
*/
@Service
public class RecordIndustryChainCategoryServiceImpl extends ServiceImpl<RecordIndustryChainCategoryMapper, RecordIndustryChainCategoryDO>
    implements RecordIndustryChainCategoryService {

    @Autowired
    RecordIndustryChainCategoryMapper industryChainCategoryMapper;

    @Override
    public List<RecordIndustryChainCategoryDTO> getAllList(String name) {
        List<RecordIndustryChainCategoryDO> tempList = industryChainCategoryMapper.selectList(name);
        List<RecordIndustryChainCategoryDTO> voList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tempList)){
            for(RecordIndustryChainCategoryDO industryChainCategoryDO : tempList){
                RecordIndustryChainCategoryDTO dto =  BeanUtil.copyProperties(industryChainCategoryDO, RecordIndustryChainCategoryDTO.class);
                if(industryChainCategoryDO.getParentId() > 0) {
                    dto.setCategoryName(industryChainCategoryDO.getParentName() + "-" + industryChainCategoryDO.getCategoryName());
                }
                voList.add(dto);
            }
        }

        return voList;
    }
}




