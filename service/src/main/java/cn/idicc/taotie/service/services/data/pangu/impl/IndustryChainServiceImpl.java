/**
 * qccr.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package cn.idicc.taotie.service.services.data.pangu.impl;


import cn.idicc.taotie.infrastructment.entity.data.IndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.wenchang.KnowledgeLibraryDO;
import cn.idicc.taotie.infrastructment.mapper.data.IndustryChainMapper;
import cn.idicc.taotie.infrastructment.mapper.wenchang.KnowledgeLibraryMapper;
import cn.idicc.taotie.infrastructment.response.data.IndustryChainDTO;
import cn.idicc.taotie.service.services.data.pangu.IndustryChainService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wd
 * @version $Id: IndustryChainServiceImpl.java,v 0.1 2020.10.10 auto Exp $
 * @description 产业链ServiceImpl
 */
@Slf4j
@Service
@RefreshScope
public class IndustryChainServiceImpl extends ServiceImpl<IndustryChainMapper, IndustryChainDO> implements IndustryChainService {

    @Autowired
    private IndustryChainMapper industryChainMapper;
    @Autowired
    private KnowledgeLibraryMapper knowledgeLibraryMapper;

    @Override
    public List<IndustryChainDTO> getAll(boolean isOnline) {
        // 1. 获取所有未删除且已上线的产业链
        List<IndustryChainDO> industryChainDOS = industryChainMapper.selectList(new QueryWrapper<>());
        if (isOnline) {
            // 先批量获取所有上线的知识库信息
            List<KnowledgeLibraryDO> knowledgeLibraries = knowledgeLibraryMapper.selectList(
                    Wrappers.lambdaQuery(KnowledgeLibraryDO.class)
                            .eq(KnowledgeLibraryDO::getState, 1)
            );
            Set<String> knowledgeLibraryDOSet = knowledgeLibraries.stream().map(KnowledgeLibraryDO::getName).collect(Collectors.toSet());
            industryChainDOS = industryChainDOS.stream().filter(item -> knowledgeLibraryDOSet.contains(item.getChainName())).collect(Collectors.toList());
        }
        return industryChainDOS.stream().map(IndustryChainDTO::adapt).collect(Collectors.toList());
    }

    @Override
    public IndustryChainDO getById(Long chainId) {
        IndustryChainDO industryChainDO = industryChainMapper.selectOne(Wrappers.lambdaQuery(IndustryChainDO.class).eq(IndustryChainDO::getId, chainId));
        return industryChainDO;
    }
}
