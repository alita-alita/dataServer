package cn.idicc.taotie.infrastructment.po.data;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.IndustryChainNodeDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Author: wd
 * @Date: 2022/12/24
 * @Description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@namespaceProperties.getNamespaceEsPrefix()}" + "industry_chain_node", createIndex = false)
public class IndustryChainNodePO extends BaseSearchPO{

    /**
     * 产业链id
     */
    private Long chainId;
    /**
     * 产业链名称
     */
    private String chainName;
    /**
     * 节点名称
     */
    private String nodeName;

    public static IndustryChainNodePO adapt(IndustryChainNodeDO param){
        return BeanUtil.copyProperties(param,IndustryChainNodePO.class);
    }

}
