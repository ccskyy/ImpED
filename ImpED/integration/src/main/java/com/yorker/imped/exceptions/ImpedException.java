package com.yorker.imped.exceptions;

public class ImpedException extends Exception {
	private static final long serialVersionUID = -4164994128995387366L;
	private String errorCode = "Unknown_Exception";
	private String errorResolution = "Unknown_Resolution";
	public ImpedException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	public ImpedException(String message, String errorCode,String errorResolution) {
		super(message);
		this.errorCode = errorCode;
		this.errorResolution = errorResolution;
	}

	public String getErrorCode() {
		return this.errorCode;
	}
	
	public String getErrorResolution() {
		return this.errorResolution;
	}

}