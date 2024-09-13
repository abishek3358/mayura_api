package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.AccountGroup;

public interface AccountGroupRepository extends WriteableRepository<AccountGroup, UUID> {
  
    Optional<AccountGroup> findByName(String name);
    
    @Query("SELECT AG FROM AccountGroup AG WHERE AG.name = :name and AG.id <> :excludeId")
    Optional<AccountGroup> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);
}
