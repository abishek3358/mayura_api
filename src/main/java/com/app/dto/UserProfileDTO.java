package com.app.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserProfileDTO {
	
	private UUID userId;
	
	private String fullName;
	
	private String email;
	
	private String mobileNo;
	
	private String fileName;
	
	private String fileUrl;
	
	private String fileBase64;
	
	private List<ProfileMobileDTO> mobileNoList;

}
