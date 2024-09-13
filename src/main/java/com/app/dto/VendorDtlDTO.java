package com.app.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class VendorDtlDTO {

	private UUID id;
	
	private String vendorName;
	
	private String vendorCode;
	
	private String branchAddress;
	
	private String ratings;
	
	private String handledOrders;
}
