package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.SalesOrderDTL;
import com.app.repository.SalesOrderDTLRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class SalesOrderDTLService {

	private @NonNull SalesOrderDTLRepository salesOrderDTLRepository;

	public void saveOrUpdate(SalesOrderDTL obj) {
		salesOrderDTLRepository.saveAndFlush(obj);
	}

	public Optional<SalesOrderDTL> findById(UUID id) {
		return salesOrderDTLRepository.findById(id);
	}

	public void save(SalesOrderDTL obj) {
		salesOrderDTLRepository.save(obj);
	}

	public List<SalesOrderDTL> findAll() {
		return salesOrderDTLRepository.findAll();
	}
	
	public void deleteById(UUID id) {
		salesOrderDTLRepository.deleteById(id);
	}

}
