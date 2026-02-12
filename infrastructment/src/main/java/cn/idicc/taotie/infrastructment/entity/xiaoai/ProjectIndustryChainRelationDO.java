package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目产业链关系表
 * </p>
 *
 * @author MengDa
 * @since 2024-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_industry_chain_relation")
public class ProjectIndustryChainRelationDO extends DataSyncBaseDO {

	/**
	 * md5
	 */
	private String projectMd5;

	/**
	 * 产业链ID
	 */
	private Long industryChainId;

	/**
	 * 节点ID
	 */
	private Long industryChainNodeId;
}
