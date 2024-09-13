package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.AccountGroup;
import com.app.repository.AccountGroupRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class AccountGroupService {
	
	private @NonNull AccountGroupRepository accountGroupRepository;
	
	public void saveOrUpdate(AccountGroup obj) {
		accountGroupRepository.saveAndFlush(obj);
	}
	
	public Optional<AccountGroup> findById(UUID id) {
		return accountGroupRepository.findById(id);
	}
	
	public List<AccountGroup> findAll() {
		return accountGroupRepository.findAll();
	}
	
	public Optional<AccountGroup> findByName(String name) {
		return accountGroupRepository.findByName(name);
	}
	
	public Optional<AccountGroup> findByNameExcludeId(String name,UUID excludeId) {
		return accountGroupRepository.findByNameExcludeId(name,excludeId);
	}

}
