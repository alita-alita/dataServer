package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wd
 * @description 产业链节点标签关系
 */
@TableName("industry_chain_node_label_relation")
@Data
@NoArgsConstructor
public class IndustryChainNodeLabelRelationDO extends BaseDO {
	private static final long serialVersionUID = 1L;
	
	/** 产业链节点ID */
	private Long chainNodeId;
	/** 产业链标签ID */
	private Long industryLabelId;

	public IndustryChainNodeLabelRelationDO(Long chainNodeId, Long industryLabelId) {
		this.chainNodeId = chainNodeId;
		this.industryLabelId = industryLabelId;
	}
}
