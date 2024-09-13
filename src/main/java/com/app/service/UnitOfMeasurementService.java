package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.UnitOfMeasurementDao;
import com.app.dto.UnitOfMeasurementResponse;
import com.app.entity.UnitOfMeasurement;
import com.app.repository.UnitOfMeasurementRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class UnitOfMeasurementService {

	private @NonNull UnitOfMeasurementRepository repository;
	
	private @NonNull UnitOfMeasurementDao  unitOfMeasurementDao;

	public void saveOrUpdate(UnitOfMeasurement obj) {
		repository.saveAndFlush(obj);
	}

	public Optional<UnitOfMeasurement> findById(UUID id) {
		return repository.findById(id);
	}

	public List<UnitOfMeasurement> findAll() {
		return repository.findAll();
	}

	public Optional<UnitOfMeasurement> findByName(String name) {
		return repository.findByName(name);
	}

	public Optional<UnitOfMeasurement> findByNameExcludeId(String name, UUID excludeId) {
		return repository.findByNameExcludeId(name, excludeId);
	}
	
	public List<UnitOfMeasurementResponse> findByGetAll() {
		// TODO Auto-generated method stub
		return unitOfMeasurementDao.findByGetAll();
	}


	public void deleteById(UUID vendorId) {
		repository.deleteById(vendorId);
	}

	
	
//	public List<UnitOfMeasurement> findByStatus(Status active) {
//		return  repository.findByStatus(active);
//	}

}
