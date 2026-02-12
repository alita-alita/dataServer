package cn.idicc.taotie.infrastructment.entity.spider;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据同步差异数据记录表
 *
 * @author taotie
 * @date 2026-02-03
 */
@Data
@TableName("data_sync_diff_record")
public class DataSyncDiffRecordDO extends BaseDO {

    /**
     * 关联的数据项ID
     */
    private Long itemId;

    /**
     * 关联的产业链ID
     */
    private Long chainId;

    /**
     * 产品的MD5
     */
    private String productMd5;

    /**
     * 产品的名称
     */
    private String productName;

    /**
     * 平台是否存在
     */
    private Integer platformExists;

    /**
     * 集市是否存在
     */
    private Integer martExists;

    /**
     * 生产是否存在
     */
    private Integer prodExists;

}