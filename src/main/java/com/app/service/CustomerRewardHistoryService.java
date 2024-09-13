package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.CustomerReward;
import com.app.entity.CustomerRewardHistory;
import com.app.repository.CustomerRewardHistoryRepository;
import com.app.repository.CustomerRewardRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerRewardHistoryService{
	
	private @NonNull CustomerRewardHistoryRepository customerRewardHistoryRepository;
	
	public void saveOrUpdate(CustomerRewardHistory obj) {
		customerRewardHistoryRepository.saveAndFlush(obj);
	}
	
	public Optional<CustomerRewardHistory> findById(UUID id) {
		return customerRewardHistoryRepository.findById(id);
	}
	
	public List<CustomerRewardHistory> findAll() {
		return customerRewardHistoryRepository.findAll();
	}

	public List<CustomerRewardHistory> findByRewardId(UUID id) {
		
		return customerRewardHistoryRepository.findByRewardId(id);
	}

	public List<CustomerRewardHistory> findByCustomerRewardId(UUID id) {
		
		return customerRewardHistoryRepository.findByCustomerRewardId(id);
	}


}
