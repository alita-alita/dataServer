package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.taotie.infrastructment.entity.data.PersonalInformationDO;
import cn.idicc.taotie.infrastructment.mapper.data.PersonalInformationMapper;
import cn.idicc.taotie.infrastructment.response.data.PersonalInformationDTO;
import cn.idicc.taotie.service.assembler.PersonalInformationAssembler;
import cn.idicc.taotie.service.services.data.pangu.PersonalInformationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: wd
 * @Date: 2023/6/13
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class PersonalInformationServiceImpl extends ServiceImpl<PersonalInformationMapper, PersonalInformationDO> implements PersonalInformationService {

    @Override
    public PersonalInformationDTO personalInformation(Integer uniqId) {
        PersonalInformationDO personalInformationDO = this.getOne(Wrappers.lambdaQuery(PersonalInformationDO.class).eq(PersonalInformationDO::getUniqId, uniqId));
        return PersonalInformationAssembler.INSTANCE.convert(personalInformationDO);
    }
}
