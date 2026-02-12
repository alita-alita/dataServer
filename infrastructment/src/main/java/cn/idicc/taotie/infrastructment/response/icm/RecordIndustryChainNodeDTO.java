package cn.idicc.taotie.infrastructment.response.icm;

import cn.idicc.common.util.BeanUtil;
import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产业链节点树DTO
 * @author wd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordIndustryChainNodeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 产业链ID */
	private Long chainId;
	/** 产业链节点id */
	private Long id;
	/** 节点名称 */
	private String nodeName;
	/** 节点序号 */
	private Integer nodeOrder;
	/** 节点深度，0为根节点 */
	private Integer nodeLevel;
	/** 父节点，当节点为根节点时取固定值0. */
	private Long nodeParent;
	/** 是否是挂载企业节点 0: 不是 1: 是 */
	private Integer isLeaf;
	/** 企业显示最大数量 */
	private Integer showMaxNumber;
	/** x坐标值 */
	private Double abscissaValue;
	/** y坐标值 */
	private Double ordinateValue;
	/** 节点间线坐标json串 */
	private String lineInfo;

	/** 关联原子节点ID， 是叶子节点时存在 */
	private Long atomNodeId;

	/**
	 * 节点描述
	 */
	private String nodeDesc;
	/**
	 * 节点产品匹配度阈值
	 */
	private BigDecimal thresholdScore;

	public static RecordIndustryChainNodeDTO adapt(RecordIndustryChainNodeDO param){
		RecordIndustryChainNodeDTO res = BeanUtil.copyProperties(param, RecordIndustryChainNodeDTO.class);
		res.setId(param.getBizId());
		return res;
	}
}
