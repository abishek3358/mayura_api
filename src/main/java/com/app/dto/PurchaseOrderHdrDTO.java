package com.app.dto;

import java.util.UUID;

public interface PurchaseOrderHdrDTO extends Comparable<String> {
	
	UUID getPurchaseId();

	String getPoNumber();

	String getPoRefNumber();

	UUID getAssociateId();

	UUID getPoStatusId();

	String getOrderPlacedDate();

	String getStatus();

	String getExpectedDeliveryDate();

	String getActualDeliveryDate();

	Double getTotalCost();

	Double getGrandTotal();

	String getVendorName();

	String getCancelledDate();

	Boolean getIsBillCreated();


}
