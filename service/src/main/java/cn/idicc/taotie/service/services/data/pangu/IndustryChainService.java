/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.IndustryChainDO;
import cn.idicc.taotie.infrastructment.response.data.IndustryChainDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author wd
 * @version $Id: IndustryChainService.java,v 0.1 2020.10.10 auto Exp $
 * @description 产业链Service
 */
public interface IndustryChainService extends IService<IndustryChainDO> {

    List<IndustryChainDTO> getAll(boolean isOnline);

    IndustryChainDO getById(Long ChainId);
}
