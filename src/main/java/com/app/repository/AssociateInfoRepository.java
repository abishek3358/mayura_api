package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.AssociateInfo;

public interface AssociateInfoRepository extends WriteableRepository<AssociateInfo, UUID> {
  
    Optional<AssociateInfo> findByName(String name);
    
    @Query("SELECT AI FROM AssociateInfo AI WHERE AI.name = :name and AI.id <> :excludeId")
    Optional<AssociateInfo> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);
}
