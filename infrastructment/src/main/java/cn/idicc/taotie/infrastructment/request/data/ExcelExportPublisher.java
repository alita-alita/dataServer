package cn.idicc.taotie.infrastructment.request.data;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import lombok.Data;

/**
 * 【企业核对内容】导出模板
 */
@Data
public class ExcelExportPublisher {

    /**
     * 任务编号
     */
    @ExcelExport(value = "uniKey", example = "例:(使用模板时请将本行示例数据删除)")
    private String uniKey;
    /**
     * 固定值
     */
    @ExcelExport(value = "taskCode", example = "news_publisher")
    private String taskCode;

    /**
     * 平台，固定值：百度
     */
    @ExcelExport(value = "platform" , example = "public_account_check")
    private String platform;

    /**
     *  企业名称
     */
    @ExcelExport(value = "enterpriseName" , example = "企业名称")
    private String enterpriseName;

    /**
     * 企业统一社会信用代码
     */
    @ExcelExport(value = "uniCode",example = "企业统一社会信用代码")
    private String uniCode;


    /**
     * 采集周期
     */
    @ExcelExport(value = "cycle", example = "采集周期,单位'天',(只需要输入数字)例: 7 ")
    private Integer keywordCycle;

}
