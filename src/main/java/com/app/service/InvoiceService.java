package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Invoice;
import com.app.repository.InvoiceRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class InvoiceService {

	private @NonNull InvoiceRepository invoiceRepository;

	public void saveOrUpdate(Invoice obj) {
		invoiceRepository.saveAndFlush(obj);
	}

	public Optional<Invoice> findById(UUID id) {
		return invoiceRepository.findById(id);
	}

	public List<Invoice> findAll() {
		return invoiceRepository.findAll();
	}
	
	public Optional<Invoice> findByTransactionId(UUID transactionId){
		return invoiceRepository.findByTransactionId(transactionId);
	}
	
	

}
