package com.unitconverter.system.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 8082581541952600304L;
	private String errorMessage;

	public ValidationException() {
		this.errorMessage = "Validation Failed!";
	}
	
	public ValidationException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

}
