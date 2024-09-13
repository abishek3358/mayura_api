package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.CityDTO;
import com.app.entity.City;
import com.app.repository.CityRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CityService {

	private @NonNull CityRepository cityRepository;

	public Optional<City> findById(UUID id) {
		return cityRepository.findById(id);
	}

	public List<City> findAll() {
		return cityRepository.findAll();
	}

	public Optional<City> findByName(String name) {
		return cityRepository.findByName(name);
	}

	public Optional<City> findByNameExcludeId(String name, UUID id) {
		return cityRepository.findByNameExcludeId(name, id);
	}

	public void saveOrUpdate(City obj) {
		cityRepository.saveAndFlush(obj);
	}

	public void deleteById(UUID cityId) {
		cityRepository.deleteById(cityId);
	}

	public CityDTO getCityByID(UUID id) {
		City city = cityRepository.findById(id).get();
		CityDTO response = new CityDTO();
		response.setId(city.getId());
		response.setLatitude(city.getLatitude());
		response.setLongitude(city.getLongitude());
		response.setName(city.getName());
		response.setShortName(city.getShortName());
		response.setStateId(city.getStateId());
		response.setCountryId(city.getCountryId());
		response.setZipCode(city.getZipCode());

		return response;
	}

	public String findNameById(UUID cityId) {		
		return cityRepository.findNameById(cityId);
	}
}
