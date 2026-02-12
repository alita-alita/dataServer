package cn.idicc.taotie.infrastructment.entity.dw;

import cn.idicc.taotie.infrastructment.entity.icm.RecordIndustryChainNodeLabelRelationDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 产业链产业标签关系表
 * </p>
 *
 * @author MengDa
 * @since 2025-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dwd_industry_chain_industry_label_relation")
public class DwdIndustryChainIndustryLabelRelationDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private String id;

	/**
	 * 产业链id
	 */
	private Integer industryChainId;

	/**
	 * 产业链名称
	 */
	private String industryChainName;

	/**
	 * 产业链节点id
	 */
	private Integer industryChainNodeId;

	/**
	 * 节点名称
	 */
	private String industryChainNodeName;

	/**
	 * 产业标签id
	 */
	private Integer industryLabelId;

	/**
	 * 产业标签
	 */
	private String industryLabelName;

	/**
	 * 逻辑删除标志,1:删除 0:未删除
	 */
	@TableLogic(value = "0", delval = "1")
	private Boolean deleted;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreate;

	/**
	 * 最近一次更新时间
	 */
	private LocalDateTime gmtModify;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 更新者
	 */
	private String updateBy;

	/**
	 * 数据来源
	 */
	private String dataSource;

	public static DwdIndustryChainIndustryLabelRelationDO adapter(RecordIndustryChainNodeLabelRelationDO relationDO,
																  Long chainId, String chainName,
																  String nodeName, String labelName
	) {
		DwdIndustryChainIndustryLabelRelationDO res = new DwdIndustryChainIndustryLabelRelationDO();
//        res.setId(MD5Util.getMd5Id(chainId.toString()+relationDO.getChainNodeId().toString()+relationDO.getIndustryLabelId().toString()));
		res.setIndustryChainId(chainId.intValue());
		res.setIndustryChainName(chainName);
		res.setIndustryChainNodeId((relationDO.getChainNodeId()).intValue());
		res.setIndustryChainNodeName(nodeName);
		res.setIndustryLabelId((relationDO.getIndustryLabelId()).intValue());
		res.setIndustryLabelName(labelName);
		res.setDeleted(false);
		res.setGmtCreate(relationDO.getGmtCreate());
		res.setGmtModify(relationDO.getGmtModify());
		res.setCreateBy(relationDO.getCreateBy());
		res.setUpdateBy(relationDO.getUpdateBy());
		res.setDataSource("产业链管理平台");
		return res;
	}
}
