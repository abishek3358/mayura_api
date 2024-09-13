package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Warehouse;
import com.app.repository.WarehouseRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class WarehouseService {

	private @NonNull WarehouseRepository tradingWarehouseRepository;

	public void saveOrUpdate(Warehouse obj) {
		tradingWarehouseRepository.saveAndFlush(obj);
	}

	public Optional<Warehouse> findById(UUID id) {
		return tradingWarehouseRepository.findById(id);
	}

	public List<Warehouse> findAll() {
		return tradingWarehouseRepository.findAll();
	}

	public Optional<Warehouse> findByName(String name) {
		return tradingWarehouseRepository.findByName(name);
	}

	public Optional<Warehouse> findByNameExcludeId(String name, UUID excludeId) {
		return tradingWarehouseRepository.findByNameExcludeId(name, excludeId);
	}

}
