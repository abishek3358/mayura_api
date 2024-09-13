package com.app.repository;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.Warehouse;

public interface WarehouseRepository extends WriteableRepository<Warehouse, UUID> {
	Optional<Warehouse> findByName(String name);
	
	
	@Query(value = "SELECT * FROM  warehouse where status='Active'", nativeQuery = true)
	List<Warehouse> findAll();
	
    
    @Query("SELECT WH FROM Warehouse WH WHERE WH.name = :name and WH.id <> :excludeId")
    Optional<Warehouse> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);


}
