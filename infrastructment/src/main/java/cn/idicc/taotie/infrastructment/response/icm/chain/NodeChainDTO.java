package cn.idicc.taotie.infrastructment.response.icm.chain;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import lombok.Data;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/3/25
 * @Description:
 * @version: 1.0
 */
@Data
public class NodeChainDTO {
    private Long chainId;

    private Long nodeId;

    private String nodeName;

    private String nodeDesc;

    private Integer nodeLevel;

    private Long nodeParent;

    private Integer isLeaf;

    private List<RecordIndustryLabelDO> labelDOList;

    public NodeChainDTO(RecordIndustryChainNodeDO nodeDO){
        this.chainId = nodeDO.getChainId();
        this.nodeId = nodeDO.getBizId();
        this.nodeName = nodeDO.getNodeName();
        this.nodeDesc = nodeDO.getNodeDesc();
        this.nodeLevel = nodeDO.getNodeLevel();
        this.nodeParent = nodeDO.getNodeParent();
        this.isLeaf = nodeDO.getIsLeaf();
    }
}
