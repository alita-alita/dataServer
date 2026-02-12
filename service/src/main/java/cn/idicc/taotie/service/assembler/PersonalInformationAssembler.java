package cn.idicc.taotie.service.assembler;

import cn.idicc.taotie.infrastructment.entity.data.PersonalInformationDO;
import cn.idicc.taotie.infrastructment.response.data.PersonalInformationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wd
 * @description 个人简介
 * @date 12/19/22 3:28 PM
 */
@Mapper
public interface PersonalInformationAssembler {

    PersonalInformationAssembler INSTANCE =  Mappers.getMapper(PersonalInformationAssembler.class);

    PersonalInformationDTO convert(PersonalInformationDO personalInformationDO);

    List<PersonalInformationDTO> convert(List<PersonalInformationDO> list);

}
