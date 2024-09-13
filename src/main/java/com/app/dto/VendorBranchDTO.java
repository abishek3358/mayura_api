package com.app.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class VendorBranchDTO {

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

	private String cityName;

	private UUID stateId;

	private String stateName;

	private UUID countryId;

	private String countryName;
}
