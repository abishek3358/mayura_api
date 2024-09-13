package com.app.dto;

import java.util.UUID;

public interface OverAllProductDTO {

	UUID getProductId();
    
    UUID getProductCategoryId();
    
    String getProductCategoryName();
    
    UUID getProductBrancdId();
    
    String getBrandName();
	
	String getProductName();
	
	Double getNoOfStars();
	
}
