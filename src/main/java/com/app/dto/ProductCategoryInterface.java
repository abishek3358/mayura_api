package com.app.dto;

import java.util.UUID;

import com.app.enumeration.ProductDiscountType;
import com.app.enumeration.Status;

import lombok.Data;
@Data
public class ProductCategoryInterface {
	
	private UUID id;
	
	private UUID categoryId;
	
	private String productCount;
	
	private String fileName;
	
	private String imageFileUrl;
	
	private String categoryName;
	
	private Status status;
	
	
}
