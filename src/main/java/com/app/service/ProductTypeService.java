package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.ProductTypeDao;
import com.app.dto.ProductTypeResponse;
import com.app.entity.ProductType;
import com.app.repository.ProductTypeRepository;
import com.twilio.rest.api.v2010.Account.Status;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProductTypeService {

	private @NonNull ProductTypeRepository productTypeRepository;
	
	private @NonNull ProductTypeDao productTypeDao;

	public void saveOrUpdate(ProductType obj) {
		productTypeRepository.saveAndFlush(obj);
	}

	public Optional<ProductType> findById(UUID id) {
		return productTypeRepository.findById(id);
	}

	public List<ProductType> findAll() {
		return productTypeRepository.findAll();
	}

	public Optional<ProductType> findByName(String name) {
		return productTypeRepository.findByName(name);
	}
	
	public List<ProductTypeResponse> findByGet() {
		
		return productTypeDao.findByGet();
	}

	public Optional<ProductType> findByNameExcludeId(String name, UUID excludeId) {
		return productTypeRepository.findByNameExcludeId(name, excludeId);
	}



//	public List<ProductType> findByStatus(Status active) {
//
//		return productTypeRepository.findByStatus(active);
//	}
}
