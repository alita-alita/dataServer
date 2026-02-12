package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 研究机构
 * </p>
 *
 * @author MengDa
 * @since 2024-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("institution_industry_chain_relation")
public class InstitutionIndustryChainRelationDO extends DataSyncBaseDO {

	/**
	 * 产业链ID
	 */
	private Long industryChainId;

	/**
	 * 节点ID
	 */
	private Long industryChainNodeId;

	/**
	 * 名称md5
	 */
	private String institutionMd5;


}
