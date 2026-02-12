package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentAttractionClueFollowUpRecordMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentAttractionClueFollowUpRecordDO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentAttractionClueFollowUpRecordService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: WangZi
 * @Date: 2023/2/24
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class InvestmentAttractionClueFollowUpRecordServiceImpl extends ServiceImpl<InvestmentAttractionClueFollowUpRecordMapper, InvestmentAttractionClueFollowUpRecordDO> implements InvestmentAttractionClueFollowUpRecordService {
    @Autowired
    private InvestmentAttractionClueFollowUpRecordMapper investmentAttractionClueFollowUpRecordMapper;

    @Override
    public Set<Long> getHisVisitPersonIdsByClueId(Long clueId) {
        Set<Long> result = CollectionUtil.newLinkedHashSet();
        List<InvestmentAttractionClueFollowUpRecordDO> list = investmentAttractionClueFollowUpRecordMapper.selectList(Wrappers.lambdaQuery(InvestmentAttractionClueFollowUpRecordDO.class)
                .eq(InvestmentAttractionClueFollowUpRecordDO::getDeleted, Boolean.FALSE)
                .eq(InvestmentAttractionClueFollowUpRecordDO::getClueId, clueId));
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().map(InvestmentAttractionClueFollowUpRecordDO::getFillInPersonId).collect(Collectors.toSet());
        }
        return result;
    }
}
