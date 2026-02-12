package cn.idicc.taotie.service.services.data.taoti;

import cn.idicc.taotie.infrastructment.entity.spider.Keyword;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsite;
import cn.idicc.taotie.infrastructment.entity.spider.OfficialWebsiteKafka;
import cn.idicc.taotie.infrastructment.request.data.OfficialWebsiteReq;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OfficialWebsiteService {
    /**
     * 根据 企业名称 企业官网采集状态 查找关键词
     * 官网 展示列表
     */
    PageInfo<OfficialWebsite> listOfficialWebsite(OfficialWebsiteReq officialWebsiteReq);


    //采集  按照企业官网主键进行采集
    List<OfficialWebsiteKafka> manualGather(List<Long> officialWebsiteId);


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
    Integer updateOfficialWebsiteTime(Integer officialWebsiteId);



    /**
     * 逻辑删除企业官网
     */
    Integer delLogicOfficialWebsite(OfficialWebsite officialWebsite);



}
