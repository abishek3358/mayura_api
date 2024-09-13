package com.app.service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.OfferDetailsDTO;
import com.app.dto.OfferDetailsViewDTO;
import com.app.entity.OfferDetails;
import com.app.enumeration.Status;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.CustomerRepository;
import com.app.repository.OfferDetailsRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class OfferDetailsService {

	private @NonNull OfferDetailsRepository offerDetailsRepository;

	private @NonNull CustomerRepository customerRepository;

	private @NonNull CustomerOrderRepository customerOrderRepository;
	
	

	@Transactional
	public void saveOrUpdate(OfferDetails obj) throws Exception {
		offerDetailsRepository.saveAndFlush(obj);
	}

	public Optional<OfferDetails> findById(UUID id) {
		return offerDetailsRepository.findById(id);
	}

	public List<OfferDetails> findAll() {
		return offerDetailsRepository.findAll();
	}

	public void deleteById(UUID id) {
		offerDetailsRepository.deleteById(id);
	}

	public OfferDetailsDTO getById(UUID offerDetailsId) {
		Optional<OfferDetails> OfferDetailsOptional = offerDetailsRepository.findById(offerDetailsId);
		OfferDetails offerDetailsObj = OfferDetailsOptional.get();
		OfferDetailsDTO offerDetailsDtoObj = new OfferDetailsDTO();
		offerDetailsDtoObj.setId(offerDetailsObj.getId());
		offerDetailsDtoObj.setOfferCode(offerDetailsObj.getOfferCode());
		offerDetailsDtoObj.setOfferName(offerDetailsObj.getOfferName());
		offerDetailsDtoObj.setStartDateAndTime(offerDetailsObj.getStartDateAndTime());
		offerDetailsDtoObj.setExpiryDateAndTime(offerDetailsObj.getExpiryDateAndTime());
		offerDetailsDtoObj.setOfferType(offerDetailsObj.getOfferType());
		offerDetailsDtoObj.setOfferValue(offerDetailsObj.getOfferValue());
		offerDetailsDtoObj.setMinimumOrderAmount(offerDetailsObj.getMinimumOrderAmount());
		offerDetailsDtoObj.setTermsAndConditions(offerDetailsObj.getTermsAndConditions());
		return offerDetailsDtoObj;
	}
	public List<OfferDetailsDTO> getAll() {
		List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
		OfferDetailsDTO offerDetailsDtoObj = null;
		List<OfferDetailsDTO> offerDetailsDtoList = new ArrayList<OfferDetailsDTO>();
		for (OfferDetails offerDetailsObj : offerDetailsList) {
			offerDetailsDtoObj = new OfferDetailsDTO();
			offerDetailsDtoObj.setId(offerDetailsObj.getId());
			offerDetailsDtoObj.setOfferCode(offerDetailsObj.getOfferCode());
			offerDetailsDtoObj.setOfferName(offerDetailsObj.getOfferName());
			offerDetailsDtoObj.setStartDateAndTime(offerDetailsObj.getStartDateAndTime());
			offerDetailsDtoObj.setExpiryDateAndTime(offerDetailsObj.getExpiryDateAndTime());
			offerDetailsDtoObj.setOfferType(offerDetailsObj.getOfferType());
			offerDetailsDtoObj.setOfferValue(offerDetailsObj.getOfferValue());
			offerDetailsDtoObj.setMinimumOrderAmount(offerDetailsObj.getMinimumOrderAmount());
			offerDetailsDtoObj.setTermsAndConditions(offerDetailsObj.getTermsAndConditions());
			offerDetailsDtoList.add(offerDetailsDtoObj);
		}
		return offerDetailsDtoList;
	}


	public List<OfferDetailsDTO> getActiveAll() {
		List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
		OfferDetailsDTO offerDetailsDtoObj = null;
		List<OfferDetailsDTO> offerDetailsDtoList = new ArrayList<OfferDetailsDTO>();
		for (OfferDetails offerDetailsObj : offerDetailsList) {
			offerDetailsDtoObj = new OfferDetailsDTO();
			offerDetailsDtoObj.setId(offerDetailsObj.getId());
			offerDetailsDtoObj.setOfferCode(offerDetailsObj.getOfferCode());
			offerDetailsDtoObj.setOfferName(offerDetailsObj.getOfferName());
			offerDetailsDtoObj.setStartDateAndTime(offerDetailsObj.getStartDateAndTime());
			offerDetailsDtoObj.setExpiryDateAndTime(offerDetailsObj.getExpiryDateAndTime());
			offerDetailsDtoObj.setOfferType(offerDetailsObj.getOfferType());
			offerDetailsDtoObj.setOfferValue(offerDetailsObj.getOfferValue());
			offerDetailsDtoObj.setMinimumOrderAmount(offerDetailsObj.getMinimumOrderAmount());
			offerDetailsDtoObj.setTermsAndConditions(offerDetailsObj.getTermsAndConditions());
			offerDetailsDtoList.add(offerDetailsDtoObj);
		}
		Date currentDate=new Date();		
		return offerDetailsDtoList.stream().filter(al->al.getExpiryDateAndTime().compareTo(currentDate)>=0)
		.collect(Collectors.toList());
		 
	}
	
	public List<OfferDetailsViewDTO> getActiveOffers() {
		List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
		OfferDetailsViewDTO offerDetailsViewDtoObj = null;
		List<OfferDetailsViewDTO> offerDetailsDtoList = new ArrayList<OfferDetailsViewDTO>();
		for (OfferDetails offerDetailsObj : offerDetailsList) {
			offerDetailsViewDtoObj = new OfferDetailsViewDTO();
			offerDetailsViewDtoObj.setId(offerDetailsObj.getId());
			offerDetailsViewDtoObj.setOfferCode(offerDetailsObj.getOfferCode());
			offerDetailsViewDtoObj.setOfferName(offerDetailsObj.getOfferName());			
			offerDetailsViewDtoObj.setTermsAndConditions(offerDetailsObj.getTermsAndConditions());
			offerDetailsViewDtoObj.setExpiryDateAndTime(offerDetailsObj.getExpiryDateAndTime());
			offerDetailsViewDtoObj.setOfferValue(offerDetailsObj.getOfferValue());
			offerDetailsViewDtoObj.setMinimumOrderAmount(offerDetailsObj.getMinimumOrderAmount());
			offerDetailsDtoList.add(offerDetailsViewDtoObj);
		}
		Date currentDate=new Date();		
		return offerDetailsDtoList.stream().filter(al->al.getExpiryDateAndTime().compareTo(currentDate)>=0)
		.collect(Collectors.toList());
		 
	}
	
	public List<OfferDetailsDTO> getExpiryAll() {
		List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
		OfferDetailsDTO offerDetailsDtoObj = null;
		List<OfferDetailsDTO> offerDetailsDtoList = new ArrayList<OfferDetailsDTO>();
		for (OfferDetails offerDetailsObj : offerDetailsList) {
			offerDetailsDtoObj = new OfferDetailsDTO();
			offerDetailsDtoObj.setId(offerDetailsObj.getId());
			offerDetailsDtoObj.setOfferCode(offerDetailsObj.getOfferCode());
			offerDetailsDtoObj.setOfferName(offerDetailsObj.getOfferName());
			offerDetailsDtoObj.setStartDateAndTime(offerDetailsObj.getStartDateAndTime());
			offerDetailsDtoObj.setExpiryDateAndTime(offerDetailsObj.getExpiryDateAndTime());
			offerDetailsDtoObj.setOfferType(offerDetailsObj.getOfferType());
			offerDetailsDtoObj.setOfferValue(offerDetailsObj.getOfferValue());
			offerDetailsDtoObj.setMinimumOrderAmount(offerDetailsObj.getMinimumOrderAmount());
			offerDetailsDtoObj.setTermsAndConditions(offerDetailsObj.getTermsAndConditions());
			offerDetailsDtoList.add(offerDetailsDtoObj);
		}
		Date currentDate=new Date();		
		return offerDetailsDtoList.stream().filter(al->al.getExpiryDateAndTime().compareTo(currentDate)<0)
		.collect(Collectors.toList());
		 
	}

	public Optional<OfferDetails> findByOfferCode(String offerCode) {
		
		return offerDetailsRepository.findByOfferCode(offerCode);
	}

	public void updateProductionStatus() {
		List<OfferDetails> productions = offerDetailsRepository.findAll();
		LocalDate currentDate = LocalDate.now().minusDays(2);

		for (OfferDetails production : productions) {
			Date expiryDate = production.getExpiryDateAndTime();

			// Convert Date to LocalDate
			Instant instant = expiryDate.toInstant();
			ZoneId zoneId = ZoneId.systemDefault();
			LocalDate convertedExpiryDate = instant.atZone(zoneId).toLocalDate();

			boolean isActive = !convertedExpiryDate.isEqual(currentDate);
			if (!isActive) {
				production.setStatus(Status.INACTIVE);
				offerDetailsRepository.save(production);
			}
		}

	}
	


}
