package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.PersonalInformationDO;
import cn.idicc.taotie.infrastructment.response.data.PersonalInformationDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: wd
 * @Date: 2023/6/13
 * @Description:个人简介
 * @version: 1.0
 */
public interface PersonalInformationService extends IService<PersonalInformationDO> {

    /**
     * 查询个人简介信息
     *
     * @param uniqId
     * @return
     */
    PersonalInformationDTO personalInformation(Integer uniqId);
}
