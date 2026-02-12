package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人才产业链关联表
 * </p>
 *
 * @author MengDa
 * @since 2024-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("talent_industry_chain_relation")
public class TalentIndustryChainRelationDO extends DataSyncBaseDO {

	/**
	 * 人才唯一标识
	 */
	private String talentMd5;

	/**
	 * 产业链id
	 */
	private Long chainId;

	/**
	 * 产业链节点id
	 */
	private Long nodeId;
}
