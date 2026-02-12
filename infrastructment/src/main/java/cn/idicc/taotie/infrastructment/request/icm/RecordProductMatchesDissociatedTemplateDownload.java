package cn.idicc.taotie.infrastructment.request.icm;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import lombok.Data;

/**
 * 模板下载
 */
@Data
public class RecordProductMatchesDissociatedTemplateDownload {

    @ExcelExport(value = "enterpriseName",example = "企业名称(必填)")
    private String enterpriseName;

    @ExcelExport(value = "enterpriseUniCode",example = "企业社会统一信用代码(必填)")
    private String enterpriseUniCode;

    @ExcelExport(value = "productName",example = "产品名称(必填)")
    private String productName;

    @ExcelExport(value = "productUrl",example = "产品URL")
    private String productUrl;

    @ExcelExport(value = "productDescription",example = "产品描述")
    private String productDescription;

    @ExcelExport(value = "productPurpose",example = "产品用途")
    private String productPurpose;

    @ExcelExport(value = "nodeId",example = "节点ID(必填)")
    private Long nodeId;

    @ExcelExport(value = "nodeName",example = "节点名称")
    private String nodeName;

    @ExcelExport(value = "labelId",example = "标签ID(必填)")
    private Long labelId;

    @ExcelExport(value = "labelName",example = "标签名称")
    private String labelName;


}
