package cn.idicc.taotie.infrastructment.response.data;

import lombok.Data;

/**
 * 数据同步差异记录详情 - 响应对象
 *
 * @author taotie
 * @date 2026-02-03
 */
@Data
public class DataSyncDiffRecordDetailResponse {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的数据项ID
     */
    private Long itemId;

    /**
     * 关联的产业链ID
     */
    private Long chainId;

    /**
     * uniqueKey
     */
    private String uniqueKey;

    /**
     * 名称
     */
    private String name;

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