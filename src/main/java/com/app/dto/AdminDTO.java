package com.app.dto;

import java.util.UUID;

import com.app.enumeration.Status;
import com.app.enumeration.UserRoleType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminDTO {
	
	private UUID id;
	
	private String fullName;
	
	private String email;
	
	private String mobileNo;
	
	private UserRoleType userRole;
	
	private Status status;

}
