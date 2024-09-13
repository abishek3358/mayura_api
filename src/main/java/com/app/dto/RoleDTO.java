package com.app.dto;

import java.io.Serializable;
import java.util.UUID;

import com.app.enumeration.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private UUID roleId;
	private String roleName;
	
	 
}
