package com.app.dto;

import java.util.UUID;

public interface SalesOrderHdrDTO {
	UUID getSalesId();

	String getSoNumber();

	String getSoRefNumber();

	String getAssociateName();

	//UUID getSoStatusId();

	String getOrderPlacedDate();

	String getStatus();

	String getCommitedDateOfDelivery();

	//String getActualDeliveryDate();

	Double getTotalCost();
	
	Double getGrandTotal();
	
	String getCancelledDate();

	//String getVendorName();

}
