package cn.idicc.taotie.infrastructment.request.data;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import lombok.Data;

/**
 * 【企业舆情-微信公众号采集】导出模板
 */
@Data
public class ExcelExportEnterpriseOF {

    /**
     * 任务编号
     */
    @ExcelExport(value = "uniKey", example = "例:(使用模板时请将本行示例数据删除)")
    private String uniKey;
    /**
     * 固定值
     */
    @ExcelExport(value = "taskCode", example = "news_enterprise")
    private String taskCode;

    /**
     * 平台，固定值：微信公众号-极致了接口查询的产业链企业微信公众号文章
     */
    @ExcelExport(value = "platform" , example = "wechat_IndustrialChain")
    private String platform;

    /**
     * 企业统一社会信用代码
     */
    @ExcelExport(value = "uniCode" , example = "企业统一社会信用代码")
    private String uniCode;

    /**
     * 公众号唯一CODE
     */
    @ExcelExport(value = "publicAccountUrl" , example = "公众号唯一CODE")
    private String publicAccountUrl;

    /**
     * 企业公众号名称
     */
    @ExcelExport(value = "newsSource" , example = "企业公众号名称")
    private String newsSource;

    /**
     * 全量/增量采集，必填，all:全量， incre:增量
     */
    @ExcelExport(value = "collectType" , example = "all/incre")
    private String collectType;
    /**
     * 搜索关键词，必填
     */
    @ExcelExport(value = "keyword",example = "搜索关键词")
    private String keyword;

    /**
     * 采集周期
     */
    @ExcelExport(value = "cycle", example = "采集周期,单位'天',(只需要输入数字)例: 7 ")
    private Integer keywordCycle;


}
