package cn.idicc.taotie.infrastructment.response.icm;

import cn.idicc.taotie.infrastructment.utils.ExcelImport;
import lombok.Data;

/**
 *
 */
@Data
public class RecordProductDissociatedAdoptDTO {

    @ExcelImport(value = "enterpriseName")
    private String enterpriseName;

    @ExcelImport(value = "enterpriseUniCode")
    private String enterpriseUniCode;

    @ExcelImport(value = "productName")
    private String productName;

    @ExcelImport(value = "productUrl")
    private String productUrl;

    @ExcelImport(value = "productDescription")
    private String productDescription;

    @ExcelImport(value = "productPurpose")
    private String productPurpose;

    @ExcelImport(value = "nodeId")
    private Long nodeId;

    @ExcelImport(value = "nodeName")
    private String nodeName;

    @ExcelImport(value = "labelId")
    private Long labelId;

    @ExcelImport(value = "labelName")
    private String labelName;

}
