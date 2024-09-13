package com.app.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.OrderStatus;
import com.app.enumeration.PaymentMode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private UUID deliveryAddressId;
	
	private PaymentMode paymentMode;
	
	private OrderStatus orderStatus;
	
	private Double deliveryFee;
	
	private Double discAmount;
	
	private Double subTotal;

	private Double grandTotal;
	
	private List<OrderItemRequestDTO> customerOrderItemsList;
	
	public static void main(String[] args) throws Exception {

		OrderRequestDTO hdr = new OrderRequestDTO();	
		hdr.setCustomerOrderItemsList(Arrays.asList(new OrderItemRequestDTO()));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}

}