package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.bean.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.IndustryLabelDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 产业链标签DTO
 *
 * @author wd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndustryLabelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产业链标签id
     */
    private Long id;

    /**
     * 产业链标签名称
     */
    private String labelName;

    /**
     * 标签分类：0未知 1产品 2技术
     */
    private Byte labelType;

    public static IndustryLabelDTO adapt(IndustryLabelDO param) {
        IndustryLabelDTO result = new IndustryLabelDTO();
        BeanUtil.copyProperties(param, result);
        return result;
    }

}
