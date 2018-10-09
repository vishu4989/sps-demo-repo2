package com.capgemini.validation;

import org.springframework.stereotype.Component;

@Component
public class FriendManagementValidation {
   
	

	String status;
	String errorDescription;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	public FriendManagementValidation() {
		
	}
	
	public FriendManagementValidation(String status, String errorDescription) {
		super();
		this.status = status;
		this.errorDescription = errorDescription;
	}
	
	
	

	
	
	
}
