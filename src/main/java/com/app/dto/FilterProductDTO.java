package com.app.dto;

import java.util.UUID;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductDTO {

	private List<FilterProduct> ratingList;
	private List<FilterProduct> productCategoryList;
	private List<FilterProduct> tagList;
	private List<FilterProduct> brandList;
	private Boolean categoryFlag = true;
	private Boolean brandFlag = true;

	@Getter
	@Setter
	public static class FilterProduct {

		private UUID id;
		private String name;
		private Integer itemCount;

	}

}
