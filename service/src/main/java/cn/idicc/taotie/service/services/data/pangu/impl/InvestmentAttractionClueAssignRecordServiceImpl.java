package cn.idicc.taotie.service.services.data.pangu.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.idicc.taotie.infrastructment.mapper.data.InvestmentAttractionClueAssignRecordMapper;
import cn.idicc.taotie.infrastructment.entity.data.InvestmentAttractionClueAssignRecordDO;
import cn.idicc.taotie.service.services.data.pangu.InvestmentAttractionClueAssignRecordService;
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
 * @Date: 2023/5/12
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Service
public class InvestmentAttractionClueAssignRecordServiceImpl extends ServiceImpl<InvestmentAttractionClueAssignRecordMapper, InvestmentAttractionClueAssignRecordDO> implements InvestmentAttractionClueAssignRecordService {

    @Autowired
    private InvestmentAttractionClueAssignRecordMapper investmentAttractionClueAssignRecordMapper;

    @Override
    public Set<Long> getHisAssignPersonIds(Long clueId) {
        Set<Long> result = CollectionUtil.newHashSet();
        List<InvestmentAttractionClueAssignRecordDO> list = listByClueId(clueId);
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().map(InvestmentAttractionClueAssignRecordDO::getOperateUserId).collect(Collectors.toSet());
        }
        return result;
    }

    @Override
    public Set<Long> getHisBeAssignPersonIds(Long clueId) {
        Set<Long> result = CollectionUtil.newHashSet();
        List<InvestmentAttractionClueAssignRecordDO> list = listByClueId(clueId);
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().map(InvestmentAttractionClueAssignRecordDO::getAssignUserId).collect(Collectors.toSet());
        }
        return result;
    }

    @Override
    public List<InvestmentAttractionClueAssignRecordDO> listByClueId(Long clueId) {
        List<InvestmentAttractionClueAssignRecordDO> result = CollectionUtil.newArrayList();
        List<InvestmentAttractionClueAssignRecordDO> list = investmentAttractionClueAssignRecordMapper.selectList(Wrappers.lambdaQuery(InvestmentAttractionClueAssignRecordDO.class)
                .eq(InvestmentAttractionClueAssignRecordDO::getDeleted, Boolean.FALSE)
                .eq(InvestmentAttractionClueAssignRecordDO::getClueId, clueId));
        if (CollectionUtil.isNotEmpty(list)) {
            result = list;
        }
        return result;
    }
}
