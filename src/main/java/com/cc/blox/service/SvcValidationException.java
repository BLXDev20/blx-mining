package com.cc.blox.service;

import java.util.ArrayList;
import java.util.List;
 
 

public class SvcValidationException extends SvcException {
private static final long serialVersionUID = 2380029215536260871L;
	
	private static final String EXCEPTION_MSG_TEXT = "Field validation errors";
	private static final String EXCEPTION_MSG_CODE = "FLD.ERRORS";
	
	private List<FieldError> fieldErrors;
	
	public SvcValidationException(List<FieldError> fieldErrors) {
		super(EXCEPTION_MSG_TEXT, EXCEPTION_MSG_CODE);
		this.fieldErrors = fieldErrors;
	}
	
	public SvcValidationException(FieldError fieldError) {
		super(EXCEPTION_MSG_TEXT, EXCEPTION_MSG_CODE);
		this.fieldErrors = new ArrayList<FieldError>();
		this.fieldErrors.add(fieldError);
	}

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	
	public String getConcatenatedErrorText() {
		StringBuilder sb = new StringBuilder();

		if (fieldErrors != null && !fieldErrors.isEmpty()) {
			for (FieldError fe : fieldErrors) {
				sb.append(fe.getField());
				sb.append(": ");
				sb.append(fe.getDefaultMessage());
				if (fieldErrors.indexOf(fe) < fieldErrors.size() - 1) {
					sb.append(" | ");
				}
			}
		}
		return sb.toString();
	}
}
