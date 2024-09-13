package com.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.CustomerRewardHistory;

public interface CustomerRewardHistoryRepository extends WriteableRepository<CustomerRewardHistory, UUID> {

	List<CustomerRewardHistory> findByRewardId(UUID id);
	
	
	@Query("SELECT CRH FROM CustomerRewardHistory CRH WHERE CRH.rewardId = :id ")
	List<CustomerRewardHistory> findByCustomerRewardId(UUID id);
  
   
    
//    @Query("SELECT PT FROM ProductType PT WHERE PT.name = :name and PT.id <> :excludeId")
//    Optional<CustomerRewardHistory> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);
}
