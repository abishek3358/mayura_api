package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.Invoice;

public interface InvoiceRepository extends WriteableRepository<Invoice, UUID> {

	Optional<Invoice> findByTransactionId(UUID transactionId);

}
