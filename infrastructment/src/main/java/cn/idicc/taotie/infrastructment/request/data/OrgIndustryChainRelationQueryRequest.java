package cn.idicc.taotie.infrastructment.request.data;

import cn.idicc.common.util.QueryPage;
import cn.idicc.taotie.infrastructment.entity.data.OrgIndustryChainRelationDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wd
 * @description 产业链分页查询DTO
 * @date 12/19/22 10:02 AM
 */
@Data
public class OrgIndustryChainRelationQueryRequest extends QueryPage<OrgIndustryChainRelationDO> implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 机构id
     */
    private Long orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 产业链名称
     */
    private String chainName;
    /**
     * 备注
     */
    private String notes;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 产业链id集合
     */
    private List<Long> chainIds;

}
