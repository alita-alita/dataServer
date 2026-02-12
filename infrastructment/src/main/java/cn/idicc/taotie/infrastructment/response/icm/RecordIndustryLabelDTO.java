package cn.idicc.taotie.infrastructment.response.icm;

import cn.hutool.core.bean.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryLabelDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 产业链标签DTO
 *
 * @author wd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordIndustryLabelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产业链标签id
     */
    private Long id;

    /**
     * 产业链标签名称
     */
    private String labelName;

    private String labelDesc;

    /**
     * 标签分类：0未知 1产品 2技术
     */
    private Byte labelType;

    /**
     * 原子节点-标签-产业链关系
     */
    private List<AtomLabelChainRefBO> atomLabelChainRefList;

    public static RecordIndustryLabelDTO adapt(RecordIndustryLabelDO param) {
        RecordIndustryLabelDTO result = new RecordIndustryLabelDTO();
        BeanUtil.copyProperties(param, result);
        result.setId(param.getBizId());
        return result;
    }

    public static RecordIndustryLabelDTO adapt(AtomLabelChainRefBO param) {
        RecordIndustryLabelDTO result = new RecordIndustryLabelDTO();
        BeanUtil.copyProperties(param, result);
        result.setId(param.getLabelId());
        return result;
    }
}
