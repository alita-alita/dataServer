package cn.idicc.taotie.infrastructment.entity.data;

import cn.idicc.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wd
 * 产业链节点
 */
@TableName("industry_chain_node")
@Data
@NoArgsConstructor
public class IndustryChainNodeDO extends BaseDO {
	private static final long serialVersionUID = 1L;
	
	/** 产业链ID */
	private Long chainId;
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
	/**
	 * x坐标值
	 */
	@TableField("abscissa_value")
	private Double abscissaValue;
	/**
	 * y坐标值
	 */
	@TableField("ordinate_value")
	private Double ordinateValue;
	/**
	 * 节点间线坐标json串
	 */
	@TableField("line_info")
	private String lineInfo;

	/**
	 * 子节点集合
	 */
	@TableField(exist = false)
	private List<IndustryChainNodeDO> childNodes;

	public IndustryChainNodeDO(Long chainId, String nodeName, Integer nodeOrder,
                               Integer nodeLevel, Long nodeParent, Integer isLeaf,
                               Integer showMaxNumber,
                               Double abscissaValue,
                               Double ordinateValue,
                               String lineInfo) {
		this.chainId = chainId;
		this.nodeName = nodeName;
		this.nodeOrder = nodeOrder;
		this.nodeLevel = nodeLevel;
		this.nodeParent = nodeParent;
		this.isLeaf = isLeaf;
		this.showMaxNumber = showMaxNumber;
		this.abscissaValue = abscissaValue;
		this.ordinateValue = ordinateValue;
		this.lineInfo = lineInfo;
	}


}
