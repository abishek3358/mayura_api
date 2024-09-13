package com.app.dto;

import java.util.UUID;

public class ProductDetailDTO {
	private UUID id;
	private UUID uomId;
	private String uomName;
	private Double PurchaseRatePerUnit;
	private Double SalesRatePerUnit;
	private String GstPercentage;
	private Double AvailableQuantity;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUomId() {
		return uomId;
	}

	public void setUomId(UUID uomId) {
		this.uomId = uomId;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public Double getPurchaseRatePerUnit() {
		return PurchaseRatePerUnit;
	}

	public void setPurchaseRatePerUnit(Double purchaseRatePerUnit) {
		PurchaseRatePerUnit = purchaseRatePerUnit;
	}

	public Double getSalesRatePerUnit() {
		return SalesRatePerUnit;
	}

	public void setSalesRatePerUnit(Double salesRatePerUnit) {
		SalesRatePerUnit = salesRatePerUnit;
	}

	public String getGstPercentage() {
		return GstPercentage;
	}

	public void setGstPercentage(String gstPercentage) {
		GstPercentage = gstPercentage;
	}

	public Double getAvailableQuantity() {
		return AvailableQuantity;
	}

	public void setAvailableQuantity(Double availableQuantity) {
		AvailableQuantity = availableQuantity;
	}

}
