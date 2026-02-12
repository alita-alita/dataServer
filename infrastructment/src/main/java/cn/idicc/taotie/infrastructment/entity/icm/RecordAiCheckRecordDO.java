package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 产品关联表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_ai_check_record")
public class RecordAiCheckRecordDO extends BaseDO {

    /**
     * 0 正常流程 1 巡检
     */
    private Integer type;
    
    private String runDesc;

    private Long chainId;

    private String chainName;

    private Integer status;

    private String statusDesc;

    private Date completeDate;

}
