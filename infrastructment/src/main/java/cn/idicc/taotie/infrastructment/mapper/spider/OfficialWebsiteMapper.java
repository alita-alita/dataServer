package cn.idicc.taotie.infrastructment.mapper.spider;

import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.entity.spider.KeywordKafka;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsite;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsiteKafka;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OfficialWebsiteMapper {
    /**
     * 根据 企业名称 企业官网采集状态 查找关键词
     * 官网 展示列表
     */
    List<OfficialWebsite> listOfficialWebsite(@Param("officialWebsiteName") String officialWebsiteName,
                                              @Param("officialWebsiteState") Integer officialWebsiteState);


    //采集  按照企业官网主键进行采集
    List<OfficialWebsiteKafka> manualGather(@Param("officialWebsiteId") List<Long> officialWebsiteId);


    List<OfficialWebsite> findByCollectTime(String officialWebsiteNextTime);
    /**
     * 添加企业官网
     */
    Integer addOfficialWebsite(OfficialWebsite officialWebsite);

    /**
     * 批量添加文件传入的数据
     */
    Integer addOfficialWebsiteBatch(List<OfficialWebsite> officialWebsiteDataList);


    /**
     * 修改企业官网
     */
    Integer updateOfficialWebsite(OfficialWebsite officialWebsite);

    /**
     * 修改企业官网采集更新时间
     */
    Integer updateOfficialWebsiteTime(@Param("officialWebsiteId")Integer officialWebsiteId);



    /**
     * 逻辑删除企业官网
     */
    Integer delLogicOfficialWebsite(Integer officialWebsiteId);


}
