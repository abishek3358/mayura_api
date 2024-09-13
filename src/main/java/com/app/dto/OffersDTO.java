package com.app.dto;

import java.util.Date;
import java.util.UUID;

import com.app.entity.State;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OffersDTO {
	
	private UUID id;
	
	private String OfferName;
	
	private String offerCode;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date expiryDate;
	
	private String details;
	
	
	private Boolean flag=true;
	
	
	
	
	
}
