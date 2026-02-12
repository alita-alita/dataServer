package cn.idicc.taotie.infrastructment.request.icm;

import javax.validation.constraints.NotEmpty;

public class IndustryChainAtomNodeAddRequest {

	@NotEmpty(message = "原子节点不可为空")
	private String atomNodeName;

	@NotEmpty(message = "原子节点描述不可为空")
	private String nodeDesc;

	public String getAtomNodeName() {
		return atomNodeName;
	}

	public void setAtomNodeName(String atomNodeName) {
		this.atomNodeName = atomNodeName;
	}

	public String getNodeDesc() {
		return nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}
}
