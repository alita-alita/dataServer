package cn.idicc.taotie.infrastructment.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: WangZi
 * @Date: 2023/2/9
 * @Description: excel相关参数
 * @version: 1.0
 */
@Data
@Component
@RefreshScope
public class ExcelProperties {

    /**
     * 导入excel最大读取数据量
     */
    @Value("${excel.maxReadCount:70000}")
    private Integer maxReadCount;


    /**
     * 导入excel表头的下标
     */
    @Value("${excel.headerRowIndex:0}")
    private Integer headerRowIndex;

    /**
     * 导入excel开始读取数据的下标
     */
    @Value("${excel.startRowIndex:1}")
    private Integer startRowIndex;

    /**
     * 导入excel结束读取数据的下标
     */
    @Value("${excel.endRowIndex:70000}")
    private Integer endRowIndex;

    /**
     * 每个读取excel线程处理数据的最大数量
     */
    @Value("${excel.everyCount:1000}")
    private Integer everyCount;
}
