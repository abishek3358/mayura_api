package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.City;

public interface CityRepository extends WriteableRepository<City, UUID> {

	public List<City> findByStateId(UUID stateId);

	public Optional<City> findByName(String name);

	@Query("SELECT WH FROM City WH WHERE WH.name = :name and WH.id =:id")
	public Optional<City> findByNameExcludeId(String name, UUID id);

	public String findNameById(UUID cityId);
	
	
}
