package cn.idicc.taotie.infrastructment.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 按天统计数据总量[数据监控]
 *
 * @author wd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataWatchDailyCountStatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 统计类型编码
     */
    private Integer code;

    /**
     * 统计类型名称
     */
    private String name;

    /**
     * 统计数值【业务数据】
     */
    private Integer odsCount;

    /**
     * 统计数值【数据组数据】
     */
    private Integer dwdCount;

    /**
     * 统计值
     */
    private Integer value;
}
