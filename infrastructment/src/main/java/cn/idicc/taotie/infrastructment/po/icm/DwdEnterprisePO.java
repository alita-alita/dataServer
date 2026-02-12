package cn.idicc.taotie.infrastructment.po.icm;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 */
@Data
@Document(indexName = "dwd_enterprise", createIndex = false)
public class DwdEnterprisePO {

    /**
     * 企业id
     */
    @Field(name = "id")
    private String id;

    @Field(name = "uni_code")
    private String uniCode;

    /**
     * 企业名称
     */

    @Field(name = "enterprise_name")
    private String enterpriseName;
}
