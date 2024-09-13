package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.VendorContactPersonDTO;
import com.app.entity.VendorContactPerson;
import com.app.repository.VendorContactPersonRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorContactPersonService {

	private @NonNull VendorContactPersonRepository vendorContactPersonRepository;

	public void saveOrUpdate(VendorContactPerson obj) {
		vendorContactPersonRepository.saveAndFlush(obj);
	}
	
	public Optional<VendorContactPerson> findById(UUID id) {
		return vendorContactPersonRepository.findById(id);
	}

	public VendorContactPersonDTO getById(UUID id) {		
		Optional<VendorContactPerson> vendorOptional = vendorContactPersonRepository.findById(id);
		VendorContactPerson vendorObj = vendorOptional.get();
		String trueString="Yes";
		String falseString="No";
		
		VendorContactPersonDTO response = new VendorContactPersonDTO();
		response.setId(vendorObj.getId());
		response.setVendorId(vendorObj.getVendorId());
		response.setFullName(vendorObj.getFullName());
		response.setEmailId(vendorObj.getEmailId());
		response.setPhoneNumber(vendorObj.getPhoneNumber());
		response.setContactPersonRole(vendorObj.getContactPersonRole());
		response.setMarkAsDefault(vendorObj.getMarkAsDefaultDb()==true?trueString:falseString);		
		return response;
		}

	public List<VendorContactPersonDTO> getAll() {
		List<VendorContactPerson> vendorList=vendorContactPersonRepository.findAll();
		List<VendorContactPersonDTO> vendorDtoList = new ArrayList<VendorContactPersonDTO>();
		VendorContactPersonDTO response = null;
		String trueString="Yes";
		String falseString="No";
		
		for (VendorContactPerson vendorObj : vendorList) {
			response = new VendorContactPersonDTO();
			response.setId(vendorObj.getId());
			response.setVendorId(vendorObj.getVendorId());
			response.setFullName(vendorObj.getFullName());
			response.setEmailId(vendorObj.getEmailId());
			response.setPhoneNumber(vendorObj.getPhoneNumber());
			response.setContactPersonRole(vendorObj.getContactPersonRole());
			response.setMarkAsDefault(vendorObj.getMarkAsDefaultDb()==true?trueString:falseString);
			vendorDtoList.add(response);
		}
		return vendorDtoList;
	}
	

	public void deleteById(UUID id) {
		vendorContactPersonRepository.deleteById(id);
	}


}
