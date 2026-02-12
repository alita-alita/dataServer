package cn.idicc.taotie.infrastructment.request.data;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import lombok.Data;

/**
 * 【官网信息采集】导出模板
 */
@Data
public class ExcelExportOfficialWebsite {

    /**
     * 任务编号
     */
    @ExcelExport(value = "uniKey", example = "例:(使用模板时请将本行示例数据删除)")
    private String uniKey;
    /**
     * 固定值
     */
    @ExcelExport(value = "taskCode", example = "and_product")
    private String taskCode;

    /**
     * 采集平台，固定值：百度
     */
    @ExcelExport(value = "platform" , example = "official_website")
    private String platform;

    /**
     * 全量/增量采集，必填，all:全量， incre:增量
     */
    @ExcelExport(value = "enterpriseId" , example = "企业ID")
    private String enterpriseId;
    /**
     * 搜索关键词，必填
     */
    @ExcelExport(value = "enterpriseName",example = "企业名称")
    private String enterpriseName;
    /**
     * 统一社会信用代码
     */
    @ExcelExport(value = "uniCode",example = "统一社会信用代码")
    private String uniCode;

    /**
     * 当前时间
     */
    @ExcelExport(value = "publicAccountUrl",example = "官网链接")
    private String publicAccountUrl;

    /**
     * 采集周期
     */
    @ExcelExport(value = "cycle", example = "采集周期,单位'天',(只需要输入数字)例: 7 ")
    private Integer keywordCycle;
}
