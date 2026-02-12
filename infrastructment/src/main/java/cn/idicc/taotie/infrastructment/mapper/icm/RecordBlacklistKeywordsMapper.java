package cn.idicc.taotie.infrastructment.mapper.icm;

import cn.idicc.taotie.infrastructment.entity.icm.RecordAppTypicalEnterpriseDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordBlacklistKeywordsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecordBlacklistKeywordsMapper extends BaseMapper<RecordBlacklistKeywordsDO> {
    /**
     *  查询所有与其产业链相匹配的黑名单关键词
     * @param blacklistIndustrialChain
     * @return
     */
    List<RecordBlacklistKeywordsDO> selectAll(@Param("blacklistIndustrialChain") Integer blacklistIndustrialChain);

    /**
     * 添加黑名单关键词
     * @param recordBlacklistKeywordsDO
     * @return
     */
    Integer addRecordBlacklistKeywords(@Param("do") RecordBlacklistKeywordsDO recordBlacklistKeywordsDO);

    /**
     * 删除黑名单关键词(逻辑删)
     * @param ids
     * @return
     */
    Integer deleteRecordBlacklistKeywords(@Param("ids") List<Long> ids);

}
