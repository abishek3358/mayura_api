package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.AssociateContact;
import com.app.enumeration.Status;
import com.app.repository.AssociateContactRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class AssociateContactService {
	
	private @NonNull AssociateContactRepository associateContactRepository;
	
	public void saveOrUpdate(AssociateContact obj) {
		associateContactRepository.saveAndFlush(obj);
	}
	
	public Optional<AssociateContact> findById(UUID id) {
		return associateContactRepository.findById(id);
	}
	
	public List<AssociateContact> findAll() {
		return associateContactRepository.findAll();
	}
	
	public List<AssociateContact> findByAssociateId(UUID associateId) {
		return associateContactRepository.findByAssociateId(associateId);
	}
	
	public Optional<AssociateContact> findByName(String name) {
		return associateContactRepository.findByName(name);
	}
	
	public Optional<AssociateContact> findByNameExcludeId(String name, UUID excludeId) {
		return associateContactRepository.findByNameExcludeId(name,excludeId);
	}
	
	public List<AssociateContact> findByStatusAndAssociateId(Status status,UUID associateId){
		return associateContactRepository.findByStatusAndAssociateId(status, associateId);
	}

}
