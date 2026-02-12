package cn.idicc.taotie.infrastructment.enums;

public enum AdministrativeDivisionEnums {

	CHINA("000000", "全国");
	final String code;
	final String name;

	AdministrativeDivisionEnums(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}


	public String getName() {
		return name;
	}
}
