package cn.idicc.taotie.infrastructment.entity.spider;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据同步任务数据项表
 *
 * @author guyongliang
 * @date 2026-01-27
 */
@Data
@TableName("data_sync_item")
public class DataSyncItemDO extends BaseDO {

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
}