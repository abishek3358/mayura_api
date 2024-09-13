package com.app.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.OfferType;
import com.app.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class OfferDetailsDTO {

	private UUID id;

	private String offerName;

	private String offerCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date startDateAndTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date expiryDateAndTime;

	private OfferType offerType;

	private Double offerValue;

	private Double minimumOrderAmount;

	private String termsAndConditions;
	
	
	public static void main(String[] args) throws Exception {

		OfferDetailsDTO hdr = new OfferDetailsDTO();	
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}


	
}
