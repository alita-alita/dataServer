package cn.idicc.taotie.infrastructment.po.icm;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/3/28
 * @Description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(indexName = "industry_chain_product_embedding", createIndex = false)
public class IndustryChainProductEmbeddingPO {

    @Id
    @Field(name = "id")
    @JSONField(name = "id")
    private String id;

    @Field(name = "lib_file_id")
    @JSONField(name = "lib_file_id")
    private String libFileId;

    @Field(name = "chain_id")
    @JSONField(name = "chain_id")
    private Long chainId;

    @Field(name = "chain_name")
    @JSONField(name = "chain_name")
    private String chainName;

    // 0 节点 1 标签
    @Field(name = "type")
    @JSONField(name = "type")
    private Integer type;

    @Field(name = "product_name")
    @JSONField(name = "product_name")
    private String productName;

    @Field(name = "product_desc")
    @JSONField(name = "product_desc")
    private String productDesc;

    @Field(name = "product_way")
    @JSONField(name = "product_way")
    private String productWay;

    @Field(name = "vector")
    @JSONField(name = "vector")
    private List<Float> vector;
}
