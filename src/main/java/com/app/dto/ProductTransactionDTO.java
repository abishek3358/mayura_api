package com.app.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTransactionDTO {
	private UUID id;
	private String transRef;
	private String transSrc;
	private String transDate;
	private Double transQty;
	private String productName;

}
