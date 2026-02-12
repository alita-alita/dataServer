package cn.idicc.taotie.infrastructment.response.icm;

import cn.hutool.core.bean.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.icm.IndustryChainAtomNodeDO;
import lombok.Data;

import java.util.List;

@Data
public class IndustryChainAtomNodeDTO {

    /**
     * 原子节点ID
     */
    private Long id;

    /**
     * 原子节点名称
     */
    private String atomNodeName;

    /**
     * 原子节点描述
     */
    private String nodeDesc;

    /**
     * 原子节点-标签-产业链关系
     */
    private List<AtomLabelChainRefBO> atomLabelChainRefList;

    public static IndustryChainAtomNodeDTO adapt(IndustryChainAtomNodeDO param) {
        IndustryChainAtomNodeDTO result = new IndustryChainAtomNodeDTO();
        BeanUtil.copyProperties(param, result);
        return result;
    }
}
