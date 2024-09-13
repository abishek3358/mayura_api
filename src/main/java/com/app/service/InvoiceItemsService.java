package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.InvoiceItems;
import com.app.repository.InvoiceItemsRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class InvoiceItemsService {

	private @NonNull InvoiceItemsRepository invoiceItemsRepository;

	public void saveOrUpdate(InvoiceItems obj) {
		invoiceItemsRepository.saveAndFlush(obj);
	}

	public Optional<InvoiceItems> findById(UUID id) {
		return invoiceItemsRepository.findById(id);
	}

	public List<InvoiceItems> findAll() {
		return invoiceItemsRepository.findAll();
	}
	
	public List<InvoiceItems> findByInvId(UUID invoiceId){
		return invoiceItemsRepository.findByInvId(invoiceId);
	}

}
