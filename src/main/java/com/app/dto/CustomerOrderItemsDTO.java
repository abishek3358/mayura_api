package com.app.dto;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderItemsDTO {

	private String imageUrl;
	private String brandName;
	private String productName;
	private String productCategoryName;
	private Double orderQty;
	private Double price;
	private Double orderQtyAmount;
	private Double discAmount;
	private String priceWithUom;
	private UUID uomId;
	private String dispPrice;
}
