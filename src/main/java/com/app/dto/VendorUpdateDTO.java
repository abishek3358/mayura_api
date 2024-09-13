package com.app.dto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class VendorUpdateDTO {

	private UUID id;

	private String name;

	private String vendorCode;

	private String phoneNumber;

	private String emailId;

	private String gstTaxNumber;
	
	private Status status ;
	
}
