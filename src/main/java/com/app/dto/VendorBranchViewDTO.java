package com.app.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class VendorBranchViewDTO {
	
	private UUID id;
	
	private UUID vendorId;
	
	private String branchName;	
	
	private String branchCode;
	
	private String location;
	
	private Double latitude;
	
	private Double longitude;
	
	private String address1;

	private String address2;
	
	private String postalCode;

	private UUID cityId;

	private UUID stateId;
	
	private UUID countryId;	 
	 
}
	
