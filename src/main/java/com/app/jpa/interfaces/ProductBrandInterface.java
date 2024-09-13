package com.app.jpa.interfaces;

import java.util.UUID;

public interface ProductBrandInterface {
	
	UUID getId();
	
	UUID getBrandId();
	
	UUID getProductId();
	
	String getProductCount();
	
	String getDescription();
	
	String getFileName();
	
	String getProductName();
	
	String getUomName();
	
	Double getSalesRate();
	
	String getNoOfStars();
	
	String getImageFileUrl();
	
	String getBrandName();
	
	
}
