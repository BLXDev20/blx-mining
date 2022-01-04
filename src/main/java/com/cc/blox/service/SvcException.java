package com.cc.blox.service;

public class SvcException extends RuntimeException {
	private static final long serialVersionUID = 2210643834815186412L;

	private String msgCode;
	
	public SvcException(String msg, String msgCode) {
		super(msg);
		this.msgCode = msgCode;
	}
	
	public String getMsgCode() {
		return this.msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
}
