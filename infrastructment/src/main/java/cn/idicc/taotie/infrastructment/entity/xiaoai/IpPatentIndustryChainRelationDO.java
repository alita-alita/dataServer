package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 专利产业链表
 * </p>
 *
 * @author MengDa
 * @since 2024-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ip_patent_industry_chain_relation")
public class IpPatentIndustryChainRelationDO extends DataSyncBaseDO {

	/**
	 * md5 专利申请号
	 */
	private String patentMd5;

	/**
	 * 产业链ID
	 */
	private Long industryChainId;

	/**
	 * 节点ID
	 */
	private Long industryChainNodeId;


}
