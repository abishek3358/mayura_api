package com.app.dto;

import java.util.List;
import java.util.UUID;

import com.app.dto.WebProductListingDTO.WebProductListingDTOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class BrandDTO {
	
	private UUID id;
	
	private String brandName;
	
	private UUID productId;
	
	private Double salesRate;
	
	private String uomName;
	
	private String productName;
	
	private String priceWithUom;
	
	private String description;
	
	private String productCount;
	
	private String fileName;
	
	private String noOfStars;
	
	private String fileUrl;
	
	private List<AdminViewOrdersDTO> productDtoList;

	
	
}
