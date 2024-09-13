package com.app.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorContactPersonDTO implements Serializable {
	
	private UUID id;
	
	private UUID vendorId;
	
	private String fullName;
	
	private String emailId;
	
	private String phoneNumber;
	
	private String contactPersonRole;
	
	private String markAsDefault;
	
	private Boolean markAsDefaultDb=true;
	
}
