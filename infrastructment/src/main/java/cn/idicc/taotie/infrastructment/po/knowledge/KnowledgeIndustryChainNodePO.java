package cn.idicc.taotie.infrastructment.po.knowledge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "knowledge_industry_chain_node", createIndex = false)
public class KnowledgeIndustryChainNodePO {
    /**
     * 节点id
     */
    private Long id;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点概念
     */
    private String nodeConcept;
    /**
     * 节点层级
     */
    private Long nodeLevel;
    /**
     * 节点下对应的产品名称
     */
    private List<String> productNames;
    /**
     * 父节点id
     */
    private Long nodeParent;
    /**
     * 根节点产业链id
     */
    private Long chainId;
   /**
     * 根节点产业链名称
     */
   /* private String chainName;*/
    /**
     * 根节点产业链概念
     */
    /*private String chainConcept;*/
}
