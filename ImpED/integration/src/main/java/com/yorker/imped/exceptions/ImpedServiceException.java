package com.yorker.imped.exceptions;

import java.text.MessageFormat;

import com.yorker.imped.util.ImpedConstants;
import com.yorker.imped.util.ImpedUtil;

public class ImpedServiceException extends Exception {
	private static final long serialVersionUID = -4164994128995387366L;
	private String errorMessage = null;
	private String errorCode = "Unknown_Exception";
	private String errorResolution = "Unknown_Resolution";
	public ImpedServiceException(String errorCode,Object... message) {
		if(errorCode.startsWith("REF#")){
			this.errorMessage = ImpedUtil.getErrorMessage(message);
			this.errorCode = errorCode;
		} else if(errorCode.startsWith("Test DB Connection Error")){
			this.errorMessage = ImpedUtil.getConnectionErrorMessage(message);
			this.errorCode = errorCode;
		} else if(errorCode.startsWith("Excel sheet error")){
			this.errorMessage = ImpedUtil.getConnectionErrorMessage(message);
			this.errorCode = errorCode;
		} else{
			this.errorMessage = MessageFormat.format(ImpedConstants.ERROR_CODES_RESOL.get(errorCode),message);
			this.errorCode = errorCode;
		}
	}
	public ImpedServiceException(String message, String errorCode,String errorResolution) {
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
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorResolution(String errorResolution) {
		this.errorResolution = errorResolution;
	}

}