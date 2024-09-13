package com.app.dto;

import java.util.List;
import java.util.UUID;

import com.app.entity.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class WebProductListingDTO {

	private UUID id;
	
	private UUID productId;
	
	private UUID productDtlId;
	
	private String productCode;
	
	private UUID uomId;
	
	private String name="";
	
	private String tag;
	
	private List<String> tagsList;
	
	private String productName="";
	
	private Double price;
	
	private String priceWithUom="";
	
	private String brandName;
	
	private String description;
	
	private String detailedDescription;
	
	private String fileUrl="";
	
	private String fileName="";
	
	private Double discAmount;
	
	private String discValue="";
	
	private String noOfStars;
	
	private boolean discOption=false;
	
	private List<ProductImageDTO> productImageList;
	
	private String favorite;

}
