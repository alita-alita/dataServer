package cn.idicc.taotie.provider.api.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * RPC 响应格式
 */
public class RPCResult<T> implements Serializable {

	private int    status;
	private String msg;
	private T      data;

	public RPCResult(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public RPCResult(T data) {
		this(0, "success", data);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
