package com.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.ManageRewards;
import com.app.enumeration.RewardType;

public interface ManageRewardsRepository extends WriteableRepository<ManageRewards, UUID> {

	ManageRewards findByRewardType(RewardType rewardType);

}
