package com.app.service;

import java.util.UUID;

import com.app.enumeration.ProductImageType;

public interface ProductImageInterface {
	
	UUID getId();
	
	UUID getProductId();
	
	String getFileName();
	
	ProductImageType getProductImageType();

}
