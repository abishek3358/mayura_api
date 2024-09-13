package com.app.dto;

import java.util.UUID;

import com.app.entity.Role;
import com.app.enumeration.Status;
import com.app.enumeration.UserRoleType;

import lombok.Data;

@Data
public class UserViewDTO {
	

	private UUID userId;
	
	private UserRoleType userRole;

	private String userName;	
	
	private String fullName;

	private String email; 
	
	private String mobileNo;	
	
	private Status status;
	
	private Role roleObj;

}
