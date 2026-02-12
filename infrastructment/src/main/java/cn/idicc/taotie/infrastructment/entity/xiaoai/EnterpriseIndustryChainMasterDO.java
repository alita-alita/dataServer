package cn.idicc.taotie.infrastructment.entity.xiaoai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业产业链链主企业
 * </p>
 *
 * @author MengDa
 * @since 2024-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_industry_chain_master")
public class EnterpriseIndustryChainMasterDO extends DataSyncBaseDO {

	/**
	 * 企业社会统一信用代码
	 */
	private String uniCode;

	/**
	 * 企业地区code
	 */
	private String code;

	/**
	 * 产业链id
	 */
	private Long chainId;

	/**
	 * 产业链节点id
	 */
	private Long nodeId;

	/**
	 * 链主层次 产业链级(国家级) 0、产业链省级 1
	 */
	private Integer masterLevel;
}
