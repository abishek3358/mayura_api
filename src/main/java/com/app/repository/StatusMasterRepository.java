package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.StatusMaster;
import com.app.enumeration.StatusType;

public interface StatusMasterRepository extends WriteableRepository<StatusMaster, UUID> {
	Optional<StatusMaster> findByName(String name);
    
    @Query("SELECT SM FROM StatusMaster SM WHERE SM.statusType = :statusType ")
    List<StatusMaster> findByStatusType(@Param("statusType") StatusType statusType);


}
