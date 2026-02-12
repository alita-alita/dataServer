package cn.idicc.taotie.service.merchants.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.entity.data.InformationCorrelationThemeDO;
import cn.idicc.taotie.infrastructment.mapper.data.InformationCorrelationThemeMapper;
import cn.idicc.taotie.infrastructment.response.data.KeywordDictionaryDTO;
import cn.idicc.taotie.service.merchants.service.ZSInformationCorrelationThemeService;
import cn.idicc.taotie.service.services.data.pangu.KeywordDictionaryService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service("zSInformationCorrelationThemeServiceImpl")
public class ZSInformationCorrelationThemeServiceImpl extends ServiceImpl<InformationCorrelationThemeMapper, InformationCorrelationThemeDO> implements ZSInformationCorrelationThemeService {

    @Autowired
    private InformationCorrelationThemeMapper informationCorrelationThemeMapper;

    @Autowired
    KeywordDictionaryService keywordDictionaryService;


    @Override
    public Map<Long, List<String>> queryByInformationIds(List<Long> informationIds) {
        List<InformationCorrelationThemeDO> informationCorrelationThemeDOS = informationCorrelationThemeMapper.selectList(Wrappers.lambdaQuery(InformationCorrelationThemeDO.class)
                .in(InformationCorrelationThemeDO::getInformationId,informationIds));
        Map<Long,List<String>> resultMap = Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(informationCorrelationThemeDOS)){
            Map<Long,List<InformationCorrelationThemeDO>> map = informationCorrelationThemeDOS.stream().collect(Collectors.groupingBy(e->e.getInformationId()));
            // 查询新闻主题
            Set<Long> keywordIds = informationCorrelationThemeDOS.stream().map(e->e.getKeywordId()).collect(Collectors.toSet());
            List<KeywordDictionaryDTO> keywordDictionaryDTOS = keywordDictionaryService.listByIds(Lists.newArrayList(keywordIds));
            Map<Long,String> keywordDictionaryMap = keywordDictionaryDTOS.stream().collect(Collectors.toMap(e->e.getId(),v->v.getName()));
            map.keySet().forEach(e->{
                List<String> themes = Lists.newArrayList();
                map.get(e).forEach(g->themes.add(keywordDictionaryMap.get(g.getKeywordId())));
                resultMap.put(e,themes);
            });
        }
        return resultMap;
    }
}
