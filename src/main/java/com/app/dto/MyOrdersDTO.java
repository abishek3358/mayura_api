package com.app.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.AddressType;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.VendorOrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MyOrdersDTO {
	
//	private UUID customerOrderId;

//	private UUID orderId;
	
	private UUID productId;
	
	private String orderNo;	
//	
//	private String customerName;
	
//	private Double subTotal;
//	
//	private Double grandTotal;
//	
	private Double salesRate;
	
	private Double orderQty;
	
//	private Double discAmount;
//	
//	private Double deliveryFee;
//	
//	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
//	private Date orderDate;
//	
//	private Integer noOfItem;
//	
//	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
//	private Date allocatedDate;
//	
//	private String vendorName;
	
	private String brandName;
	
	private String uomName;
	
	private String fileName;
	
	private String fileUrl;
//	
//	private String priceWithUom;
	
	private String productName;
	
	private String orderStatus;
//	
//	private CustomerDeliveryAddressDTO deliveryAddress;
//	
//	private String customerDeliveryAddress; 
//	
//	private VendorOrderStatus vendorOrderStatus;
//	
//	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
//	private Date outForDelivery;
//	
//	private String deliveryDuration;
//	
//	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
//	private Date orderPlacedDate;
//	
//	private String location; 
//	
	private List<MyOrdersDTO> myOrdersDtoList;
}
