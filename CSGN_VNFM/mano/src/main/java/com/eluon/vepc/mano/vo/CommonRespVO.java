package com.eluon.vepc.mano.vo;

public class CommonRespVO<T> extends BaseVO{

	private boolean success;
	private String message;
	private T data;
	
	public CommonRespVO(boolean success,String message, T data){
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
