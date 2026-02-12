package cn.idicc.taotie.service.services.data.taoti.Impl;

import cn.idicc.taotie.infrastructment.entity.spider.KeywordKafka;
import cn.idicc.taotie.infrastructment.mapper.spider.KeywordMapper;
import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.request.data.KeywordReq;
import cn.idicc.taotie.service.services.data.taoti.KeywordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
public class KeywordServiceImpl extends ServiceImpl< KeywordMapper, Keyword> implements KeywordService {

    private static final Integer FAIL_CODE = 0;

    @Autowired
    private KeywordMapper keywordMapper;

    @Override
    public PageInfo<Keyword> listKeyword(KeywordReq keywordReq) {
        PageHelper.startPage(keywordReq.getPageNum(),keywordReq.getPageSize());
        List<Keyword> keywords = keywordMapper.listKeyword(keywordReq.keywordName,keywordReq.keywordMode,
                                                           keywordReq.keywordTaskCode, keywordReq.keywordState,
                                                           keywordReq.distinguish,keywordReq.keywordNewsSource );
        PageInfo<Keyword> listPageInfo = new PageInfo<>(keywords);
        return listPageInfo;
    }

    @Override
    public List<KeywordKafka> manualGather(String keywordTaskCode, List<Integer> keywordIds,Integer distinguish) {
        return keywordMapper.manualGather(keywordTaskCode,keywordIds,distinguish);
    }
    @Override
    public List<KeywordKafka> maintenanceAutomatic(List<Long> keywordIds) {
        return keywordMapper.maintenanceAutomatic(keywordIds);
    }

    /**
     * @param keyword 要添加的关键词对象
     * @return 返回添加成功后的记录主键值，添加失败返回0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addKeyword(Keyword keyword) {
        if (keyword == null){
            log.error("参数为空，无法添加关键词");
            return FAIL_CODE;
        }
        try {
            Integer addedKeyword = keywordMapper.addKeyword(keyword);
            if (addedKeyword.equals(FAIL_CODE)) {
                log.error("添加关键词失败，请检查参数是否正确");
                return FAIL_CODE;
            }
            return addedKeyword;
        }  catch (Exception e) {
            e.printStackTrace();
            log.error("添加关键词失败，请检查参数是否正确");
            return FAIL_CODE;
        }

    }

    @Override
    public Integer addKeywordBatch(List<Keyword> keywordDataList) {
        //批量添加方法
        return  keywordMapper.addKeywordBatch(keywordDataList);

    }


    @Override
    public Integer updateKeyword(Keyword keyword) {
        if (keyword == null) {
            return 0;
        }
        try {
            Integer updateKeyword = keywordMapper.updateKeyword(Math.toIntExact(keyword.getKeywordId()),keyword.getKeywordName(),
                                                                                keyword.getKeywordMode(),keyword.getKeywordState(),
                                                                                keyword.getKeywordCycle()        );
            if (updateKeyword < 0) {
                return 0;
            }
            return updateKeyword;
        }  catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer updateKeywordOF(Keyword keyword) {
        if (keyword == null) {
            return 0;
        }
        try {
            Integer updateKeyword = keywordMapper.updateKeywordOF(Math.toIntExact(keyword.getKeywordId()),
                    keyword.getKeywordPublicAccountUrl(),keyword.getKeywordNewsSource(),keyword.getKeywordSearchWord());
            if (updateKeyword < 0) {
                return 0;
            }
            return updateKeyword;
        }  catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public Integer deleteKeyword(Keyword keyword) {
        if (keyword.getKeywordId()== null) {
            return 0;
        }
        try {
        Integer deleteKeyword = keywordMapper.deleteKeyword(keyword);
        if(deleteKeyword < 0){
            return 0;
        }
        return  deleteKeyword;
        }  catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer delLogicKeyword(Keyword keyword) {

        if (keyword.getKeywordId() == null) {
            return 0;
        }
        try {
        Integer delLogicKeyword = keywordMapper.delLogicKeyword(keyword);
        if(delLogicKeyword < 0){
            return 0;
        }
        return  delLogicKeyword;
        }  catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
