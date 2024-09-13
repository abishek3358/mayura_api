package com.app.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.OrderStatus;
import com.app.enumeration.PaymentMode;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerOrderDetailsDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	private UUID customerOrderId;
	private String orderNo;
	private UUID customerId;
	private String customerName;
	private String mobileNumber;
	private String emailId;
	private Double deliveryFee;
	private Double discAmount;
	private Double subTotal;
	private Double grandTotal;
	private PaymentMode paymentMode ;
	private OrderStatus orderStatus;
	private Integer noOfProducts;
	private String fileName;
	private String productName;
	private Double Quantity;
	private String brandName;
	private String productAmount;
	private String location;

	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date deliveryDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date estimateDate;
	
	
	private CustomerDeliveryAddressDTO deliveryAddressDtoObj;
	
	List<VendorDtlDTO> vendorDtlList;
	
}