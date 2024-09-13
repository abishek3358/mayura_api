package com.app.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class AllProductDTO {

	private UUID Id;
	
	private UUID productDtlId;
	
	private UUID uomId;

	private String productName;

	private Double noOfStars;
	
	private String priceWithUom;

	private String fileName;
	
	private String fileUrl;
	
	private Double price;
	
	private String uomName;
	
	private Boolean favorite;	
	
	private String brandName;
	
	private String description;
	
	private String detailedDescription;
	
	private Double discAmount;
	
	private String discValue;
	
	private boolean discOption=false;
	
	private List<ProductImageDTO> productImageList;
	
	private UUID productId;
	
	private String productCode;
	
	private String name="";
	
	private String tag;
	
	private List<String> tagsList;
	
}
