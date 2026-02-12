package cn.idicc.taotie.infrastructment.entity.xiaoai;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: WangZi
 * @Date: 2023/1/4
 * @Description: 行政区划
 * @version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("administrative_division")
public class AdministrativeDivisionDO extends BaseDO {

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 区县
     */
    @TableField("area")
    private String area;

    /**
     * 行政区划代码code
     */
    @TableField("code")
    private String code;

    /**
     * 父节点id
     */
    @TableField("parent_id")
    private String parentId;

    public AdministrativeDivisionDO(String province, String city, String area) {
        this.province = province;
        this.city = city;
        this.area = area;
    }
}
