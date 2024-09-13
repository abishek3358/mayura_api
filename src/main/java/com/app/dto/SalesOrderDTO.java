package com.app.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.app.entity.Invoice;
import com.app.entity.InvoiceItems;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String commitedDateOfDelivery;
	private String associateName;
	private String soNumber;
	private String soRefNumber;
	private String salesOrderNumber;
	private String salesRefNumber;
	private String orderDate;
	private UUID salesId;
	private String orderPlacedDate;
	private String cancelledDate;
	private Double totalCost;
	private String status;
	private Double grandTotal;
	private UUID id;
	private Invoice invoice;
	private List<InvoiceItems> invoiceItemslist;
	private UUID createdBy;
	private Boolean isInvoiceCreated; 

}
