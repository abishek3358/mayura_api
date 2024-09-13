package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.State;

public interface StateRepository extends WriteableRepository<State, UUID> {

	public List<State> findAllByCountryObjId(UUID countryId);
	
	public Optional<State> findByNameAndCountryObjId(String name, UUID countryId);

	@Query(value = "Select s.name from state s where s.id=:stateId", nativeQuery = true)
	public String findNameById(UUID stateId);
	
}
