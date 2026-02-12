package cn.idicc.taotie.infrastructment.request.data;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import cn.idicc.taotie.infrastructment.utils.ExcelImport;
import lombok.Data;

/**
 * 【人才招商-关键词采集】导出模板
 */
@Data
public class ExcelExportTalent {

    /**
     * 任务编号
     */
    @ExcelExport(value = "uniKey", example = "例:(使用模板时请将本行示例数据删除)")
    private String uniKey;
    /**
     * 固定值
     */
    @ExcelExport(value = "taskCode", example = "news_talent")
    private String taskCode;

    /**
     * 平台，固定值：百度
     */
    @ExcelExport(value = "platform" , example = "baidu_wechat")
    private String platform;

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
