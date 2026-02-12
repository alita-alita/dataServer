package cn.idicc.taotie.infrastructment.entity.spider;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据同步执行日志表
 *
 * @author taotie
 * @date 2026-01-27
 */
@Data
@TableName("data_sync_task_log")
public class DataSyncTaskLogDO extends BaseDO {

    /**
     * 比较任务时间
     */
    private LocalDateTime diffCalcTime;

    /**
     * 比较任务状态（0待执行1执行中2执行成功3执行失败）
     */
    private Integer diffCalcStatus;

    /**
     * 平台集市同步时间
     */
    private LocalDateTime platformToMartTime;

    /**
     * 平台集市同步状态（0待执行1执行中2执行成功3执行失败）
     */
    private Integer platformToMartStatus;

    /**
     * 平台生产同步时间
     */
    private LocalDateTime platformToProdTime;

    /**
     * 平台生产同步状态（0待执行1执行中2执行成功3执行失败）
     */
    private Integer platformToProdStatus;

    /**
     * 集市平台同步时间
     */
    private LocalDateTime martToPlatformTime;

    /**
     * 集市平台同步状态（0待执行1执行中2执行成功3执行失败）
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
     * 关联的产业链ID
     */
    private Long chainId;
}