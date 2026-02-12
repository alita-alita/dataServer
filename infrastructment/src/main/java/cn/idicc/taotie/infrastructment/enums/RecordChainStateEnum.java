package cn.idicc.taotie.infrastructment.enums;

public enum RecordChainStateEnum {

	NORMAL(0, "正常"),
	PRODUCING(1, "待生产"),
	PATENT_PROCESS(2, "专利找企业中"),
	PATENT_PROCESS_END(3, "专利找企业完成"),
	LABEL_FIND_ENTERPRISE(4, "产品找企业中"),
	LABEL_FIND_ENTERPRISE_END(5, "产品找企业完成"),
	ENTERPRISE_FIND_LABEL(6, "企业找产品中"),
	ENTERPRISE_FIND_LABEL_END(7, "企业找产品完成"),
	MATCH_ENTERPRISE(8, "挂载企业中"),
	MATCH_ENTERPRISE_END(9, "挂载企业完成"),
	WAITING_AI_QUALIFICATION(10, "待AI质检"),
	AI_QUALIFICATION_PROCESS(11, "AI质检中"),
	WAITING_HUMAN_QUALIFICATION(12, "待人工质检"),
	HUMAN_QUALIFICATION_FINISH(13, "人工质检完成"),
	WAITING_SYNC_DW(14, "待同步集市"),
	SYNCING_DW(15, "同步集市中"),
	SYNC_DW_FINISH(16, "同步集市完成"),
	WAITING_SYNC_PRODUCTION(17, "待同步生产"),
	SYNCING_PRODUCTION(18, "同步生产中"),
	SYNC_PRODUCTION_FINISH(19, "同步生产完成"),

	ERROR(98, "失败"),
	FINISH(99, "完成");


	int    value;
	String desc;

	RecordChainStateEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

}
