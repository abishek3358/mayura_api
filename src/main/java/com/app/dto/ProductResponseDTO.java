package com.app.dto;

import java.util.List;
import java.util.UUID;

import com.app.entity.ProductImage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductResponseDTO {

	private UUID id;
	
	private String productCode;
	
	private String productName;
	
	private UUID productCategoryId;
	
	private String productCategoryName;
	
	private Integer productValidityInDays;
	
	private UUID brandId;
	
	private String brandName;
	
	private String hsCode;
	
	private String tag;
	
	private List<String> tagsList;
	
	private String detailedDescription;
	
	private String description;	
	
	private String noOfStars;
	
	private List<ProductDtlDTO> productDtlList;
	
	private List<ProductImageDTO> productImageList;


	
}
