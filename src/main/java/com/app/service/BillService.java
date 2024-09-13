package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Bill;
import com.app.repository.BillRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class BillService {
	
	private @NonNull BillRepository billRepository;
	
	public void saveOrUpdate(Bill obj) {
		billRepository.saveAndFlush(obj);
	}
	
	public Optional<Bill> findById(UUID id) {
		return billRepository.findById(id);
	}
	
	public List<Bill> findAll() {
		return billRepository.findAll();
	}
	
	public Optional<Bill> getById(String id){
		return billRepository.getById(id);
	}
	
	public Optional<Bill> findByTransactionId(UUID transactionId){
		return billRepository.findByTransactionId(transactionId);
	}
	
	

}
