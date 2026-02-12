package cn.idicc.taotie.infrastructment.request.spider;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据同步任务数据项表 - 请求对象
 *
 * @author guyongliang
 * @date 2026-01-27
 */
@Data
public class DataSyncItemRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 数据项名称
     */
    @NotBlank(message = "数据项名称不能为空")
    private String itemName;

    /**
     * 差异计算
     */
    private Boolean diffCalc = false;

    /**
     * 平台同步集市开关
     */
    private Boolean platformToMart = false;

    /**
     * 集市同步平台开关
     */
    private Boolean martToPlatform = false;

    /**
     * 平台同步生产开关
     */
    private Boolean platformToProd = false;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

}