package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.BillItems;
import com.app.repository.BillItemsRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class BillItemsService {
	
	private @NonNull BillItemsRepository billItemsRepository;
	
	public void saveOrUpdate(BillItems obj) {
		billItemsRepository.saveAndFlush(obj);
	}
	
	public Optional<BillItems> findById(UUID id) {
		return billItemsRepository.findById(id);
	}
	
	public List<BillItems> findAll() {
		return billItemsRepository.findAll();
	}
	
	public List<BillItems> findByBillId(UUID id){
		return billItemsRepository.findByBillId(id);
	}

}
