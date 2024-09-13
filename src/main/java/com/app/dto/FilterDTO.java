package com.app.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FilterDTO {	
	
	private UUID categoryId;
	
	private String categoryName;
	
	private UUID brandId;
	
	private String brandName;
	
	private Double noOfStars;
	
	
}
