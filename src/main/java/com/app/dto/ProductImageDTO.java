package com.app.dto;

import java.util.UUID;

import com.app.enumeration.ProductImageType;

import lombok.Data;

@Data
public class ProductImageDTO {

	private UUID id;	
	
	private UUID productId;	
	
	private ProductImageType productImageType ;
		
	private String fileName;
	
	private String priceWithUom;

	private String fileBase64;
	
	private String fileUrl;

}
