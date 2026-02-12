/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.mapper.data.IndustryChainNodeLabelRelationMapper;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeLabelRelationDO;
import cn.idicc.taotie.infrastructment.exception.BizException;
import cn.idicc.taotie.infrastructment.mapper.icm.ChainNodeRefAtomNodeMapper;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainNodeLabelRelationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 标签节点与产业链标签关联关系ServiceImpl
 *
 * @author wd
 * @version $Id: IndustryChainNodeLabelRelationServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 */
@Service
public class IndustryChainNodeLabelRelationServiceImpl extends ServiceImpl<IndustryChainNodeLabelRelationMapper, IndustryChainNodeLabelRelationDO> implements
        IndustryChainNodeLabelRelationService {
    @Autowired
    IndustryChainNodeLabelRelationMapper industryChainNodeLabelRelationMapper;



    @Override
    public List<IndustryChainNodeLabelRelationDO> listByLabelIds(List<Long> labelIds) {
        List<IndustryChainNodeLabelRelationDO> result;
        if (CollectionUtil.isEmpty(labelIds)) {
            throw new BizException("传入产业链id集合不能为null");
        }
        result = industryChainNodeLabelRelationMapper.selectList(Wrappers.lambdaQuery(new IndustryChainNodeLabelRelationDO())
                .eq(IndustryChainNodeLabelRelationDO::getDeleted, Boolean.FALSE)
                .in(IndustryChainNodeLabelRelationDO::getIndustryLabelId, labelIds));
        return result;
    }

    @Override
    public List<String> getLabelByNode(Long nodeId) {
        List<String> productNames = industryChainNodeLabelRelationMapper.getLabelNameByNodeId(nodeId);
        if (CollectionUtil.isNotEmpty(productNames)){
            return productNames;
        }
        return Collections.emptyList();
    }

}
