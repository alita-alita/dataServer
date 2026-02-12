package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: wd
 * @Date: 2023/3/23
 * @Description:汇率
 * @version: 1.0
 */

@Data
@TableName("exchange_rate")
public class ExchangeRateDO extends BaseDO {

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 汇率
     */
    @TableField("exchange_rate")
    private Double exchangeRate;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;
}
