package com.app.dto;

import java.util.UUID;

import com.app.enumeration.PayoutPeriodType;

import lombok.Data;

@Data
public class VendorAccountDTO {
	
	private UUID id;
	
	private UUID vendorId;	
	
	private Double commissionPercentage;
	
	private PayoutPeriodType payoutPeriod;
}

