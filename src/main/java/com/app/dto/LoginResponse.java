package com.app.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UUID userId;
	
	private String userName;
	
	private String fullName;
	
	private String email;
	
	private String phone;
	
	private RoleDTO roleObj	;
	
	private Boolean forcePasswordChange;
	
	private UUID referenceId;

	public void setUserRoleId(UUID userRollId) {
		// TODO Auto-generated method stub
		
	}
	
}
