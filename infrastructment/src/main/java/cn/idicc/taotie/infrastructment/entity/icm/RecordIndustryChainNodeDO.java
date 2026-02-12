package cn.idicc.taotie.infrastructment.entity.icm;

import cn.idicc.common.model.BaseDO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wd
 * 产业链节点
 */
@TableName("record_industry_chain_node")
@Data
@NoArgsConstructor
public class RecordIndustryChainNodeDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	/**
	 * 业务主键ID
	 */
	private Long bizId;


	/**
	 * 产业链ID
	 */
	private Long    chainId;
	/**
	 * 节点名称
	 */
	private String  nodeName;
	/**
	 * 节点序号
	 */
	private Integer nodeOrder;
	/**
	 * 节点深度，0为根节点
	 */
	private Integer nodeLevel;
	/**
	 * 父节点，当节点为根节点时取固定值0.
	 */
	private Long    nodeParent;
	/**
	 * 是否是挂载企业节点 0: 不是 1: 是
	 */
	private Integer isLeaf;
	/**
	 * 企业显示最大数量
	 */
	private Integer showMaxNumber;
	/**
	 * x坐标值
	 */
	@TableField("abscissa_value")
	private Double  abscissaValue;
	/**
	 * y坐标值
	 */
	@TableField("ordinate_value")
	private Double  ordinateValue;
	/**
	 * 节点间线坐标json串
	 */
	@TableField("line_info")
	private String  lineInfo;


	/**
	 * 节点描述
	 */
	@TableField("node_desc")
	private String nodeDesc;


	/**
	 * 节点产品匹配度阈值
	 */
	@TableField("threshold_score")
	private BigDecimal thresholdScore;

	/**
	 * 子节点集合
	 */
	@TableField(exist = false)
	private List<RecordIndustryChainNodeDO> childNodes;

	public static RecordIndustryChainNodeDO adapter(JSONObject jsonObject) {
		RecordIndustryChainNodeDO res = new RecordIndustryChainNodeDO();
		res.setBizId(jsonObject.getLong("id"));
		res.setChainId(jsonObject.getLong("chain_id"));
		res.setNodeName(jsonObject.getString("node_name").isEmpty() ? null : jsonObject.getString("node_name"));
		res.setNodeOrder(jsonObject.getInteger("node_order"));
		res.setNodeLevel(jsonObject.getInteger("node_level"));
		res.setNodeParent(jsonObject.getLong("node_parent"));
		res.setIsLeaf(jsonObject.getInteger("is_leaf"));
		res.setShowMaxNumber(jsonObject.getInteger("show_max_number"));
		res.setAbscissaValue(jsonObject.getDoubleValue("abscissa_value"));
		res.setOrdinateValue(jsonObject.getDoubleValue("ordinate_value"));
		res.setLineInfo(jsonObject.getString("line_info").isEmpty() ? null : jsonObject.getString("line_info"));
		res.setDeleted(jsonObject.getBoolean("deleted"));
		return res;
	}

	public RecordIndustryChainNodeDO(Long chainId, String nodeName, Integer nodeOrder,
									 Integer nodeLevel, Long nodeParent, Integer isLeaf,
									 Integer showMaxNumber,
									 Double abscissaValue,
									 Double ordinateValue,
									 String lineInfo, String nodeDesc, Double thresholdScore) {
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
		this.nodeDesc = nodeDesc;
		if (thresholdScore != null) {
			this.thresholdScore = new BigDecimal(thresholdScore);
		}
	}


}
