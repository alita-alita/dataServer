package cn.idicc.taotie.infrastructment.entity.spider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 核对 数据操作表
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class Hold {
    /**
     * 主键
     */
    private Long   holdId;
    /**
     * 企业名称
     */
    private String holderName;
    /**
     * 企业信用代码
     */
    private String holderUniCode;

    /**
     * 平台
     */
    private String holderPlatform;
    /**
     * 固定值（类型）
     */
    private String holderTaskCode;
    /**
     * 核对状态(0-未核对 1-新增入库 2-无新增)
     */
    private Integer holderState;
    /**
     * 上次核对时间
     */
    private String holderTime;
    /**
     * 核对周期
     */
    private Integer holderCycle;
    /**
     * 下次核对时间
     */
    private String holderNextTime;
    /**
     * 删除 0:否 1是
     */
    private Boolean deleted;


}
