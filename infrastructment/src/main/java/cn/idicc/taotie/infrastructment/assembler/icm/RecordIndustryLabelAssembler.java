package cn.idicc.taotie.infrastructment.assembler.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryLabelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wd
 * @description
 * @date 12/19/22 3:28 PM
 */
@Mapper
public interface RecordIndustryLabelAssembler {

    RecordIndustryLabelAssembler INSTANCE =  Mappers.getMapper(RecordIndustryLabelAssembler.class);

    /**
     * DO转DTO
     * @param industryLabelDO
     * @return
     */
    List<RecordIndustryLabelDTO> ListDOToListDTO(List<RecordIndustryLabelDO> industryLabelDO);
}
