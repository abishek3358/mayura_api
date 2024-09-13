package com.app.dto;

import java.util.UUID;


import com.app.enumeration.ProductDiscountType;

public interface ProductViewDTO {
    
	UUID getId();
	
	UUID getProductId();
	
	UUID getCategoryId();
	
	UUID getProductDtlId();
	
	UUID getUomId();
	
	String getProductName();
	
	 Double getPrice();
	
	 String getUomName();
	 
	 String getPriceWithUom();
	
	 String getFileName();
	 
	 String getBrandName();
	 
	 String getImageFileUrl();
	
	 String getNoOfStars();

	 ProductDiscountType getDiscType();
		
	Double getProductDiscount();
	
	String getfavorite();

}
