package cn.idicc.taotie.infrastructment.request.icm;

import cn.idicc.common.util.QueryPage;
import cn.idicc.taotie.infrastructment.entity.icm.RecordAiCheckRecordDO;
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
public class RecordAiCheckRecordQueryRequest extends QueryPage<RecordAiCheckRecordDO> implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 产业链名称
     */
    private Long chainId;


}
