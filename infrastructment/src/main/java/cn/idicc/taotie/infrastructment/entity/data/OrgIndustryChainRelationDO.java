package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 机构产业链标签关系
 * @author wd
 */
@TableName("org_industry_chain_relation")
@Data
@NoArgsConstructor
public class OrgIndustryChainRelationDO extends BaseDO {
	private static final long serialVersionUID = 1L;
	
	/** 组织机构ID */
	private Long organizeId;
	/** 产业链ID */
	private Long industryChainId;
	/** 状态 0：禁用 1：启用 */
	private Boolean status;
	/** 备注 */
	private String notes;

	public OrgIndustryChainRelationDO(Long organizeId, Long industryChainId, String notes) {
		this.organizeId = organizeId;
		this.industryChainId = industryChainId;
		this.notes = notes;
	}

	public OrgIndustryChainRelationDO(Long id, Boolean status) {
		super.setId(id);
		this.status = status;
	}
}
