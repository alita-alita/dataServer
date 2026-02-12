package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wd
 * @description 产业链节点标签关系
 */
@TableName("record_industry_chain_node_label_relation")
@Data
@NoArgsConstructor
public class RecordIndustryChainNodeLabelRelationDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	
	/** 产业链节点ID */
	private Long chainNodeId;
	/** 产业链标签ID */
	private Long industryLabelId;

	public RecordIndustryChainNodeLabelRelationDO(Long chainNodeId, Long industryLabelId) {
		this.chainNodeId = chainNodeId;
		this.industryLabelId = industryLabelId;
	}

	public static RecordIndustryChainNodeLabelRelationDO adapter(JSONObject jsonObject){
		RecordIndustryChainNodeLabelRelationDO res = new RecordIndustryChainNodeLabelRelationDO();
		res.setChainNodeId(jsonObject.getLong("chain_node_id"));
		res.setIndustryLabelId(jsonObject.getLong("industry_label_id"));
		res.setDeleted(jsonObject.getBoolean("deleted"));
		return res;
	}
}
