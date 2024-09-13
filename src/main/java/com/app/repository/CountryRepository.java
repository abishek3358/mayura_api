package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.Country;

public interface CountryRepository extends WriteableRepository<Country, UUID> {

	public Optional<Country> findByName(String name);

	@Query(value = "Select c.name from country c where c.id=:countryId", nativeQuery = true)
	public String findNameById(UUID countryId);
}
