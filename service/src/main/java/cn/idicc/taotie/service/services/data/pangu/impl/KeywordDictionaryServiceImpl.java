package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.mapper.data.KeywordDictionaryMapper;
import cn.idicc.taotie.infrastructment.entity.data.KeywordDictionaryDO;
import cn.idicc.taotie.infrastructment.response.data.KeywordDictionaryDTO;
import cn.idicc.taotie.service.services.data.pangu.KeywordDictionaryService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/3/21
 * @Description: 关键字词典实现层
 * @version: 1.0
 */
@Slf4j
@Service
public class KeywordDictionaryServiceImpl extends ServiceImpl<KeywordDictionaryMapper, KeywordDictionaryDO> implements KeywordDictionaryService {

    @Autowired
    private KeywordDictionaryMapper keywordDictionaryMapper;

    @Override
    public List<KeywordDictionaryDTO> listByIds(List<Long> ids) {
        List<KeywordDictionaryDTO> result = CollectionUtil.newArrayList();
        List<KeywordDictionaryDO> keywordDictionaryDOS = keywordDictionaryMapper.selectList(Wrappers.lambdaQuery(new KeywordDictionaryDO())
                .eq(KeywordDictionaryDO::getDeleted, Boolean.FALSE)
                .in(KeywordDictionaryDO::getId, ids));
        if (CollectionUtil.isNotEmpty(keywordDictionaryDOS)) {
            result = keywordDictionaryDOS.stream().map(KeywordDictionaryDTO::adapt).collect(Collectors.toList());
        }
        return result;
    }
}
