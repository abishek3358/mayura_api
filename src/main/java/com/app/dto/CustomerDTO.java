package com.app.dto;

import java.util.List;
import java.util.UUID;

import com.app.entity.CustomerDeliveryAddress;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomerDTO {
	private UUID id;
	private String fullName;
	private String email;
	private String mobileNo;
	private List<CustomerDeliveryAddress> deliveryAddressList ;
}
