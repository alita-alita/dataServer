package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.taotie.infrastructment.entity.xiaoai.DataSyncBaseDO;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("chain_node_ref_atom_node")
public class ChainNodeRefAtomNodeDO extends DataSyncBaseDO {

	private Long nodeId;
	private Long atomNodeId;

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Long getAtomNodeId() {
		return atomNodeId;
	}

	public void setAtomNodeId(Long atomNodeId) {
		this.atomNodeId = atomNodeId;
	}
}
