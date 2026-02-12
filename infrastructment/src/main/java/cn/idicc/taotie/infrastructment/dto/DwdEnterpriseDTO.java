package cn.idicc.taotie.infrastructment.dto;

import lombok.Data;

/**
 *
 */
@Data
public class DwdEnterpriseDTO {

    /**
     * 企业id
     */
    private String id;

    /**
     * 统一社会信用代码
     */
    private String uniCode;

    /**
     * 企业名称
     */
    private String enterpriseName;
}
