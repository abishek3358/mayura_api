package com.app.dto;

import com.app.enumeration.OrderStatus;
import com.app.enumeration.Status;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AllOrdersDTO {
	
	private OrderStatus orderStatus;

	private Status status=Status.ACTIVE;
}
