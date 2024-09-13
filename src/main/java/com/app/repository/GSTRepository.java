package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.GST;
import com.twilio.rest.api.v2010.Account.Status;

public interface GSTRepository extends WriteableRepository<GST, UUID> {

    Optional<GST> findByName(String name);
    
    @Query("SELECT PT FROM GST PT WHERE PT.name = :name and PT.id <> :excludeId")
    
    Optional<GST> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);

	


}
