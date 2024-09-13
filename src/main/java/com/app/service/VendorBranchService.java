package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.VendorBranchDTO;
import com.app.dto.VendorBranchInterface;
import com.app.dto.VendorHandledOrderInterface;
import com.app.entity.VendorBranch;
import com.app.repository.VendorBranchRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.lang.*;
@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorBranchService {

	private @NonNull VendorBranchRepository vendorBranchRepository;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull CountryService countryService;

	public void saveOrUpdate(VendorBranch object) {
		vendorBranchRepository.saveAndFlush(object);
	}

	public Optional<VendorBranch> findById(UUID id) {
		return vendorBranchRepository.findById(id);
	}

	public List<VendorBranch> findAll() {
		return vendorBranchRepository.findAll();
	}

	public void deleteById(UUID vendorId) {
		vendorBranchRepository.deleteById(vendorId);
	}

	public List<VendorBranchDTO> getAll() {

		List<VendorBranch> vendorBranchList = vendorBranchRepository.findAll();
		List<VendorBranchDTO> vendorBranchDtoList = new ArrayList<VendorBranchDTO>();
		VendorBranchDTO vendorBranchDtoObj = null;
		for (VendorBranch vendorBranchObj : vendorBranchList) {
			vendorBranchDtoObj = new VendorBranchDTO();
			vendorBranchDtoObj.setId(vendorBranchObj.getId());
			vendorBranchDtoObj.setVendorId(vendorBranchObj.getVendorId());
			vendorBranchDtoObj.setBranchName(vendorBranchObj.getBranchName());
			vendorBranchDtoObj.setBranchCode(vendorBranchObj.getBranchCode());
			vendorBranchDtoObj.setLocation(vendorBranchObj.getLocation());
			vendorBranchDtoObj.setLatitude(vendorBranchObj.getLatitude());
			vendorBranchDtoObj.setLongitude(vendorBranchObj.getLongitude());
			vendorBranchDtoObj.setAddress1(vendorBranchObj.getAddress1());
			vendorBranchDtoObj.setAddress2(vendorBranchObj.getAddress2());
			vendorBranchDtoObj.setPostalCode(vendorBranchObj.getPostalCode());
			vendorBranchDtoObj.setCityId(vendorBranchObj.getCityId());
			vendorBranchDtoObj.setCityName(cityService.findById(vendorBranchObj.getCityId()).get().getName());
			vendorBranchDtoObj.setStateId(vendorBranchObj.getStateId());
			vendorBranchDtoObj.setStateName(stateService.findById(vendorBranchObj.getStateId()).get().getName());
			vendorBranchDtoObj.setCountryId(vendorBranchObj.getCountryId());
			vendorBranchDtoObj.setCountryName(countryService.findById(vendorBranchObj.getCountryId()).get().getName());
			vendorBranchDtoList.add(vendorBranchDtoObj);
		}
		return vendorBranchDtoList;
	}

	public VendorBranchDTO getById(UUID id) {

		Optional<VendorBranch> vendorBranchOptional = vendorBranchRepository.findById(id);
		VendorBranch vendorBranchObj = vendorBranchOptional.get();
		VendorBranchDTO vendorBranchDtoObj = new VendorBranchDTO();
		vendorBranchDtoObj.setId(vendorBranchObj.getId());
		vendorBranchDtoObj.setVendorId(vendorBranchObj.getVendorId());
		vendorBranchDtoObj.setBranchName(vendorBranchObj.getBranchName());
		vendorBranchDtoObj.setBranchCode(vendorBranchObj.getBranchCode());
		vendorBranchDtoObj.setLocation(vendorBranchObj.getLocation());
		vendorBranchDtoObj.setLatitude(vendorBranchObj.getLatitude());
		vendorBranchDtoObj.setLongitude(vendorBranchObj.getLongitude());
		vendorBranchDtoObj.setAddress1(vendorBranchObj.getAddress1());
		vendorBranchDtoObj.setAddress2(vendorBranchObj.getAddress2());
		vendorBranchDtoObj.setPostalCode(vendorBranchObj.getPostalCode());
		vendorBranchDtoObj.setCityId(vendorBranchObj.getCityId());
		vendorBranchDtoObj.setCityName(cityService.findById(vendorBranchObj.getCityId()).get().getName());
		vendorBranchDtoObj.setStateId(vendorBranchObj.getStateId());
		vendorBranchDtoObj.setStateName(stateService.findById(vendorBranchObj.getStateId()).get().getName());
		vendorBranchDtoObj.setCountryId(vendorBranchObj.getCountryId());
		vendorBranchDtoObj.setCountryName(countryService.findById(vendorBranchObj.getCountryId()).get().getName());
		return vendorBranchDtoObj;
	}

//	public List<VendorBranch> findByVendors(Double new_latitude, Double new_longitude, 
//			Double start_latitude, Double start_longitude) {
//		
//		return vendorBranchRepository.findByVendors(new_latitude,new_longitude,start_latitude,start_longitude);
//	}

	public List<VendorBranchInterface> findByVendors(UUID cityId) {		
		return vendorBranchRepository.findByVendors(cityId.toString());
	}

	public List<VendorBranchInterface> findByVendorsState(String stateId) {		
		return vendorBranchRepository.findByVendorsState(stateId.toString());
	}
				 		
	}

	

