package com.app.dto;

import java.util.UUID;

import com.app.enumeration.ProductDiscountType;

public interface OverAllProductDtlDTO {
	
    UUID getProductDtlId();
	
	UUID getUomId();
	
	String getUomName();
	
	Double getSaleRate();
	
	
	
	ProductDiscountType getProductDiscountType();
	
	Double getDiscountAmount();
	
	
	
	

	
	
	
	
}
