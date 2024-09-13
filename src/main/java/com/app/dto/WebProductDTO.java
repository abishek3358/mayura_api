package com.app.dto;

import java.util.List;
import java.util.UUID;

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
public class WebProductDTO {
	
	private UUID id;
	private String name;
	private String description;
	private String detailedDescription;
	private String deliveryType;
	private Double rating;
	private String brandName;
	private String ratingText;
	private List<String> images;
	private List<WebProductPriceDTO> pricingList;

}
