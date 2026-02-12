package cn.idicc.taotie.infrastructment.request.icm;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import lombok.Data;
/**
 * 【疑似企业名录】模板下载
 */
@Data
public class TemplateDownload {
//    /**
//     * 企业id
//     */
//    @ExcelExport(value = "enterpriseId", example = "例:(使用模板时请将本行示例数据删除)企业id")
//    private String enterpriseId;

    /**
     * 企业名称
     */
    @ExcelExport(value = "enterpriseName", example = "企业名称")
    private String enterpriseName;

    /**
     * 企业社会统一信用代码
     */
    @ExcelExport(value = "enterpriseUniCode", example = "企业社会统一信用代码")
    private String enterpriseUniCode;

//    /**
//     * 产业链id
//     */
//    @ExcelExport(value = "industryChainId", example = "产业链id")
//    private Integer industryChainId;
//    //只留一个-匹配产业链之后再进行存入数据
//    /**
//     * 产业链
//     */
//    @ExcelExport(value = "industryChainName", example = "产业链名称")
//    private String industryChainName;

    /**
     * 数据来源
     */
    @ExcelExport(value = "dataSource", example = "类别（数据来源）")
    private String dataSource;



}
