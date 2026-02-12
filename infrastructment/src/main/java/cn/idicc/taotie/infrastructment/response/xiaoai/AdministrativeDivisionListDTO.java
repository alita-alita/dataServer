package cn.idicc.taotie.infrastructment.response.xiaoai;

import cn.hutool.core.bean.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.xiaoai.AdministrativeDivisionDO;
import cn.idicc.taotie.infrastructment.enums.AdministrativeDivisionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: WangZi
 * @Date: 2023/1/4
 * @Description:
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeDivisionListDTO implements Serializable {

    private static final long serialVersionUID = 352738127646085673L;

    /**
     * 行政区划名称
     */
    private String name;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 行政区划code
     */
    private String code;

    public static AdministrativeDivisionListDTO adapt(AdministrativeDivisionDO param, AdministrativeDivisionTypeEnum typeEnum) {
        AdministrativeDivisionListDTO result = new AdministrativeDivisionListDTO();
        BeanUtil.copyProperties(param, result);
        switch (typeEnum) {
            case PROVINCE:
                result.setName(param.getProvince());
                break;
            case CITY:
                result.setName(param.getCity());
                break;
            case AREA:
                result.setName(param.getArea());
                break;
            default:
                break;
        }
        return result;
    }

}
