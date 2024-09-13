package com.app.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.AddressType;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.VendorOrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data

public class AdminViewOrdersDTO {

	private UUID customerOrderId;

	private UUID orderId;

	private UUID productId;

	private String orderNo;
	
	private String customerName;

	private String orderStatus;

	private Double subTotal;

	private Double grandTotal;

	private Double salesRate;

	private Double noOfStars;

	private Double price;

	private Double orderQty;
	
	private String rejectionReason;

	private Double discAmount;

	private Double deliveryFee;

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date orderDate;

	private Integer noOfItem;

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date deliveryDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date allocatedDate;
	
	private UUID vendorId;
	
	private UUID userId;

	private String vendorName;

	private String brandName;

	private String mobileNumber;

	private String uomName;

	private String fileName;

	private String fileUrl;

	private String productName;

	private CustomerDeliveryAddressDTO deliveryAddress;

	private String customerDeliveryAddress;

	private String receiverName;
	
	private String vendorOrderStatus;

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date orderShippedDate;

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date outForDelivery;

	private String deliveryDuration;

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date orderPlacedDate;

	private String location;
	
	private Boolean favorite;
	
	private List<MyOrdersDTO> myOrdersDtoList;
	
	private List<CustomerOrderItemsDTO> CustomerOrderItemsDTOList;
	
	private String paymentStatus;
	
	private Boolean status;

	    public Boolean getStatus() {
	        return status;
	    }

	    public void setStatus(Boolean status) {
	        this.status = status;
	    }
}
