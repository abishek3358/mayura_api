package com.app.jpa.interfaces;

import java.util.UUID;

public interface ProductCategoryListDTO {
	
	UUID getId();
	
	String getProductCount();
	
	UUID getCategoryId();
	
	String getFileName();
	
	String getImageFileUrl();
	
	String getCategoryName();
	
	
}
