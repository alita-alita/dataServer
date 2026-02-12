package cn.idicc.taotie.infrastructment.response.data;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import cn.idicc.taotie.infrastructment.utils.ExcelImport;

import lombok.Data;

/**
 * Excel 采用数据 实体类
 */
@Data
public class ExcelAdoptDTO {

    /**
     * 关键字主键id
     */
    private Long uniKey;

    /**
     * 关键字名称
     */
    @ExcelImport(value = "keyword")
    private String keyword;

    /**  采集类型
     （关键词采集：
     人才招商 news_talent   资本招商 news_capital   产品舆情 news_product   企业舆情 news_enterprise
     公众号采集：
     企业舆情 news_enterprise   亲缘舆情 news_qinshang   产业舆情  news_industry)
     */
    @ExcelImport(value = "taskCode")
    private String taskCode;

    /**
     * 企业名称
     */
    @ExcelImport(value = "searchWord")
    private String searchWord;

    /**
     *关键词采平台
     */
    @ExcelImport(value = "platform")
    private String platform;

    /**
     * 关键词采集模式 (all 全量采集  incre 增量采集 )
     */
    @ExcelImport(value = "collectType")
    private String collectType;

    /**
     * 公众号唯一CODE/官网地址
     */
    @ExcelImport(value = "publicAccountUrl")
    private String publicAccountUrl;

    /**
     * 企业公众号名称
     */
    @ExcelImport(value = "newsSource" )
    private String newsSource;

    /**
     * 产业链名称
     */
    @ExcelImport(value = "industryName")
    private String searchedIndustryName;

    /**
     * 企业统一社会信用代码
     */
    @ExcelImport(value = "uniCode")
    private String uniCode;

    /**
     * 采集周期
     */
    @ExcelImport(value = "cycle" )
    private Integer cycle;

    /**
     * 官网-企业ID
     */
    @ExcelImport(value = "enterpriseId" )
    private String enterpriseId;

    /**
     * 官网-企业名称
     */
    @ExcelImport(value = "enterpriseName" )
    private String enterpriseName;
}
