package cn.idicc.taotie.infrastructment.assembler.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author wd
 * @description
 * @date 12/19/22 3:28 PM
 */
@Mapper
public interface RecordIndustryChainAssembler {

    RecordIndustryChainAssembler INSTANCE =  Mappers.getMapper(RecordIndustryChainAssembler.class);



    /**
     * DO转DTO
     * @param industryChainDOPage
     * @return
     */
    Page<RecordIndustryChainDTO> DOToDTO(Page<RecordIndustryChainDO> industryChainDOPage);

}
