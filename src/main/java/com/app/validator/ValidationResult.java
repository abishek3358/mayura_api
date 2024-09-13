package com.app.validator;

import java.util.List;

import lombok.Data;

@Data
public class ValidationResult {

	private boolean isValid =true;
	private List<String> errors;
	private Object object;
	public boolean hasErrors() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
