package com.app.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Discount;
import com.app.entity.Product;
import com.app.repository.DiscountRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class DiscountService {
	
	private @NonNull DiscountRepository discountRepository;
	
	@Transactional()
	public void saveOrUpdate(Discount obj) throws Exception {
		
		discountRepository.saveAndFlush(obj);
		
	}
	public Optional<Discount> findById(UUID id) {
		
		return discountRepository.findById(id);
	}
	
	public Optional<Discount> findByDiscountCode(String code) {
		return discountRepository.findByDiscountCode(code);
	}
	

}
