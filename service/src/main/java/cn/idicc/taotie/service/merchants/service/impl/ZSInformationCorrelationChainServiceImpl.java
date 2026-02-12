package cn.idicc.taotie.service.merchants.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.entity.data.InformationCorrelationChainDO;
import cn.idicc.taotie.infrastructment.mapper.data.InformationCorrelationChainMapper;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationChainService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service("zSInformationCorrelationChainServiceImpl")
public class ZSInformationCorrelationChainServiceImpl extends ServiceImpl<InformationCorrelationChainMapper, InformationCorrelationChainDO> implements ZSInformationCorrelationChainService {

    @Autowired
    private InformationCorrelationChainMapper informationCorrelationChainMapper;

    @Override
    public InformationCorrelationChainDO getByInformationIdAndChainId(Long informationId, Long chainId) {
        return informationCorrelationChainMapper.selectOne(Wrappers.lambdaQuery(InformationCorrelationChainDO.class)
                .eq(InformationCorrelationChainDO::getDeleted, Boolean.FALSE)
                .eq(InformationCorrelationChainDO::getInformationId, informationId)
                .eq(InformationCorrelationChainDO::getChainId, chainId));
    }

    @Override
    public Map<Long, List<Long>> queryByInformationIds(List<Long> informationIds) {
        List<InformationCorrelationChainDO> informationCorrelationChainDOS = informationCorrelationChainMapper.selectList(Wrappers.lambdaQuery(InformationCorrelationChainDO.class)
                .in(InformationCorrelationChainDO::getInformationId, informationIds));
        Map<Long, List<Long>> map = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(informationCorrelationChainDOS)) {
            map.putAll(informationCorrelationChainDOS.stream().collect(Collectors.groupingBy(e -> e.getInformationId(), Collectors.mapping(v -> v.getChainId(), Collectors.toList()))));
        }
        return map;
    }

}
