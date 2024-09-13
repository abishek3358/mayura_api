package com.app.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {

	private String code;
	private String reason;
	private List<String> errorList;
	 
	@Override
	public String toString() {
		return "Error [code=" + code + ", Reason=" + reason + "]";
	}
	
	
}
