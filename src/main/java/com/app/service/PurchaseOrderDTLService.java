package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.PurchaseOrderDTL;
import com.app.repository.PurchaseOrderDTLRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class PurchaseOrderDTLService {

	private @NonNull PurchaseOrderDTLRepository tradingPurchaseOrderDTLRepository;

	public void saveOrUpdate(PurchaseOrderDTL obj) {
		tradingPurchaseOrderDTLRepository.saveAndFlush(obj);
	}

	public Optional<PurchaseOrderDTL> findById(UUID id) {
		return tradingPurchaseOrderDTLRepository.findById(id);
	}

	public List<PurchaseOrderDTL> findAll() {
		return tradingPurchaseOrderDTLRepository.findAll();
	}
	
	public void save(PurchaseOrderDTL obj) {
		tradingPurchaseOrderDTLRepository.save(obj);
	}
	
	public void deleteById(UUID id) {
		tradingPurchaseOrderDTLRepository.deleteById(id);
	}
	

}
