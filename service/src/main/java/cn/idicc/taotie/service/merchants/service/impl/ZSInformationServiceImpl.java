package cn.idicc.taotie.service.merchants.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.InformationDO;
import cn.idicc.taotie.infrastructment.entity.data.MyCollectDataDO;
import cn.idicc.taotie.infrastructment.enums.InfoTypeEnum;
import cn.idicc.taotie.infrastructment.enums.MyCollectDataTypeEnum;
import cn.idicc.taotie.infrastructment.mapper.data.InformationMapper;
import cn.idicc.taotie.infrastructment.po.data.InformationPO;
import cn.idicc.taotie.infrastructment.po.data.UserCollectInfo;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationChainService;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationEnterpriseService;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationThemeService;
import cn.idicc.taotie.service.merchants.service.ZSInformationService;
import cn.idicc.taotie.service.search.InformationSearch;
import cn.idicc.taotie.service.services.data.pangu.MyCollectDataService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/5/31
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service("zSInformationServiceImpl")
public class ZSInformationServiceImpl extends ServiceImpl<InformationMapper, InformationDO> implements ZSInformationService {

    @Autowired
    InformationMapper informationMapper;

    @Autowired
    ZSInformationCorrelationChainService zsInformationCorrelationChainService;

    @Autowired
    ZSInformationCorrelationThemeService zsInformationCorrelationThemeService;

    @Autowired
    ZSInformationCorrelationEnterpriseService zsInformationCorrelationEnterpriseService;

    @Autowired
    MyCollectDataService myCollectDataService;

    @Autowired
    InformationSearch informationSearch;

    @Override
    public InformationDO getByUrl(String url) {
        InformationDO informationDO = informationMapper.selectOne(Wrappers.lambdaQuery(InformationDO.class)
                .eq(InformationDO::getDeleted, Boolean.FALSE)
                .eq(InformationDO::getUrl, url));
        return informationDO;
    }

    @Override
    public void syncToEsById(Long id) {
        InformationDO informationDO = informationMapper.selectById(id);
        Assert.notNull(informationDO, "传入资讯id不存在对应的记录");
        syncInformationToEs(CollectionUtil.newArrayList(informationDO));
    }


    /**
     * 同步资讯信息到资讯es
     *
     * @param informationDOS
     */
    private void syncInformationToEs(List<InformationDO> informationDOS) {
        if (CollectionUtil.isNotEmpty(informationDOS)) {
            List<Long> informationIds = informationDOS.stream().map(e -> e.getId()).collect(Collectors.toList());
            // 查询关联产业
            Map<Long, List<Long>> informationChainIdsMap = zsInformationCorrelationChainService.queryByInformationIds(informationIds);
            // 查询关联企业
            Map<Long, List<String>> informationEnterprisesMap = zsInformationCorrelationEnterpriseService.queryByInformationIds(informationIds);
            // 查询关联主题
            Map<Long, List<String>> informationThemesMap = zsInformationCorrelationThemeService.queryByInformationIds(informationIds);
            // 查询关注资讯
            List<MyCollectDataDO> myCollectDataDOS = myCollectDataService.listByEnterpriseIds(informationIds, MyCollectDataTypeEnum.COLLECT_INFORMATION.getCode());
            Map<Long, List<MyCollectDataDO>> myCollectDataMap = myCollectDataDOS.stream().collect(Collectors.groupingBy(e -> e.getBusId()));
            // 开始处理数据
            List<InformationPO> informationPOS = Lists.newArrayList();
            informationDOS.forEach(e -> {
                InformationPO informationPO = BeanUtil.copyProperties(e, InformationPO.class);
                // 设置资讯类型
                List<Integer> infoTypes = Lists.newArrayList();
                if (e.getIsIndustry()) {
                    infoTypes.add(InfoTypeEnum.INDUSTRY_INFORMATION.getCode());
                }
                if (e.getIsEnterprise()) {
                    infoTypes.add(InfoTypeEnum.ENTERPRISE_INFORMATION.getCode());
                }
                if (e.getIsInvestment()) {
                    infoTypes.add(InfoTypeEnum.INVESTMENT_INFORMATION.getCode());
                }
                if (e.getIsChange()) {
                    infoTypes.add(InfoTypeEnum.POTENTIAL_OPPORTUNITY.getCode());
                }
                informationPO.setInfoType(infoTypes);
                // 设置关联产业
                List<Long> chainIds = informationChainIdsMap.get(e.getId());
                if (CollectionUtil.isNotEmpty(chainIds)) {
                    informationPO.setIndustryIds(chainIds);
                }
                // 设置关联企业
                List<String> unifiedSocialCreditCodes = informationEnterprisesMap.get(e.getId());
                if (CollectionUtil.isNotEmpty(unifiedSocialCreditCodes)) {
                    informationPO.setUnifiedSocialCreditCodes(unifiedSocialCreditCodes);
                }
                // 设置关联主题
                List<String> themes = informationThemesMap.get(e.getId());
                if (CollectionUtil.isNotEmpty(themes)) {
                    informationPO.setThemes(themes);
                }
                // 设置关注人
                if (MapUtils.isNotEmpty(myCollectDataMap)) {
                    List<MyCollectDataDO> myCollectDataDOList = myCollectDataMap.get(e.getId());
                    if (CollectionUtil.isNotEmpty(myCollectDataDOList)) {
                        List<UserCollectInfo> userCollectInfo = Lists.newArrayList();
                        myCollectDataDOList.forEach(t -> {
                            Long collDate = t.getGmtCreate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                            userCollectInfo.add(new UserCollectInfo(t.getUserId(), collDate));
                        });
                        informationPO.setUserCollectInfo(userCollectInfo);
                    }
                }
                informationPOS.add(informationPO);
            });
            // 保存数据
            if (CollectionUtil.isNotEmpty(informationPOS)) {
                informationSearch.saveAll(informationPOS);
            }
        }
    }
}
