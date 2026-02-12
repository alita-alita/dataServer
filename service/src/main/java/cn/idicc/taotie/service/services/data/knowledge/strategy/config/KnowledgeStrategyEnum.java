package cn.idicc.taotie.service.services.data.knowledge.strategy.config;

import lombok.Getter;

/**
 * 数据同步策略枚举
 *
 * @author guyongliang
 */

@Getter
public enum KnowledgeStrategyEnum {

	//企业基本信息同步策略
	ENTERPRISE_SYNC_STRATEGY(0, "enterpriseDataSyncStrategy"),
	ENTERPRISE_PRODUCT_SYNC_STRATEGY(1, "enterpriseProductDataSyncStrategy"),
	TALENT_ENTERPRISE_SYNC_STRATEGY(2, "talentEnterpriseDataSyncStrategy"),
	INST_INDUSTRY_ASSOCIATION_SYNC_STRATEGY(2, "instIndustryAssociationDataSyncStrategy"),

	//产业链节点数据同步策略
	INDUSTRY_CHAIN_NODE_SYNC_STRATEGY(3, "industryChainNodeDataSyncStrategy"),
	;

	/**
	 * 编码
	 */
	private Integer code;

	/**
	 * 描述
	 */
	private String name;

	KnowledgeStrategyEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static KnowledgeStrategyEnum getByCode(Integer code) {
		for (KnowledgeStrategyEnum item : values()) {
			if (item.getCode()
					.equals(code)) {
				return item;
			}
		}
		return null;
	}
}
