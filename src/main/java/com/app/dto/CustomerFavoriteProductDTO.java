package com.app.dto;

import java.util.UUID;

public interface CustomerFavoriteProductDTO {	

	UUID getCustomerFavoriteProductId();
	
	UUID getId();

	UUID getProductId();
	
	UUID getProductDtlId();

	String getProductName();

	String getFileUrl();
	
	String getProductStack();

	String getProduct();

	String getPrice();
	
	String getFavoriteItem();
	
	String getProductRating();
	
	String getBrandName();
	
	String getTagList();

	String getDescription();

	String getPriceWithUom();

}
