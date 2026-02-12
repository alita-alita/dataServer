/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu;

import cn.idicc.taotie.infrastructment.entity.data.OrgIndustryChainRelationDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 机构产业链关系Service
 *
 * @author wd
 * @version $Id: OrgIndustryChainRelationService.java,v 0.1 2020.10.10 auto Exp $
 */
public interface OrgIndustryChainRelationService extends IService<OrgIndustryChainRelationDO> {

    /**
     * 根据机构id查询关联的产业链数据
     *
     * @param organizeId
     * @return
     */
    List<OrgIndustryChainRelationDO> listByOrgId(Long organizeId);

}
