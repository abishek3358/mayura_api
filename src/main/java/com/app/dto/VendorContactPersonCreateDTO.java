package com.app.dto;

import lombok.Data;

@Data
public class VendorContactPersonCreateDTO {
	
	private String fullName;
	
	private String emailId;
	
	private String phoneNumber;
	
	private String contactPersonRole;
	
	private String markAsDefault;
	
}
