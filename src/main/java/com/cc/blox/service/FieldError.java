package com.cc.blox.service;

import java.io.Serializable;

public class FieldError implements Serializable {
	private static final long serialVersionUID = 4412233559653772755L;

	private String field;
	private String code;
	private String defaultMessage;
	
	public FieldError(String field, String code, String defaultMessage) {
		this.field = field;
		this.code = code;
		this.defaultMessage = defaultMessage;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDefaultMessage() {
		return defaultMessage;
	}
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
}
