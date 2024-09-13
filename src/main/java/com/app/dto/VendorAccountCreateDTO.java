package com.app.dto;

import com.app.enumeration.PayoutPeriodType;

import lombok.Data;

@Data
public class VendorAccountCreateDTO {
	
	private Double commissionPercentage;
	
	private PayoutPeriodType payoutPeriod;
}

