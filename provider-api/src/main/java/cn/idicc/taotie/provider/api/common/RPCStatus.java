package cn.idicc.taotie.provider.api.common;

public enum RPCStatus {

	SUCCESS(0, "success"),
	SYSTEM_UNEXCEPT_ERROR(50000, "系统异常"),

	//全局
	ERROR_PARAMETER_EMPTY(50001, "输入参数不可为空");


	int    status;
	String message;

	RPCStatus(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int status() {
		return this.status;
	}

	public String message() {
		return this.message;
	}

}
