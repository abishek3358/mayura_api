package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.SalesInvoiceReceipt;
import com.app.repository.SalesInvoiceReceiptRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class SalesInvoiceReceiptService {

	private @NonNull SalesInvoiceReceiptRepository invoiceReceiptRepository;

	public void saveOrUpdate(SalesInvoiceReceipt obj) {
		invoiceReceiptRepository.saveAndFlush(obj);
	}

	public Optional<SalesInvoiceReceipt> findById(UUID id) {
		return invoiceReceiptRepository.findById(id);
	}

	public List<SalesInvoiceReceipt> findAll() {
		return invoiceReceiptRepository.findAll();
	}

	public void save(SalesInvoiceReceipt obj) {
		invoiceReceiptRepository.save(obj);
	}
	
	public List<SalesInvoiceReceipt> findByInvId(UUID invId){
		return invoiceReceiptRepository.findByInvId(invId);
	}

}
