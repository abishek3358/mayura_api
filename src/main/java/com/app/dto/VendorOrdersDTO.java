package com.app.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class VendorOrdersDTO {

	private UUID customerOrderId;                                                                                                                                                                 ;

	private String customerOrderNo;
	
	private String customerName;

	private Double grantTotal;

	private Integer noOfItems;

    private String rejectionReason;
    
    private String vendorOrderStatus;
    
    private UUID vendorId ;
    
    private UUID userId ;

	@JsonFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date allocatedDate;
	
	private String deliveryAddress;
	
	private String paymentStatus;
	
	private List<CustomerOrderItemsDTO> CustomerOrderItemsDTOList;
	
}
