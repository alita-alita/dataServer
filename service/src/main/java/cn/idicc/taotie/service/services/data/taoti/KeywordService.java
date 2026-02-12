package cn.idicc.taotie.service.services.data.taoti;


import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.entity.spider.KeywordKafka;
import cn.idicc.taotie.infrastructment.request.data.KeywordReq;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface KeywordService  extends IService<Keyword> {

    /**
     * 根据 关键词名称 采集状态 查找关键词
     * 关键词展示列表
     */
    PageInfo<Keyword> listKeyword(KeywordReq keywordReq);

    //手动采集  按照关键字主键进行采集
    List<KeywordKafka> manualGather(String keywordTaskCode, List<Integer> keywordIds,Integer distinguish);

    //自动采集 维护关键词
    List<KeywordKafka> maintenanceAutomatic(List<Long> keywordIds);

    /**
     * 添加关键词
     */
    Integer addKeyword(Keyword keyword);
    /**
     * 批量添加文件传入的数据
     */
    Integer addKeywordBatch(List<Keyword> keywordDataList);


    /**
     * 修改关键字
     */
    Integer updateKeyword(Keyword keyword);
    Integer updateKeywordOF(Keyword keyword);


    /**
     * 删除关键字(物理删除)
     */
    Integer deleteKeyword(Keyword keyword);

    /**
     * 逻辑删除关键字
     */
    Integer delLogicKeyword(Keyword keyword);


}
