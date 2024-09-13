package com.app.repository;

import java.util.List;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.SalesInvoiceReceipt;

public interface SalesInvoiceReceiptRepository extends WriteableRepository<SalesInvoiceReceipt, UUID> {
	
	public List<SalesInvoiceReceipt> findByInvId(UUID invId);

}
