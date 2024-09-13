package com.app.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.State;
import com.app.repository.StateRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class StateService {

	private @NonNull StateRepository stateRepository;
	public Optional<State> findById(UUID id) {
		return stateRepository.findById(id);
	}
	public String findNameById(UUID stateId) {		
		return stateRepository.findNameById(stateId);
	}
}
