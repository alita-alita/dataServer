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
@TableName("industry_chain_atom_node")
public class IndustryChainAtomNodeDO extends BaseDO {

	@TableField("atom_node_name")
	private String atomNodeName;
	@TableField("node_desc")
	private String nodeDesc;

}
