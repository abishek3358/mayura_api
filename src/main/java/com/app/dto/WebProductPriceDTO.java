package com.app.dto;

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
public class WebProductPriceDTO {
	
	private UUID id;
	private UUID uomId;
	private String name;
	private String brandName;
	private String priceDisp;
	private Double price;
	private boolean discOption;
	private Double discAmount;
	private String discValue; 
	private String imageUrl="";
	private String priceWithUom;
}
