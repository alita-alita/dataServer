/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.idicc.taotie.infrastructment.dao.data.OrgIndustryChainRelationDao;
import cn.idicc.taotie.infrastructment.mapper.data.OrgIndustryChainRelationMapper;
import cn.idicc.taotie.infrastructment.entity.data.OrgIndustryChainRelationDO;
import cn.idicc.taotie.service.services.data.pangu.OrgIndustryChainRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 机构产业链关系ServiceImpl
 *
 * @author wd
 * @version $Id: OrgIndustryChainRelationServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 */
@Service
@Slf4j
@RefreshScope
public class OrgIndustryChainRelationServiceImpl extends ServiceImpl<OrgIndustryChainRelationMapper, OrgIndustryChainRelationDO> implements
        OrgIndustryChainRelationService {

    @Autowired
    OrgIndustryChainRelationDao orgIndustryChainRelationDao;

    @Override
    public List<OrgIndustryChainRelationDO> listByOrgId(Long organizeId) {
        return orgIndustryChainRelationDao.listByOrgId(organizeId);
    }

}
