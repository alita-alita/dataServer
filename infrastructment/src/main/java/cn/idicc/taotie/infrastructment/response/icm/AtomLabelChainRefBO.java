package cn.idicc.taotie.infrastructment.response.icm;

import lombok.Data;

@Data
public class AtomLabelChainRefBO {

    /**
     * 原子节点ID
     */
    private Long atomNodeId;

    /**
     * 原子节点名称
     */
    private String atomNodeName;

    /**
     * 标签ID
     */
    private Long labelId;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 产业链ID
     */
    private Long chainId;

    /**
     * 产业链名称
     */
    private String chainName;
}
