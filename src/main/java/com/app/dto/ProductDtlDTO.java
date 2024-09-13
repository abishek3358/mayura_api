package com.app.dto;

import java.util.UUID;

import com.app.enumeration.ProductDiscountType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDtlDTO {
	private UUID id;
	private UUID uomId;
	private UUID productId;
	private String uomName;
	private String prize;
	private String fileName;
	private String fileUrl;
	private String priceWithUom;
	private double quantity;
	private double thresholdQuantity;
	private double purchaseRate;
	private double salesRate;
	private ProductDiscountType discountType;
	private double discount;
}
