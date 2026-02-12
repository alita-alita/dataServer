package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据同步任务数据项表 - 响应对象
 *
 * @author guyongliang
 * @date 2026-01-27
 */
@Data
public class DataSyncItemResponse {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 数据项名称
     */
    private String itemName;

    /**
     * 差异计算
     */
    private Boolean diffCalc;

    /**
     * 平台同步集市开关
     */
    private Boolean platformToMart;

    /**
     * 集市同步平台开关
     */
    private Boolean martToPlatform;

    /**
     * 平台同步生产开关
     */
    private Boolean platformToProd;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime gmtModify;
}