package cn.idicc.taotie.infrastructment.response.icm.chain;

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
public class IndustryChainProductEmbeddingDTO {

    @Id
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "lib_file_id")
    private String libFileId;

    @JSONField(name = "chain_id")
    private Long chainId;

    @JSONField(name = "chain_name")
    private String chainName;

    // 0 节点 1 标签
    @JSONField(name = "type")
    private Integer type;

    @JSONField(name = "product_name")
    private String productName;

    @JSONField(name = "product_desc")
    private String productDesc;

    @JSONField(name = "product_way")
    private String productWay;

    @JSONField(name = "vector")
    private List<Float> vector;

    @JSONField(name = "score")
    private Float score;
}
