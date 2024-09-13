package com.app.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequestDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private UUID productDtlId;
	private UUID uomId;
	private Double orderQty;
	private Double discAmount;
	private Double price;
	private Double orderQuantityAmount;
}