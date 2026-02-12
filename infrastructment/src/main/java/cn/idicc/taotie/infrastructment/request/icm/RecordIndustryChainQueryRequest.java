package cn.idicc.taotie.infrastructment.request.icm;

import cn.idicc.common.util.QueryPage;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wd
 * @description 产业链分页查询DTO
 * @date 12/19/22 10:02 AM
 */
@Data
public class RecordIndustryChainQueryRequest extends QueryPage<RecordIndustryChainDO> implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 产业链名称
     */
    private String chainName;
    /**
     * 备注
     */
    private String notes;

    /**
     * version state
     */
    private Integer state;

    private Long categoryId;

    private List<Long> chainIds;

}
