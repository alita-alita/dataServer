package cn.idicc.taotie.infrastructment.response.icm.chain;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import cn.idicc.taotie.infrastructment.po.icm.IndustryChainProductEmbeddingPO;
import cn.idicc.taotie.infrastructment.utils.MD5Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: MengDa
 * @Date: 2025/3/25
 * @Description:
 * @version: 1.0
 */
@Data
public class ChainVectorDTO {
    //ID相关字段
    private String id;

    private String libFileId;

    private Long chainId;

    private String chainName;

    //数据字段如下
    // 0 节点 1 标签
    private Integer type;

    private String productName;

    private String productDesc;

    private String productWay;

    public IndustryChainProductEmbeddingPO toPo(){
        IndustryChainProductEmbeddingPO res = new IndustryChainProductEmbeddingPO();
        res.setLibFileId(libFileId);
        res.setChainId(chainId);
        res.setChainName(chainName);
        res.setType(type);
        res.setProductName(productName);
        res.setProductDesc(productDesc);
        res.setProductWay(productWay);
        res.setId(id);
        return res;
    }

    public String toVectorText(){
        return (productName==null?"":productName)+(productDesc==null?"":productDesc)+(productWay==null?"":productWay);
    }

    public String toId(String version){
        return MD5Util.getMd5Id(version+chainId+productName);
    }

    public static List<ChainVectorDTO> from(List<NodeChainDTO> chainDTOS, RecordIndustryChainDO chainDO){
        if (chainDTOS.isEmpty()){
            return new ArrayList<>();
        }
        NodeChainDTO last = chainDTOS.get(chainDTOS.size()-1);
        ChainVectorDTO vectorDTO = new ChainVectorDTO();
        vectorDTO.setProductName(chainDTOS.get(chainDTOS.size()-1).getNodeName());
        vectorDTO.setProductDesc(chainDTOS.get(chainDTOS.size()-1).getNodeDesc());
        vectorDTO.setProductWay(chainDTOS.stream().map(NodeChainDTO::getNodeName).collect(Collectors.joining("-")));
        vectorDTO.setType(0);
        vectorDTO.setChainId(chainDO.getBizId());
        vectorDTO.setChainName(chainDO.getChainName());
//        vectorDTO.setId(vectorDTO.toId(chainDO.getVersion()));
        if (vectorDTO.getProductDesc() != null){
            vectorDTO.setProductDesc(vectorDTO.getProductDesc().replace("\n",""));
        }
        List<ChainVectorDTO> res = new ArrayList<>();
        res.add(vectorDTO);
        if (last.getLabelDOList() != null){
            for (RecordIndustryLabelDO labelDO: last.getLabelDOList()){
                if (!labelDO.getLabelName().equals(vectorDTO.productName)) {
                    ChainVectorDTO tmp = new ChainVectorDTO();
                    tmp.setProductName(labelDO.getLabelName());
                    tmp.setProductDesc(labelDO.getLabelDesc());
                    tmp.setProductWay(vectorDTO.getProductWay()+"-"+labelDO.getLabelName());
                    tmp.setType(1);
                    tmp.setChainId(chainDO.getBizId());
                    tmp.setChainName(chainDO.getChainName());
//                    tmp.setId(tmp.toId(chainDO.getVersion()));
                    if (tmp.getProductDesc() != null){
                        tmp.setProductDesc(tmp.getProductDesc().replace("\n",""));
                    }
                    res.add(tmp);
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "ChainVectorDTO{" +
                "type=" + type +
                ", productName='" + productName + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", productWay='" + productWay + '\'' +
                '}';
    }
}
