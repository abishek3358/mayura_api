package com.app.dto;

import java.util.UUID;

import com.app.enumeration.AddressType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDeliveryAddressViewDTO {
	
	private UUID id;
	
	private AddressType addressType;
	
	private String receiverName;
	
	private String mobileNo;	
	
	private String addressLine1;
	
	private String addressLine2;
	 
	private String postalCode;
	
	private String landMark;
	
	private UUID cityId;
	
	private String cityName;
	
	private UUID stateId;
	
	private String stateName;
	
	private UUID countryId;
	
	private String countryName;
	
	private String location;
	
	private Double latitude;
	
	private Double longitude;
	
	public static void main(String[] args) throws Exception {

		CustomerDeliveryAddressViewDTO hdr = new CustomerDeliveryAddressViewDTO();		
		ObjectMapper mapper = new ObjectMapper();
	
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}
	
}
