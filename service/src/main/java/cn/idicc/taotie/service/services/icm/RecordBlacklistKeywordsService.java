package cn.idicc.taotie.service.services.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordBlacklistKeywordsDO;

import java.util.List;

public interface RecordBlacklistKeywordsService {

    /**
     *  查询所有与其产业链相匹配的黑名单关键词
     * @param recordBlacklistKeywordsDO
     * @return
     */
    List<RecordBlacklistKeywordsDO> selectAll(RecordBlacklistKeywordsDO recordBlacklistKeywordsDO);

    /**
     * 添加黑名单关键词
     * @param recordBlacklistKeywordsDO
     * @return
     */
    Integer addRecordBlacklistKeywords(RecordBlacklistKeywordsDO recordBlacklistKeywordsDO);
    Integer deleteRecordBlacklistKeywords(RecordBlacklistKeywordsDO recordBlacklistKeywordsDO);

    /**
     * 删除黑名单关键词(逻辑删)
     * @param ids
     * @return
     */
    Integer deleteRecordBlacklistKeywords(List<Long> ids);









}
