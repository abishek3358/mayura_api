package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.AssociateInfo;
import com.app.repository.AssociateInfoRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class AssociateInfoService {
	
	private @NonNull AssociateInfoRepository associateInfoRepository;
	
	public void saveOrUpdate(AssociateInfo obj) {
		associateInfoRepository.saveAndFlush(obj);
	}
	
	public Optional<AssociateInfo> findById(UUID id) {
		return associateInfoRepository.findById(id);
	}
	
	public List<AssociateInfo> findAll() {
		return associateInfoRepository.findAll();
	}
	
	public Optional<AssociateInfo> findByName(String name) {
		return associateInfoRepository.findByName(name);
	}
	
	public Optional<AssociateInfo> findByNameExcludeId(String name, UUID excludeId) {
		return associateInfoRepository.findByNameExcludeId(name,excludeId);
	}

}
