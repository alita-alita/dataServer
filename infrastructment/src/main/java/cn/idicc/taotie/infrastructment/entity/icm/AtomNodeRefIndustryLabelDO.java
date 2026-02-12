package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("atom_node_ref_industry_label")
public class AtomNodeRefIndustryLabelDO extends BaseDO {

	@TableField("atom_node_id")
	private Long atomNodeId;

	@TableField("industry_label_id")
	private Long industryLabelId;

}
