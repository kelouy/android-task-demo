package com.task.common.transbean;

import java.io.Serializable;


public class TranObject<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TranObjectType type;
	private boolean success = false;
	private String msg;
	private int fromUser;
	private int toUser;
	private T object;
	
	public TranObject(TranObjectType type) {
		this.type = type;
	}
	
	public TranObjectType getType() {
		return type;
	}
	public void setType(TranObjectType type) {
		this.type = type;
	}
	public int getFromUser() {
		return fromUser;
	}
	public void setFromUser(int fromUser) {
		this.fromUser = fromUser;
	}
	public int getToUser() {
		return toUser;
	}
	public void setToUser(int toUser) {
		this.toUser = toUser;
	}
	public T getObject() {
		return object;
	}
	public void setObject(T object) {
		this.object = object;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
