package com.app.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderStatusUpdateDTO {

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date estimatedDeliveryDate;
	
//	@JsonFormat(pattern = "dd/MM/yyyy")
//	private Date actualDeliveryDate;
	
	private String notes; 
	
	private String reviews;
	
	private Double noOfStars;
	
}
