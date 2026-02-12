package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.bean.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.data.EnterpriseLabelDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: WangZi
 * @Date: 2023/1/6
 * @Description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseLabelDTO implements Serializable {

    private static final long serialVersionUID = -8124344552477325682L;

    /**
     * 企业标签id
     */
    private Long id;

    /**
     * 企业标签名称
     */
    private String labelName;

    /**
     * 企业类型id
     */
    private Long labelTypeId;


    public static EnterpriseLabelDTO adapt(EnterpriseLabelDO param) {
        EnterpriseLabelDTO result = new EnterpriseLabelDTO();
        BeanUtil.copyProperties(param, result);
        return result;
    }
}
