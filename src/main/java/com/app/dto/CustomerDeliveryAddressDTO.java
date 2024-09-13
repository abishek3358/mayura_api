package com.app.dto;

import java.util.UUID;

import com.app.enumeration.AddressType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDeliveryAddressDTO {
	
	private UUID id;
	
	private AddressType addressType;
	
	private String receiverName;
	
	private String mobileNo;	
	
	private String addressLine1;
	
	private String addressLine2;
	 
	private String postalCode;
	
	private String landMark;
	
	private String cityName;
	
	private String stateName;
	
	private String countryName;
	
	private String location;
	
	private Double latitude;
	
	private Double longitude;
	
	public static void main(String[] args) throws Exception {

		CustomerDeliveryAddressDTO hdr = new CustomerDeliveryAddressDTO();		
		ObjectMapper mapper = new ObjectMapper();
	
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}
	
}
