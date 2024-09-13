package com.app.dto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class VendorDTO {

	private UUID id;

	private String name;

	private String vendorCode;

	private String phoneNumber;

	private String emailId;

	private String gstTaxNumber;

	private List<VendorContactPersonCreateDTO> vendorContactPersonList;

	private List<VendorBranchCreateDTO> vendorBranchList;

	private List<VendorAccountCreateDTO> vendorAccountList;
	
	public static void main(String[] args) throws Exception {

		VendorDTO hdr = new VendorDTO();	
		hdr.setVendorContactPersonList(Arrays.asList(new VendorContactPersonCreateDTO()));
		hdr.setVendorBranchList(Arrays.asList(new VendorBranchCreateDTO()));
		hdr.setVendorAccountList(Arrays.asList(new VendorAccountCreateDTO()));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}

	

}
