package cn.idicc.taotie.service.services.icm.impl;

import cn.idicc.common.model.BaseDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordBlacklistKeywordsDO;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordAppEnterpriseIndustryChainSuspectedMapper;
import cn.idicc.taotie.infrastructment.mapper.icm.RecordBlacklistKeywordsMapper;
import cn.idicc.taotie.service.services.icm.RecordBlacklistKeywordsService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class RecordBlacklistKeywordsServiceImpl implements RecordBlacklistKeywordsService {
    @Autowired
    private RecordBlacklistKeywordsMapper recordBlacklistKeywordsMapper;
    @Autowired
    private RecordAppEnterpriseIndustryChainSuspectedMapper recordAppEnterpriseIndustryChainSuspectedMapper;

    @Override
    public List<RecordBlacklistKeywordsDO> selectAll(RecordBlacklistKeywordsDO recordBlacklistKeywordsDO) {
        List<RecordBlacklistKeywordsDO> select = recordBlacklistKeywordsMapper.selectAll(recordBlacklistKeywordsDO.getBlacklistIndustrialChain());
        return select;
    }

    @Override
    public Integer addRecordBlacklistKeywords(RecordBlacklistKeywordsDO recordBlacklistKeywordsDO) {
        Integer addRecordBlacklistKeywords = recordBlacklistKeywordsMapper.addRecordBlacklistKeywords(recordBlacklistKeywordsDO);
        if (addRecordBlacklistKeywords > 0){
            Integer updateNegative = recordAppEnterpriseIndustryChainSuspectedMapper.updateNegative(recordBlacklistKeywordsDO.getBlacklistKeywordsName(), recordBlacklistKeywordsDO.getBlacklistIndustrialChain());
            log.info("黑名单企业新增"+updateNegative+"个");
        }
        return addRecordBlacklistKeywords;

    }

    @Override
    public Integer deleteRecordBlacklistKeywords(RecordBlacklistKeywordsDO recordBlacklistKeywordsDO) {
        List<Long> ids = recordBlacklistKeywordsMapper.selectList(Wrappers.lambdaQuery(RecordBlacklistKeywordsDO.class)
                .eq(RecordBlacklistKeywordsDO::getBlacklistIndustrialChain,recordBlacklistKeywordsDO.getBlacklistIndustrialChain())
                .eq(RecordBlacklistKeywordsDO::getBlacklistKeywordsName,recordBlacklistKeywordsDO.getBlacklistKeywordsName())
        ).stream().map(BaseDO::getId).collect(Collectors.toList());
        if (ids.isEmpty()){
            return 0;
        }
        return deleteRecordBlacklistKeywords(ids);
    }

    @Override
    public Integer deleteRecordBlacklistKeywords(List<Long> ids) {
        Integer deleteRecordBlacklistKeywords = recordBlacklistKeywordsMapper.deleteRecordBlacklistKeywords(ids);
        if (deleteRecordBlacklistKeywords <= 0){
            return 0;
        }
        return deleteRecordBlacklistKeywords;
    }
}



