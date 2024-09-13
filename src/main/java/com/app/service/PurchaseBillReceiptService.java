package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.PurchaseBillReceipt;
import com.app.repository.PurchaseBillReceiptRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class PurchaseBillReceiptService {

	private @NonNull PurchaseBillReceiptRepository billReceiptRepository;

	public void saveOrUpdate(PurchaseBillReceipt obj) {
		billReceiptRepository.saveAndFlush(obj);
	}

	public Optional<PurchaseBillReceipt> findById(UUID id) {
		return billReceiptRepository.findById(id);
	}

	public List<PurchaseBillReceipt> findAll() {
		return billReceiptRepository.findAll();
	}

	public void save(PurchaseBillReceipt obj) {
		billReceiptRepository.save(obj);
	}
	
	public List<PurchaseBillReceipt> findByBillId(UUID billId){
		return billReceiptRepository.findByBillId(billId);
	}

}
