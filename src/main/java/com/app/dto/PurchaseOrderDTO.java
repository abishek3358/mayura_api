package com.app.dto;

import java.util.List;
import java.util.UUID;

import com.app.entity.Bill;
import com.app.entity.BillItems;
import com.app.entity.StatusMaster;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderDTO {
	private String orderPlacedDate;
	private String vendorName;
	private String status;
	private String expectedDeliveryDate;
	private String actualDeliveryDate;
	private String poRefNumber;
	private Double totalCost;
	private UUID purchaseId;
	private String cancelledDate;
	private String poNumber;
	private UUID associateId;
	private UUID poStatusId;
	private StatusMaster statusMaster;
	private Double grandTotal;
	private Bill bill;
	private List<BillItems> billItems;
	private UUID createdBy;
	private Boolean isBillCreated = false;

}
