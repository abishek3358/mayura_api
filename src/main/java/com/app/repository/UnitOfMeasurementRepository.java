package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.UnitOfMeasurement;
import com.twilio.rest.api.v2010.Account.Status;

public interface UnitOfMeasurementRepository extends WriteableRepository<UnitOfMeasurement, UUID> {
	Optional<UnitOfMeasurement> findByName(String name);

	@Query("SELECT WH FROM UnitOfMeasurement WH WHERE WH.name = :name and WH.id <> :excludeId")
	Optional<UnitOfMeasurement> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);
	
	
	

	// List<UnitOfMeasurement> findByStatus(Status active);

}
