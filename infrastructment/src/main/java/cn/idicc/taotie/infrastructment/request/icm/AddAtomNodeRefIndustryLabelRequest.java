package cn.idicc.taotie.infrastructment.request.icm;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class AddAtomNodeRefIndustryLabelRequest {

	@NotEmpty(message = "原子节点ID不能为空")
	private Long       atomNodeId;
	@NotEmpty(message = "产业链标签ID不能为空")
	private List<Long> industryLabelId;

	public Long getAtomNodeId() {
		return atomNodeId;
	}

	public void setAtomNodeId(Long atomNodeId) {
		this.atomNodeId = atomNodeId;
	}

	public List<Long> getIndustryLabelId() {
		return industryLabelId;
	}

	public void setIndustryLabelId(List<Long> industryLabelId) {
		this.industryLabelId = industryLabelId;
	}
}
