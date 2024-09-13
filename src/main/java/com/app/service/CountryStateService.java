package com.app.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.app.entity.City;
import com.app.entity.Country;
import com.app.entity.State;

public interface CountryStateService{
	public List<Country> getCountryList();
	public List<State> getStatesByCountryId(UUID countryId);
	//public List<State> getStatesByCountryId(String countryName);
	public Map<UUID,List<State>> getCountryStateMap();
	public List<City> getCitiesByStateId(UUID stateId);
	public List<City> getCitiesByStateId(UUID stateId, String cityKey);
	public List<City> getCitiesBykey(String cityKey);
	public City getCityByStateIdAndCityID(UUID stateId, UUID cityId);
	public Country getCountryById(UUID countryId);
	public State getStateById(UUID stateId);
	public City getCityById(UUID cityId);
	public void pushCountry(Country countryOBj);
	public void pushState(State state);
//	public City findById(UUID cityId);
	
}
