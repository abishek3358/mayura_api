package com.app.dto;

import java.util.UUID;

import com.app.enumeration.VendorOrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class VendorOrderMappingDTO {
	
//	 UUID getVendorId();
//	
//	 String getVendorName();
//	
//	 UUID getCustomerId();	
//	
//	 String getCustomerName();
	
	private UUID id;
	
	private UUID vendorId;
	
	private UUID customerId;
	
	private String vendorName;
	
	private String customerName;
	
    private UUID customerOrderId;
	
	private VendorOrderStatus vendorOrderStatus;
	
	private UUID allocatedDate;	
	
	public static void main(String[] args) throws Exception {

		VendorOrderMappingDTO hdr = new VendorOrderMappingDTO();	
		//hdr.setProgramTaskDTOList(Arrays.asList(new ProgramTaskDTO()));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}

}
