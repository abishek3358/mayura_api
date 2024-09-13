package com.app.repository;

import java.util.List;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.PurchaseBillReceipt;

public interface PurchaseBillReceiptRepository extends WriteableRepository<PurchaseBillReceipt, UUID> {
	
	public List<PurchaseBillReceipt> findByBillId(UUID billId);

}
