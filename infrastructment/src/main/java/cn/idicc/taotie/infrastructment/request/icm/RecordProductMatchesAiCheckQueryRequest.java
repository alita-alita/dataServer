package cn.idicc.taotie.infrastructment.request.icm;

import cn.idicc.common.util.QueryPage;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import cn.idicc.taotie.infrastructment.entity.icm.RecordProductMatchesAiCheckDO;
import cn.idicc.taotie.infrastructment.enums.RecordAiCheckRecordStateEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wd
 * @description 产业链分页查询DTO
 * @date 12/19/22 10:02 AM
 */
@Data
public class RecordProductMatchesAiCheckQueryRequest extends QueryPage<RecordProductMatchesAiCheckDO> implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 产业链名称
     */
    private String version;

    private String enterpriseName;

    private String productName;

    private Integer status = RecordAiCheckRecordStateEnum.NOT_PASS.getCode();


    private Long chainId;

    private Boolean isNegative;
    
    private Long labelId;

    private Long nodeId;
}
