package cn.idicc.taotie.infrastructment.response.data;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据同步执行日志表 - 响应对象
 *
 * @author taotie
 * @date 2026-01-27
 */
@Data
public class DataSyncTaskLogResponse {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 比较任务时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime diffCalcTime;

    /**
     * 比较任务状态
     */
    private Integer diffCalcStatus;

    /**
     * 平台集市同步时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime platformToMartTime;

    /**
     * 平台集市同步状态
     */
    private Integer platformToMartStatus;

    /**
     * 平台生产同步时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime platformToProdTime;

    /**
     * 平台生产同步状态
     */
    private Integer platformToProdStatus;

    /**
     * 集市平台同步时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime martToPlatformTime;

    /**
     * 集市平台同步状态
     */
    private Integer martToPlatformStatus;

    /**
     * 平台数量
     */
    private Integer platformCount;

    /**
     * 集市数量
     */
    private Integer martCount;

    /**
     * 生产数量
     */
    private Integer prodCount;

    /**
     * 关联的数据项ID
     */
    private Long itemId;

    /**
     * 关联的数据项名称
     */
    private String itemName;

    /**
     * 关联的产业链ID
     */
    private Long chainId;

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