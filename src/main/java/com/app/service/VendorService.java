package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.VendorDAo;
import com.app.dto.VendorAccountDTO;
import com.app.dto.VendorBranchDTO;
import com.app.dto.VendorContactPersonDTO;
import com.app.dto.VendorUpdateDTO;
import com.app.dto.VendorViewDTO;
import com.app.entity.City;
import com.app.entity.Vendor;
import com.app.entity.VendorAccount;
import com.app.entity.VendorBranch;
import com.app.entity.VendorContactPerson;
import com.app.repository.OneTimePasswordRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.repository.VendorAccountRepository;
import com.app.repository.VendorBranchRepository;
import com.app.repository.VendorContactPersonRepository;
import com.app.repository.VendorRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorService {

	private @NonNull VendorRepository vendorRepository;

	private @NonNull VendorContactPersonRepository vendorContactPersonRepository;

	private @NonNull VendorBranchRepository vendorBranchRepository;

	private @NonNull VendorAccountRepository vendorAccountRepository;

	private @NonNull OneTimePasswordRepository otpRepository;

	private @NonNull UserRepository userRepository;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull CountryService countryService;

	private @NonNull RoleRepository roleRepository;

	private @NonNull UserService userService;
	@Autowired
	MessagePropertyService messageSource;
	
	@Autowired
	VendorDAo vendorDAo;

	@Transactional
	public void saveOrUpdate(Vendor object) {
		vendorRepository.saveAndFlush(object);

		/*
		 * if (object.getId() != null) { //Optional<Role> role =
		 * roleService.findByRoleName("USER");
		 * 
		 * OneTimePassword oneTimePassword =
		 * OneTimePassword.builder().mobileNo(object.getPhoneNumber()).otp("12345")
		 * .userId(object.getId()).build(); otpRepository.saveAndFlush(oneTimePassword);
		 * 
		 * 
		 * Optional<Role> role = roleRepository.findByRoleName(IConstant.ROLE_STAFF);
		 * vendorRepository.saveAndFlush(object); String
		 * enPassword=PasswordUtil.getEncryptedPassword(object.getName());
		 * Optional<User> useropt = userRepository.findByUserName(object.getName()); if
		 * (useropt.isPresent()) { throw new
		 * ObjectInvalidException(messageSource.getMessage("user.name.duplicate"));
		 * }else { User user =
		 * User.builder().email(object.getEmailId()).fullName(object.getName()).
		 * forcePasswordChange(false)
		 * .isDeleted(false).isLocked(false).isOtpVerified(false).roleObj(role.get())
		 * .userName(object.getName()).mobileNo(object.getPhoneNumber()).userRole(
		 * UserRoleType.STAFF)
		 * .password(enPassword).referenceId(object.getId()).build();
		 * userRepository.saveAndFlush(user); } }
		 */
		if (null != object.getVendorContactPersonList() && !object.getVendorContactPersonList().isEmpty()) {
			for (VendorContactPerson vendorCantactPersonObj : object.getVendorContactPersonList()) {
				vendorCantactPersonObj.setVendorId(object.getId());
				vendorContactPersonRepository.saveAndFlush(vendorCantactPersonObj);
			}
		}
		if (null != object.getVendorBranchList() && !object.getVendorBranchList().isEmpty()) {
			for (VendorBranch vendorBranchObj : object.getVendorBranchList()) {
				vendorBranchObj.setVendorId(object.getId());
				vendorBranchRepository.saveAndFlush(vendorBranchObj);
			}
		}
		if (null != object.getVendorAccountList() && !object.getVendorAccountList().isEmpty()) {
			for (VendorAccount vendorAccountObj : object.getVendorAccountList()) {
				vendorAccountObj.setVendorId(object.getId());
				vendorAccountRepository.saveAndFlush(vendorAccountObj);
			}
		}
	}

	public Optional<Vendor> findById(UUID id) {
		return vendorRepository.findById(id);
	}

	public List<Vendor> findAll() {
		return vendorRepository.findAll();
	}

	public void deleteById(UUID vendorId) {
		vendorRepository.deleteById(vendorId);
	}

	public List<VendorViewDTO> getAll() {

		List<Vendor> vendorList = vendorRepository.findAll();
		List<VendorViewDTO> vendorDtoList = new ArrayList<VendorViewDTO>();
		VendorViewDTO vendorDtoObj = null;

		for (Vendor vendorObj : vendorList) {
			vendorDtoObj = new VendorViewDTO();

			vendorDtoObj.setId(vendorObj.getId());
			vendorDtoObj.setName(vendorObj.getName());
			vendorDtoObj.setVendorCode(vendorObj.getVendorCode());
			vendorDtoObj.setEmailId(vendorObj.getEmailId());
			vendorDtoObj.setPhoneNumber(vendorObj.getPhoneNumber());
			vendorDtoObj.setGstTaxNumber(vendorObj.getGstTaxNumber());
			vendorDtoObj.setStatus(vendorObj.getStatus());
			vendorDtoList.add(vendorDtoObj);

			List<VendorContactPerson> vendorContactPersonList = vendorContactPersonRepository
					.findByVendorId(vendorObj.getId());
			List<VendorContactPersonDTO> vendorContactPersonDtoList = new ArrayList<VendorContactPersonDTO>();
			VendorContactPersonDTO vendorContactPersonDtoObj = null;
			for (VendorContactPerson vendorContactPersonObj : vendorContactPersonList) {

				vendorContactPersonDtoObj = new VendorContactPersonDTO();
				vendorContactPersonDtoObj.setId(vendorContactPersonObj.getId());
				vendorContactPersonDtoObj.setVendorId(vendorContactPersonObj.getVendorId());
				vendorContactPersonDtoObj.setFullName(vendorContactPersonObj.getFullName());
				vendorContactPersonDtoObj.setEmailId(vendorContactPersonObj.getEmailId());
				vendorContactPersonDtoObj.setPhoneNumber(vendorContactPersonObj.getPhoneNumber());
				vendorContactPersonDtoObj.setContactPersonRole(vendorContactPersonObj.getContactPersonRole());
				vendorContactPersonDtoObj.setMarkAsDefaultDb(vendorContactPersonObj.getMarkAsDefaultDb());
				vendorContactPersonDtoObj
						.setMarkAsDefault(vendorContactPersonObj.getMarkAsDefaultDb() == true ? "Yes" : "No");
				// vendorContactPersonDtoObj.setMarkAsDefault(vendorContactPersonObj.getMarkAsDefault());
				vendorContactPersonDtoList.add(vendorContactPersonDtoObj);

				List<VendorBranch> vendorBranchList = vendorBranchRepository.findByVendorId(vendorObj.getId());
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
					Optional<City> cityOptional = cityService.findById(vendorBranchObj.getCityId());
					if (cityOptional.isPresent()) {
					    String cityName = cityOptional.get().getName();
					    vendorBranchDtoObj.setCityName(cityName);
					} else {
					}
					vendorBranchDtoObj.setStateId(vendorBranchObj.getStateId());
					vendorBranchDtoObj
							.setStateName(stateService.findById(vendorBranchObj.getStateId()).get().getName());
					vendorBranchDtoObj.setCountryId(vendorBranchObj.getCountryId());
					vendorBranchDtoObj
							.setCountryName(countryService.findById(vendorBranchObj.getCountryId()).get().getName());
					vendorBranchDtoList.add(vendorBranchDtoObj);

					List<VendorAccount> vendorAccountList = vendorAccountRepository.findByVendorId(vendorObj.getId());
					List<VendorAccountDTO> vedorAccountDtoList = new ArrayList<VendorAccountDTO>();
					VendorAccountDTO vendorAccountDtoObj = null;
					for (VendorAccount vendorAccountObj : vendorAccountList) {

						vendorAccountDtoObj = new VendorAccountDTO();
						vendorAccountDtoObj.setId(vendorAccountObj.getId());
						vendorAccountDtoObj.setVendorId(vendorAccountObj.getVendorId());
						vendorAccountDtoObj.setCommissionPercentage(vendorAccountObj.getCommissionPercentage());
						vendorAccountDtoObj.setPayoutPeriod(vendorAccountObj.getPayoutPeriod());
						vedorAccountDtoList.add(vendorAccountDtoObj);
					}
					vendorDtoObj.setVendorAccountList(vedorAccountDtoList);
				}
				vendorDtoObj.setVendorBranchList(vendorBranchDtoList);

			}
			vendorDtoObj.setVendorContactPersonList(vendorContactPersonDtoList);
		}
		return vendorDtoList;
	}

	public VendorViewDTO getById(UUID id) {

		Optional<Vendor> vendorOptional = vendorRepository.findById(id);
		Vendor vendorObj = vendorOptional.get();
		VendorViewDTO vendorDtoObj = new VendorViewDTO();
		vendorDtoObj.setId(vendorObj.getId());
		vendorDtoObj.setName(vendorObj.getName());
		vendorDtoObj.setVendorCode(vendorObj.getVendorCode());
		vendorDtoObj.setEmailId(vendorObj.getEmailId());
		vendorDtoObj.setPhoneNumber(vendorObj.getPhoneNumber());
		vendorDtoObj.setGstTaxNumber(vendorObj.getGstTaxNumber());
		vendorDtoObj.setStatus(vendorObj.getStatus());

		List<VendorContactPerson> vendorContactPersonList = vendorContactPersonRepository
				.findByVendorId(vendorObj.getId());
		List<VendorContactPersonDTO> vendorContactPersonDtoList = new ArrayList<VendorContactPersonDTO>();
		VendorContactPersonDTO vendorContactPersonDtoObj = null;
		for (VendorContactPerson vendorContactPersonObj : vendorContactPersonList) {
			vendorContactPersonDtoObj = new VendorContactPersonDTO();
			vendorContactPersonDtoObj.setId(vendorContactPersonObj.getId());
			vendorContactPersonDtoObj.setVendorId(vendorContactPersonObj.getVendorId());
			vendorContactPersonDtoObj.setFullName(vendorContactPersonObj.getFullName());
			vendorContactPersonDtoObj.setEmailId(vendorContactPersonObj.getEmailId());
			vendorContactPersonDtoObj.setPhoneNumber(vendorContactPersonObj.getPhoneNumber());
			vendorContactPersonDtoObj.setContactPersonRole(vendorContactPersonObj.getContactPersonRole());
			vendorContactPersonDtoObj.setMarkAsDefaultDb(vendorContactPersonObj.getMarkAsDefaultDb());
			vendorContactPersonDtoObj
					.setMarkAsDefault(vendorContactPersonObj.getMarkAsDefaultDb() == true ? "Yes" : "No");
			// vendorContactPersonDtoObj.setMarkAsDefault(vendorContactPersonObj.getMarkAsDefault());
			vendorContactPersonDtoList.add(vendorContactPersonDtoObj);

			List<VendorBranch> vendorBranchList = vendorBranchRepository.findByVendorId(vendorObj.getId());
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
				vendorBranchDtoObj
						.setCountryName(countryService.findById(vendorBranchObj.getCountryId()).get().getName());
				vendorBranchDtoList.add(vendorBranchDtoObj);

				List<VendorAccount> vendorAccountList = vendorAccountRepository.findByVendorId(vendorObj.getId());
				List<VendorAccountDTO> vedorAccountDtoList = new ArrayList<VendorAccountDTO>();
				VendorAccountDTO vendorAccountDtoObj = null;
				for (VendorAccount vendorAccountObj : vendorAccountList) {
					vendorAccountDtoObj = new VendorAccountDTO();
					vendorAccountDtoObj.setId(vendorAccountObj.getId());
					vendorAccountDtoObj.setVendorId(vendorAccountObj.getVendorId());
					vendorAccountDtoObj.setCommissionPercentage(vendorAccountObj.getCommissionPercentage());
					vendorAccountDtoObj.setPayoutPeriod(vendorAccountObj.getPayoutPeriod());
					vedorAccountDtoList.add(vendorAccountDtoObj);
				}
				vendorDtoObj.setVendorAccountList(vedorAccountDtoList);
			}
			vendorDtoObj.setVendorBranchList(vendorBranchDtoList);
		}
		vendorDtoObj.setVendorContactPersonList(vendorContactPersonDtoList);

		return vendorDtoObj;
	}
	@Transactional
	public void saveOrUpdates(Vendor object) {
	    Vendor existingVendor = vendorRepository.findById(object.getId()).orElse(null);
	    if (existingVendor != null) {
	        existingVendor.setName(object.getName());
	        existingVendor.setVendorCode(object.getVendorCode());
	        existingVendor.setPhoneNumber(object.getPhoneNumber());
	        existingVendor.setEmailId(object.getEmailId());
	        existingVendor.setGstTaxNumber(object.getGstTaxNumber());
	        existingVendor.setStatus(object.getStatus());  // Update the status

	        vendorRepository.saveAndFlush(existingVendor);

	        // Handle other related entities as needed
	    } else {
	        // Handle the case where the vendor with the given ID doesn't exist
	        // You can throw an exception or return an appropriate response.
	    }
	}

	public Vendor findByphone(String phoneNumber, UUID id) {
		// TODO Auto-generated method stub
		return vendorDAo.findByphone(phoneNumber,id);
	}

	public Vendor findByemails(String emailId, UUID id) {
		// TODO Auto-generated method stub
		return vendorDAo.findByemails(emailId,id);
	}

	
	

}
