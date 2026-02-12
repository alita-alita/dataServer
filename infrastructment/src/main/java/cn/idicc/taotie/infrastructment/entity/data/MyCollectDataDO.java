package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wd
 * @Date: 2023/5/4
 * @Description: 我的业务数据
 * @version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("my_collect_data")
public class MyCollectDataDO extends BaseDO {

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 业务类型 1收藏资讯 2关注企业
     */
    @TableField("bus_type")
    private Integer busType;

    /**
     * 业务id
     */
    @TableField("bus_id")
    private Long busId;

}
