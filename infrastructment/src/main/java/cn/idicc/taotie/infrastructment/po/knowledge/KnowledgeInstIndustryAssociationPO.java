package cn.idicc.taotie.infrastructment.po.knowledge;

import cn.idicc.taotie.infrastructment.dto.EnterpriseAndChainDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

/**
 * @author guyongliang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "knowledge_inst_industry_association", createIndex = false)
public class KnowledgeInstIndustryAssociationPO {

    /**
     * 协会id
     */
    @Id
    private Long id;

    /**
     * 协会md5
     */
    private String associationMd5;

    /**
     * 协会名称
     */
    private String associationName;

    /**
     * 协会统一社会信用代码
     */
    private String instindustryAssociationUniCode;

    /**
     * 协会官网网址
     */
    private String registerAddress;

    /**
     * 协会联系方式
     */
    private String mobile;

    /**
     * 区域代码
     */
    private String regionCode;

    @Field(type = FieldType.Nested)
    private Set<EnterpriseAndChainDTO> enterpriseAndChains;

}
