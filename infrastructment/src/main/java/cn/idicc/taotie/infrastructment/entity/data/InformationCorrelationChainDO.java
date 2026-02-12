package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangZi
 * @Date: 2023/6/20
 * @Description: 资讯和产业链关联关系实体类
 * @version: 1.0
 */
@Data
@TableName("information_correlation_chain")
public class InformationCorrelationChainDO extends BaseDO {

    /**
     * 产业链id
     */
    @TableField("chain_id")
    private Long chainId;

    /**
     * 资讯id
     */
    @TableField("information_id")
    private Long informationId;
}
