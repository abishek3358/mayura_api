package com.app.dto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class VendorViewDTO {

	private UUID id;
	
	private String name;

	private String vendorCode;

	private String phoneNumber;

	private String emailId;

	private String gstTaxNumber;
	
	private Status status ;

	private List<VendorBranchDTO> vendorBranchList;	
	
	private List<VendorAccountDTO> vendorAccountList;
	
	private List<VendorContactPersonDTO> vendorContactPersonList;
	
	public static void main(String[] args) throws Exception {

		VendorViewDTO hdr = new VendorViewDTO();
		hdr.setVendorBranchList(Arrays.asList(new VendorBranchDTO()));
		hdr.setVendorAccountList(Arrays.asList(new VendorAccountDTO()));
		hdr.setVendorContactPersonList(Arrays.asList(new VendorContactPersonDTO()));
		ObjectMapper mapper = new ObjectMapper();
	
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}
}
