package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.CustomerFavoriteProductDTO;
import com.app.entity.CustomerFavoriteProduct;
import com.app.repository.CustomerFavoriteProductRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerFavoriteProductService {

	private @NonNull CustomerFavoriteProductRepository customerFavoriteProductRepository;

	@Transactional
	public void saveOrUpdate(CustomerFavoriteProduct obj) throws Exception {
		customerFavoriteProductRepository.save(obj);
	}

	public Optional<CustomerFavoriteProduct> findById(UUID id) {
		return customerFavoriteProductRepository.findById(id);
	}

	public void deleteByFavorite(String customerId, String ProductId) {

		customerFavoriteProductRepository.deleteByFavoriteupdate(customerId, ProductId);

	}


	public List<CustomerFavoriteProductDTO> getAll(String customerId) {
	
		return customerFavoriteProductRepository.getAll(customerId);
	}

}
