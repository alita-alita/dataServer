package cn.idicc.taotie.infrastructment.assembler.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.response.icm.RecordIndustryChainNodeDTO;
import cn.idicc.taotie.infrastructment.response.icm.OrgRecordIndustryChainNodeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wd
 * @description
 * @date 12/19/22 3:28 PM
 */
@Mapper
public interface RecordIndustryChainNodeAssembler {

    RecordIndustryChainNodeAssembler INSTANCE =  Mappers.getMapper(RecordIndustryChainNodeAssembler.class);

    /**
     * DOs转DTOs
     * @param chainNodeDO
     * @return
     */
    List<OrgRecordIndustryChainNodeDTO> OrgDOsToOrgDTOs(List<RecordIndustryChainNodeDO> chainNodeDO);

    /**
     * DO转DTO
     * @param chainNodeDO
     * @return
     */
    RecordIndustryChainNodeDTO DOToDTO(RecordIndustryChainNodeDO chainNodeDO);

}
