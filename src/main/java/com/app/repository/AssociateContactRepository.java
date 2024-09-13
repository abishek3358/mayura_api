package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.AssociateContact;
import com.app.enumeration.Status;

public interface AssociateContactRepository extends WriteableRepository<AssociateContact, UUID> {
  
    Optional<AssociateContact> findByName(String name);
    
    @Query("SELECT AC FROM AssociateContact AC WHERE AC.name = :name and AC.id <> :excludeId")
    Optional<AssociateContact> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);
    
    
    List<AssociateContact> findByAssociateId(UUID associateId);
    
    List<AssociateContact> findByStatusAndAssociateId(Status status,UUID associateId);
}
