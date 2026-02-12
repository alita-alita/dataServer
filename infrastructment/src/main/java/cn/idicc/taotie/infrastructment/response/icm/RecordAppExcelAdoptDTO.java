package cn.idicc.taotie.infrastructment.response.icm;

import cn.idicc.taotie.infrastructment.utils.ExcelExport;
import cn.idicc.taotie.infrastructment.utils.ExcelImport;
import lombok.Data;

/**
 * 疑似名录 采用数据 实体类
 */
@Data
public class RecordAppExcelAdoptDTO {


    /**
     * 企业id
     */
    @ExcelImport(value = "enterpriseId")
    private String enterpriseId;

    /**
     * 企业名称
     */
    @ExcelImport(value = "enterpriseName")
    private String enterpriseName;

    /**
     * 企业社会统一信用代码
     */
    @ExcelImport(value = "enterpriseUniCode")
    private String enterpriseUniCode;

    /**
     * 产业链id
     */
    @ExcelImport(value = "industryChainId")
    private Integer industryChainId;
    //只留一个-匹配产业链之后再进行存入数据
    /**
     * 产业链
     */
    @ExcelImport(value = "industryChainName")
    private String industryChainName;

    /**
     * 数据来源
     */
    @ExcelImport(value = "dataSource")
    private String dataSource;



    /**
     * 版本
     */
    private String version;

    /**
     * 是否加入黑名单, 1:是 0:否
     */
    private Integer negative;




}
