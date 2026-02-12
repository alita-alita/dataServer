package cn.idicc.taotie.provider.api.common;

public class RPCResultBuilder {

	public static <T> RPCResult<T> success(T data) {
		return new RPCResult<T>(data);
	}

	public static <T> RPCResult<T> failed(int status, String errorMsg, T data) {
		return new RPCResult<T>(status, errorMsg, data);
	}

	public static <T> RPCResult<T> build(RPCStatus rpcStatus, T data) {
		return new RPCResult<T>(rpcStatus.status(), rpcStatus.message(), data);
	}

	public static RPCResult<Void> build(RPCStatus rpcStatus) {
		return new RPCResult<>(rpcStatus.status(), rpcStatus.message(), null);
	}

}
