package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.ManageRewards;
import com.app.enumeration.RewardType;
import com.app.repository.ManageRewardsRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
@Transactional()
public class ManageRewardsService {

	private @NonNull ManageRewardsRepository manageRewardsRepository;

	@Transactional()
	public void saveOrUpdate(ManageRewards obj) {

		manageRewardsRepository.saveAndFlush(obj);

	}

	public Optional<ManageRewards> findById(UUID id) {

		return manageRewardsRepository.findById(id);
	}

	public List<ManageRewards> findAll() {
		// TODO Auto-generated method stub
		return manageRewardsRepository.findAll();
	}

	public ManageRewards findBYRewardType(RewardType rewardType) {

		return manageRewardsRepository.findByRewardType(rewardType);
	}

}
