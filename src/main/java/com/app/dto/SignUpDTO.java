package com.app.dto;

import java.io.Serializable;
import java.util.UUID;

import com.app.entity.Role;
import com.app.enumeration.Status;
import com.app.enumeration.UserRoleType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class SignUpDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    private UUID userId; 
    
    private UUID vendorId; 
    
  	private String fullName;

	private String userName;

	private String email;

	private String mobileNo;
	
	private String secondaryMobileNo;

	private String password;

	private String confirmPassword; 
	
	private UserRoleType userRole;
	
	//private Role roleObj;
	
	private Status status;
	
	

}


