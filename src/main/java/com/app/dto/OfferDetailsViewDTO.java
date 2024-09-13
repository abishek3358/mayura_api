package com.app.dto;

import java.util.Date;
import java.util.UUID;

import com.app.enumeration.OfferType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class OfferDetailsViewDTO {

	private UUID id;

	private String offerName;

	private String offerCode;	

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date expiryDateAndTime;

	private String termsAndConditions;	
	
	private Double offerValue;
	
	private Double minimumOrderAmount;
	
}
