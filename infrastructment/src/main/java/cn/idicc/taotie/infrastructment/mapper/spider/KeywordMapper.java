package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.entity.spider.KeywordKafka;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KeywordMapper extends BaseMapper<Keyword> {

    /**
     * 根据 关键词名称 采集模式 关键词采集状态 查找关键词
     * 关键词展示列表
     * 关键词采集类型做区分
     */
    List<Keyword> listKeyword(@Param("keywordName") String keywordName, @Param("keywordMode") String keywordMode ,
                              @Param("keywordTaskCode") String keywordTaskCode, @Param("keywordState") Integer keywordState,
                              @Param("distinguish") Integer distinguish,@Param("keywordNewsSource")String keywordNewsSource);


    //手动采集  按照关键字主键进行采集
    List<KeywordKafka> manualGather(@Param("keywordTaskCode") String keywordTaskCode,
                                    @Param("keywordIds") List<Integer> keywordIds ,
                                    @Param("distinguish")Integer distinguish);
    //自动采集 维护关键词
    List<KeywordKafka> maintenanceAutomatic(@Param("keywordIds") List<Long> keywordIds);

    /**
     * 关键词搜索词唯一校验
     */
    boolean isKeywordExists(String keyword,String platform);

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
    Integer updateKeyword(@Param("keywordId")Integer keywordId,@Param("keywordName") String keywordName ,
                          @Param("keywordMode") String keywordMode, @Param("keywordState") Integer keywordState,
                          @Param("keywordCycle")Integer keywordCycle);

    /**
     * 修改关键字
     */
    Integer updateKeywordOF(@Param("keywordId")Integer keywordId,
                            @Param("keywordPublicAccountUrl")String keywordPublicAccountUrl,
                            @Param("keywordNewsSource") String keywordNewsSource ,
                            @Param("keywordName") String keywordName);


    /**
     * 修改关键字采集更新时间
     */
    Integer updateKeywordTime(@Param("keywordId")Integer keywordId);


    /**
     * 逻辑删除关键字
     */
    Integer delLogicKeyword(Keyword keyword);
    /**
     * 删除关键字(物理删除)
     */
    Integer deleteKeyword(Keyword keyword);


    List<Keyword> findByCollectTime(String keywordNextTime);
}
