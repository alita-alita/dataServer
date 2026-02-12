package cn.idicc.taotie.infrastructment.response.icm;

import cn.idicc.taotie.infrastructment.entity.icm.*;
import lombok.Data;

import java.util.List;

/**
 * @Author: MengDa
 * @Date: 2025/1/4
 * @Description:
 * @version: 1.0
 */
@Data
public class RecordChainImportDTO {

    private RecordIndustryChainDO industryChainDO;

    private List<RecordIndustryChainNodeDO> industryChainNodeDOS;

    private List<RecordIndustryLabelDO> industryLabelDOS;

    private List<RecordIndustryChainNodeLabelRelationDO> industryChainNodeLabelRelationDOS;

    private List<RecordIndustryChainCategoryDO> industryChainCategoryDOS;

    private List<RecordIndustryChainCategoryRelationDO> industryChainCategoryRelationDOS;
}
