package com.app.dto;

import java.util.UUID;

import com.app.enumeration.MobileType;

import lombok.Data;

@Data
public class ProfileMobileDTO {	
	
	private UUID id;
	
	private MobileType title;
	
	private String mobileNo;
	
}
