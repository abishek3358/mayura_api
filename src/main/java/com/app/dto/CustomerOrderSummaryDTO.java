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
public class CustomerOrderSummaryDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private UUID customerOrderId;
	
	private String orderNo;
	
	private String customerName;
	
	private String mobileNumber;
	
	private String emailId;
	
	private PaymentMode paymentMode;
	
	private String payment;
	
	private Integer numberOfItems;
	
	private Integer orderQty;
	
	private Double deliveryFee;
	
	private Double discAmount;
	
	private Double subTotal;
	
	private Double grandTotal;
	
	private OrderStatus orderStatus;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderPlacedDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date actualDeliveryDate;

	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderAllocatedDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderShippedDate;
	
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderDeliveredDate;
	
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderCompletedDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderCancelledDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	private Date orderRejectedDate;
	
	private CustomerDeliveryAddressDTO deliveryAddressDtoObj;
	
	private List<CustomerOrderItemsDTO> CustomerOrderItemsDTOList;
	
	public String getPayment() {
		if(paymentMode.equals(PaymentMode.CASH_ON_DELIVERY)) {
			payment="CASH ON DELIVERY";
		}
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	
}