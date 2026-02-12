package cn.idicc.taotie.service.message.data;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import cn.idicc.taotie.infrastructment.utils.ExcelImport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ExcelData {
    //平台
    @ExcelExport(value = "平台")
    @ExcelImport(value = "平台")
    private String platform;
    //社会信用代码
    @ExcelExport(value = "社会信用代码")
    @ExcelImport(value = "社会信用代码")
    private String uniCode;
    //产业链名称
    @ExcelExport(value = "产业链名称")
    @ExcelImport(value = "产业链名称")
    private String industryName;
    //全量/增量采集
    @ExcelExport(value = "全量/增量采集")
    @ExcelImport(value = "全量/增量采集")
    private String collectType;
    //搜索关键字
    @ExcelExport(value = "搜索关键字")
    @ExcelImport(value = "搜索关键字")
    private String keyword;

}
