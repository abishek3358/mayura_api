package com.app.dto;

import java.util.UUID;

public interface ProductDetailsDTO {
	UUID getId();
	
	UUID getProductUomId();
	
	String getUomName();
	
	Double getPurchaseRatePerUnit();
	
	Double getSalesRatePerUnit();
	
	String getGstPercentage();
	
	Double getAvailableQuantity();
	
	

}
