package com.app.dto;

import java.util.Date;
import java.util.UUID;

import com.app.entity.State;
import com.app.enumeration.OfferType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomerOffersOrderDTO {
	
	
	private UUID customerOrderId;
	
	private String customerOrderNo;
	
	private Double discountAmount;
	
	private Double grandTotal;
	
	private OfferType offerType;
	
}
