package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.CustomerReward;
import com.app.entity.ProductType;

public interface CustomerRewardRepository extends WriteableRepository<CustomerReward, UUID> {
  

	@Query( "SELECT CR from CustomerReward CR WHERE CR.customerId=:customerId")
	Optional<CustomerReward> findByCustomerId(UUID customerId);	
//    
//    @Query("SELECT PT FROM ProductType PT WHERE PT.name = :name and PT.id <> :excludeId")
//    Optional<CustomerReward> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);
}
