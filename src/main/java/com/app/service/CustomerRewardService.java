package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.CustomerReward;
import com.app.entity.CustomerRewardHistory;
import com.app.enumeration.TransactionType;
import com.app.repository.CustomerRewardRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerRewardService {

	private @NonNull CustomerRewardRepository customerRewardRepositoy;

	private @NonNull CustomerRewardHistoryService customerRewardHistoryService;

	public void saveOrUpdate(CustomerReward obj) {

		Optional<CustomerReward> customerRewardOptional = customerRewardRepositoy.findByCustomerId(obj.getCustomerId());
		Double activeRewards = 0.0;
		Double usedRewards = 0.0;
		Double lockedRewards = 0.0;
		
		CustomerReward customerRewardObj = null;
		if (customerRewardOptional.isPresent()) {
			customerRewardObj = customerRewardOptional.get();
			activeRewards = customerRewardObj.getActiveRewards();
			usedRewards = customerRewardObj.getUsedRewards();
		}

		CustomerRewardHistory customerRewardHistoryObj = null;
		for (CustomerRewardHistory customerRewardHistoryObject : obj.getCustomerRewardHistoryList()) {
			customerRewardHistoryObj = new CustomerRewardHistory();

			if (customerRewardHistoryObject.getTransactionType().equals(TransactionType.CREDIT)) {
				activeRewards = activeRewards + customerRewardHistoryObject.getRewards();
			} else if (customerRewardHistoryObject.getTransactionType().equals(TransactionType.DEBIT)) {

				activeRewards = activeRewards - customerRewardHistoryObject.getRewards();
				usedRewards=customerRewardHistoryObject.getRewards();
			}

			customerRewardHistoryObj.setRewardId(obj.getId());

		}
		obj.setLockedRewards(lockedRewards);
		obj.setActiveRewards(activeRewards);
		obj.setUsedRewards(usedRewards);
		customerRewardRepositoy.saveAndFlush(obj);

		for (CustomerRewardHistory customerRewardHistoryObject : obj.getCustomerRewardHistoryList()) {
			customerRewardHistoryObj = new CustomerRewardHistory();
			customerRewardHistoryObject.setRewardId(obj.getId());
			customerRewardHistoryService.saveOrUpdate(customerRewardHistoryObject);
		}

	}

	public Optional<CustomerReward> findById(UUID id) {
		return customerRewardRepositoy.findById(id);
	}

	public List<CustomerReward> findAll() {
		return customerRewardRepositoy.findAll();
	}

	public Optional<CustomerReward> findByCustomerId(UUID customerId) {
		return customerRewardRepositoy.findByCustomerId(customerId);
	}

//	public Optional<CustomerReward> findByNameExcludeId(String name,UUID excludeId) {
//		return customerRewardRepositoy.findByNameExcludeId(name,excludeId);
//	}

}
