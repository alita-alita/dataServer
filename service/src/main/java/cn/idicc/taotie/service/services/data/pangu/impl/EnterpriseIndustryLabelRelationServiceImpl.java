/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.mapper.data.EnterpriseIndustryLabelRelationMapper;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseIndustryLabelRelationDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.service.services.data.pangu.EnterpriseIndustryLabelRelationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * 企业产业链标签关系ServiceImpl
 *
 * @author wd
 * @version $Id: EnterpriseLabelRelationServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 */
@Slf4j
@Service
public class EnterpriseIndustryLabelRelationServiceImpl extends ServiceImpl<EnterpriseIndustryLabelRelationMapper, EnterpriseIndustryLabelRelationDO> implements
        EnterpriseIndustryLabelRelationService {

    @Autowired
    private EnterpriseIndustryLabelRelationMapper enterpriseIndustryLabelRelationMapper;

    @Override
    public Map<Long, List<EnterpriseIndustryLabelRelationDO>> mapByEnterpriseIds(List<Long> enterpriseIds) {
        if (CollectionUtil.isEmpty(enterpriseIds)) {
            throw new BizException("传入企业id集合不能为null");
        }

        Map<Long, List<EnterpriseIndustryLabelRelationDO>> result = new ConcurrentHashMap<>();
        List<EnterpriseIndustryLabelRelationDO> list = enterpriseIndustryLabelRelationMapper.selectList(Wrappers.lambdaQuery(new EnterpriseIndustryLabelRelationDO())
                .eq(EnterpriseIndustryLabelRelationDO::getDeleted, Boolean.FALSE)
                .in(EnterpriseIndustryLabelRelationDO::getEnterpriseId, enterpriseIds));
//        log.info("查询结果为,ids:{},{}",enterpriseIds, list.stream().map(EnterpriseIndustryLabelRelationDO::getLabelId).collect(Collectors.toSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().collect(Collectors.groupingBy(EnterpriseIndustryLabelRelationDO::getEnterpriseId));
        }
        return result;
    }
}
