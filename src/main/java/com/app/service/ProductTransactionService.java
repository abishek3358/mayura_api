package com.app.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.ProductTransactionResponseDTO;
import com.app.entity.ProductTransaction;
import com.app.repository.ProductTransactionRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProductTransactionService {
	
private @NonNull ProductTransactionRepository tradingProductTransactionRepository;
	
	@Transactional
	public void saveOrUpdate(ProductTransaction obj) {
		tradingProductTransactionRepository.saveAndFlush(obj);
	}
	
	public Optional<ProductTransaction> findById(UUID id) {
		return tradingProductTransactionRepository.findById(id);
	}
	
	public List<ProductTransaction> findAll() {
		return tradingProductTransactionRepository.findAll();
	}
	
	public List<ProductTransactionResponseDTO> getproductTransByFromAndToDate( Date fromDate, Date toDate){
		return tradingProductTransactionRepository.getproductTransByFromAndToDate(fromDate, toDate);
	}
	
	public List<ProductTransactionResponseDTO> getproductTransByFromToDateAndProductId(Date fromDate, Date toDate,UUID productId){
		return tradingProductTransactionRepository.getproductTransByFromToDateAndProductId(fromDate, toDate, productId.toString());
	}
	
}



