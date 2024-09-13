package com.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.entity.City;
import com.app.entity.Country;
import com.app.entity.State;
import com.app.repository.CityRepository;
import com.app.repository.CountryRepository;
import com.app.repository.StateRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull; 

@Repository
@AllArgsConstructor(onConstructor_ = { @Autowired })
public  class CountryStateServiceImpl implements CountryStateService{

	final static Logger logger = LoggerFactory.getLogger(CountryStateServiceImpl.class);
	
	private @NonNull CountryRepository countryRepository;
	private @NonNull StateRepository stateRepository;
	private @NonNull CityRepository cityRepository;
	
	private List<Country> countryList = new ArrayList<Country>();
	private Map<UUID,List<State>> countryStateMap = new HashMap<UUID,List<State>>();
	private Map<UUID,List<City>> stateCityMap = new HashMap<UUID,List<City>>();
	private Map<UUID, Country> countryMap = new HashMap<>();
	private Map<UUID, State> stateMap = new HashMap<>();
	private Map<UUID, City> cityMap = new HashMap<>();
	private Map<UUID, City> cityObjMap = new HashMap<>();
	
	@PostConstruct
    private void init() {
    	
		 
    	 List<Country> countries = countryRepository.findAll();
    	 
    	 if(null != countries && !countries.isEmpty()) {
    		 
    		List<State> stateList = stateRepository.findAll();
    		
    		Map<UUID,List<State>> countryStateMapTemp = new HashMap<UUID,List<State>>();
    		Map<UUID, State> stateMapTemp = new HashMap<>();
    		for (State state : stateList) {
    			List<State> stateTempList = countryStateMapTemp.get(state.getCountryObj().getId());
    			if(null != stateTempList && !stateTempList.isEmpty()) {
    				stateTempList.add(state);
    			}else {
    				stateTempList = new ArrayList<State>();
    				stateTempList.add(state);
    			}
    			countryStateMapTemp.put(state.getCountryObj().getId(),stateTempList);
    			stateMapTemp.put(state.getId(), state);
    			List<City> cities = cityRepository.findByStateId(state.getId());
    			stateCityMap.put(state.getId(), cities);
    			
			}
    		
    		 Map<UUID, Country> countryMapTemp = new HashMap<>();
    		 List<Country> countryListTemp = new ArrayList<Country>();
			 for (Country country : countries) {
				 countryMapTemp.put(country.getId(), country);
				 countryListTemp.add(country);
			 }
    		 
			 countryMap.clear();
			 countryMap = new HashMap<UUID, Country>(countryMapTemp);
			 
    		 countryList.clear();
    		 countryList = new ArrayList<>(countryListTemp); 
    		 
    		 countryStateMap.clear();
    		 countryStateMap = new HashMap<>(countryStateMapTemp);
    		 
    		 stateMap.clear();
    		 stateMap = new HashMap<UUID, State>(stateMapTemp);
    		 
    	 }
    }

	@Override
	public List<Country> getCountryList() {
		return countryList;
	}

	@Override
	public List<State> getStatesByCountryId(UUID countryId) {
		return countryStateMap.get(countryId);
	}

	@Override
	public Map<UUID, List<State>> getCountryStateMap() {
		return countryStateMap;
	}

	/*
	 * @Override public List<State> getStatesByCountryId(String countryName) {
	 * return countryStateMapByName.get(countryName); }
	 */

	@Override
	public List<City> getCitiesByStateId(UUID stateId) {
		return stateCityMap.get(stateId);
	}
	
	@Override
	public List<City> getCitiesByStateId(UUID stateId, String cityKey) {
		List<City> cityList=new ArrayList<City>();
		for(City cityOb:stateCityMap.get(stateId)) {
			if(cityOb.getName().toLowerCase().contains(cityKey.toLowerCase())) {
				cityList.add(cityOb);
			}
		}
		 
		return cityList;
	}
	
	@Override
	public List<City> getCitiesBykey(String cityKey) {
		List<City> cityList=new ArrayList<City>();
		for(UUID stateId:stateCityMap.keySet()) {
			for(City cityOb:stateCityMap.get(stateId)) {
				if(cityOb.getName().toLowerCase().contains(cityKey.toLowerCase())) {
					cityList.add(cityOb);
				}
			}
		}
		
		return cityList;
	}
	
	@Override
	public City getCityByStateIdAndCityID(UUID stateId, UUID cityId) {
		City cityObj = null;
		for(City cityOb:stateCityMap.get(stateId)) {
			if(cityOb.getId().equals(cityId)) {
				cityObj = cityOb;
				break;
			}
		}
		return cityObj;
	}
	
	@Override
	public void pushCountry(Country countryOBj) {
		countryMap.put(countryOBj.getId(), countryOBj);
		countryList.add(countryOBj);
	}
	
	
	@Override
	public void pushState(State state) {
		stateMap.put(state.getId(), state);
		List<State> stateTempList = countryStateMap.get(state.getCountryObj().getId());
		if(null != stateTempList && !stateTempList.isEmpty()) {
			stateTempList.add(state);
		}else {
			stateTempList = new ArrayList<State>();
			stateTempList.add(state);
		}
		countryStateMap.put(state.getCountryObj().getId(),stateTempList);
	}

	@Override
	public Country getCountryById(UUID countryId) {
		return countryMap.get(countryId);
	}

	@Override
	public State getStateById(UUID stateId) {
		return stateMap.get(stateId);
	}

	@Override
	public City getCityById(UUID cityId) {
		return cityMap.get(cityId);
	}
 
//	@Override
//	public City findById(UUID cityId) {
//		// TODO Auto-generated method stub
//		return cityObjMap.get(cityId);
//	}
	
	
}
