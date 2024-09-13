package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.StatusMaster;
import com.app.enumeration.StatusType;
import com.app.repository.StatusMasterRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class StatusMasterService {

	private @NonNull StatusMasterRepository statusMasterRepository;

	public List<StatusMaster> findByStatusType(StatusType statusType) {
		return statusMasterRepository.findByStatusType(statusType);
	}

	public void saveOrUpdate(StatusMaster obj) {
		statusMasterRepository.saveAndFlush(obj);
	}

	public Optional<StatusMaster> findById(UUID id) {
		return statusMasterRepository.findById(id);
	}

	public List<StatusMaster> findAll() {
		return statusMasterRepository.findAll();
	}

	public Optional<StatusMaster> findByName(String name) {
		return statusMasterRepository.findByName(name);
	}

}
