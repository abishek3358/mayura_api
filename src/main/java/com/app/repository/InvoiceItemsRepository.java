package com.app.repository;

import java.util.List;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.InvoiceItems;

public interface InvoiceItemsRepository extends WriteableRepository<InvoiceItems, UUID> {
	public List<InvoiceItems> findByInvId(UUID invoiceId);

}
