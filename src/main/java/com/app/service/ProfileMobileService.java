package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.ProfileMobileDTO;
import com.app.entity.Customer;
import com.app.entity.ProfileMobile;
import com.app.entity.User;
import com.app.enumeration.MobileType;
import com.app.enumeration.Status;
import com.app.repository.CustomerRepository;
import com.app.repository.ProfileMobileRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProfileMobileService {

	private @NonNull ProfileMobileRepository profileMobileRepository;

	private @NonNull CustomerService customerService;

	private @NonNull CustomerRepository customerRepository;

	private @NonNull UserService userService;

	@Transactional
	public void saveOrUpdate(ProfileMobile obj) throws Exception {

		if (obj.getId() != null || obj.getId() == null) {
			if (obj.getTitle().equals(MobileType.PRIMARY)) {
				Optional<User> userOptional = userService.findByReferenceId(obj.getCustomerId());
				User userObj = userOptional.get();

				String primaryMobileNo = obj.getMobileNo();
				String secondaryMobileNo = userObj.getMobileNo();

				userObj.setMobileNo(primaryMobileNo);
				userService.saveOrUpdate(userObj);

				Optional<ProfileMobile> customerMobileOptional = profileMobileRepository
						.findByMobileNo(MobileType.PRIMARY, userObj.getReferenceId());
				if (customerMobileOptional.isPresent()) {
					ProfileMobile profileMobileObj = customerMobileOptional.get();
					profileMobileObj.setMobileNo(secondaryMobileNo);
					profileMobileObj.setTitle(MobileType.SECONDARY);
					profileMobileRepository.saveAndFlush(profileMobileObj);
				}
				obj.setMobileNo(obj.getMobileNo());
				obj.setTitle(obj.getTitle());

				Optional<Customer> customerOptional = customerService.findById(userObj.getReferenceId());
				Customer customerObj = customerOptional.get();
				customerObj.setMobileNo(primaryMobileNo);
				customerRepository.saveAndFlush(customerObj);
			}
		}
		profileMobileRepository.saveAndFlush(obj);
	}

	public Optional<ProfileMobile> findById(UUID id) {
		return profileMobileRepository.findById(id);
	}

	public List<ProfileMobile> findAll() {
		return profileMobileRepository.findAll();
	}

	public void deleteById(UUID id) {
		profileMobileRepository.deleteById(id);
	}

	public List<ProfileMobileDTO> findByCustomerId(UUID customerId) {
		List<ProfileMobile> customerMobileList = profileMobileRepository.findByCustomerId(customerId);
		ProfileMobileDTO customerMobileDtoObj = null;
		List<ProfileMobileDTO> customerMobileDtoList = new ArrayList<ProfileMobileDTO>();
		for (ProfileMobile customerMobileObj : customerMobileList) {
			customerMobileDtoObj = new ProfileMobileDTO();
			customerMobileDtoObj.setId(customerMobileObj.getId());
			customerMobileDtoObj.setTitle(customerMobileObj.getTitle());
			customerMobileDtoObj.setMobileNo(customerMobileObj.getMobileNo());
			customerMobileDtoList.add(customerMobileDtoObj);
		}
		return customerMobileDtoList;
	}

	public Optional<ProfileMobile> findByMobileNo(String mobileNo, UUID customerId) {
		return profileMobileRepository.findByMobileNo(mobileNo, customerId);
	}

	public ProfileMobileDTO getById(UUID id) {
		Optional<ProfileMobile> profileMobileOptional = profileMobileRepository.findById(id);
		ProfileMobile profileMobileObj = profileMobileOptional.get();
		ProfileMobileDTO profileMobileDtoObj = new ProfileMobileDTO();
		profileMobileDtoObj = new ProfileMobileDTO();
		profileMobileDtoObj.setId(profileMobileObj.getId());
		profileMobileDtoObj.setTitle(profileMobileObj.getTitle());
		profileMobileDtoObj.setMobileNo(profileMobileObj.getMobileNo());
		return profileMobileDtoObj;
	}

	public List<ProfileMobile> findByCustomerIdAndStatus(UUID referenceId, Status active) {
		return profileMobileRepository.findByCustomerIdAndStatus(referenceId, active);
	}
}
