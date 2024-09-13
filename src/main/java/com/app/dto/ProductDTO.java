package com.app.dto;

import java.util.UUID;

import com.app.enumeration.ProductDiscountType;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface ProductDTO {
	
	UUID getId();

	String getProductName();
	
	UUID getProductId();
	
	String getProductCode();
	
	String getTags();
	
	String getUomName();
	
	String getBrandName();
	
	String getDescription();
	
	String getDetailedDescription();
	
	UUID getProductDtlId();
	
	UUID getUomId();
	
	Double getSalesRate();
	
	String getNoOfStars();
	
	String getProductCount();
	
	String getFileName();
	
	String getFileUrl();	
	
	String getCategoryName();
	
	ProductDiscountType getProductDiscType();
	
	Double getProductDiscount();
	
	String getfavorite();	
	

	
}
