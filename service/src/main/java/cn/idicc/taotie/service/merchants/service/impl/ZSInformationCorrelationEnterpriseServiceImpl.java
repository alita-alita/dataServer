package cn.idicc.taotie.service.merchants.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.entity.data.InformationCorrelationEnterpriseDO;
import cn.idicc.taotie.infrastructment.mapper.data.InformationCorrelationEnterpriseMapper;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationEnterpriseService;
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
@Service("zsInformationCorrelationEnterpriseServiceImpl")
public class ZSInformationCorrelationEnterpriseServiceImpl extends ServiceImpl<InformationCorrelationEnterpriseMapper, InformationCorrelationEnterpriseDO> implements ZSInformationCorrelationEnterpriseService {

    @Autowired
    private InformationCorrelationEnterpriseMapper informationCorrelationEnterpriseMapper;


    @Override
    public InformationCorrelationEnterpriseDO getByInformationIdAndUnifiedSocialCreditCode(Long informationId, String unifiedSocialCreditCode) {
        return informationCorrelationEnterpriseMapper.selectOne(Wrappers.lambdaQuery(InformationCorrelationEnterpriseDO.class)
                .eq(InformationCorrelationEnterpriseDO::getDeleted, Boolean.FALSE)
                .eq(InformationCorrelationEnterpriseDO::getInformationId, informationId)
                .eq(InformationCorrelationEnterpriseDO::getUnifiedSocialCreditCode, unifiedSocialCreditCode));
    }

    @Override
    public Map<Long, List<String>> queryByInformationIds(List<Long> informationIds) {
        List<InformationCorrelationEnterpriseDO> informationCorrelationChainDOS = informationCorrelationEnterpriseMapper.selectList(Wrappers.lambdaQuery(InformationCorrelationEnterpriseDO.class)
                .in(InformationCorrelationEnterpriseDO::getInformationId, informationIds));
        Map<Long, List<String>> map = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(informationCorrelationChainDOS)) {
            map.putAll(informationCorrelationChainDOS.stream().collect(Collectors.groupingBy(e -> e.getInformationId(), Collectors.mapping(v -> v.getUnifiedSocialCreditCode(), Collectors.toList()))));
        }
        return map;
    }
}
