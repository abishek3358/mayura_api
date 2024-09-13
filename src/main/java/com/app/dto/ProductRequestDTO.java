package com.app.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.SortingType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class ProductRequestDTO implements Serializable {


	private static final long serialVersionUID = 1L;

	private String searchKey;
	
	private UUID categoryId;
	
	private UUID brandId;
	
	private Double maxPrice;
	
	private Double minPrice;

	private List<Double> ratingList;
	
	private List<String> tagList;
	
	private List<UUID> brandList;
	
	private List<UUID> categoryList;

	private Integer showItemsCount;
	
	private SortingType sortByType;



	
}
