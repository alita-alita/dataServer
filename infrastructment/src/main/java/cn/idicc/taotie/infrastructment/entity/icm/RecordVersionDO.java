package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 版本主表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_version")
public class RecordVersionDO extends BaseDO {

    /**
     * 版本
     */
    private String version;

    /**
     * 业务下关联键
     */
    private String businessUk;

    /**
     * 业务下关联键
     */
    private String businessRelationKey;

    /**
     * 同类型下排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 状态详情
     */
    private String stateDesc;

    /**
     * 是否上锁
     */
    private Boolean isLock;

    /**
     * 锁定人
     */
    private String lockBy;

}
