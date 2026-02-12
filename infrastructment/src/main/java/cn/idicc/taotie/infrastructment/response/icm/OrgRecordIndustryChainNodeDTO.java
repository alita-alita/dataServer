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
public class OrgRecordIndustryChainNodeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 节点id */
	private Long id;

	/**
	 * 版本
	 */
	private String version;
	/** 节点名称 */
	private String nodeName;
	/** 父节点，当节点为根节点时取固定值0. */
	private Long nodeParent;
	/** 节点深度 */
	private Integer nodeLevel;
	/**
	 * 挂载节点数量
	 */
	private Integer leafNodeCount;
	/** 是否是挂载企业节点 0: 不是 1: 是 */
	private Integer isLeaf;
	/** 企业显示最大数量 */
	private Integer showMaxNumber;
	/** 关联企业数 */
	private Integer relevanceEnterpriseNumber;
	/** 关联本地企业数 */
	private Integer localEnterpriseNumber;
	/** x坐标值 */
	private Double abscissaValue;
	/** y坐标值 */
	private Double ordinateValue;
	/** 节点间线坐标json串 */
	private String lineInfo;
	/** 节点属性 */
	private String nodePropose;

	/**
	 * 节点描述
	 */
	private String nodeDesc;

	/**
	 * 节点产品匹配度阈值
	 */
	private BigDecimal thresholdScore;
	/** 产业链节点 */
	private List<OrgRecordIndustryChainNodeDTO> childNodes;

	public static OrgRecordIndustryChainNodeDTO adapter(RecordIndustryChainNodeDO nodeDO){
		OrgRecordIndustryChainNodeDTO res = BeanUtil.copyProperties(nodeDO, OrgRecordIndustryChainNodeDTO.class);
		res.setId(nodeDO.getBizId());
		return res;
	}

}
