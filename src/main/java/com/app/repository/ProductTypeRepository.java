package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.ProductType;
import com.twilio.rest.api.v2010.Account.Status;

public interface ProductTypeRepository extends WriteableRepository<ProductType, UUID> {
  
    Optional<ProductType> findByName(String name);
    
    @Query("SELECT PT FROM ProductType PT WHERE PT.name = :name and PT.id <> :excludeId")
    
    Optional<ProductType> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);

	//List<ProductType> findByStatus(Status active);
}
