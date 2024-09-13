package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.VendorAccountCreateDTO;
import com.app.dto.VendorBranchCreateDTO;
import com.app.dto.VendorContactPersonCreateDTO;
import com.app.dto.VendorDTO;
import com.app.dto.VendorUpdateDTO;
import com.app.entity.City;
import com.app.entity.Country;
import com.app.entity.State;
import com.app.entity.Vendor;
import com.app.entity.VendorAccount;
import com.app.entity.VendorBranch;
import com.app.entity.VendorContactPerson;
import com.app.enumeration.RequestType;
import com.app.service.CityService;
import com.app.service.CountryService;
import com.app.service.MessagePropertyService;
import com.app.service.StateService;
import com.app.service.VendorService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull VendorService vendorService;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull CountryService countryService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for product type validation Added by Ulaganathan
	 * 
	 * @param vendorBranch
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, VendorDTO request) {

		errors = new ArrayList<>();
		Vendor vendor = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<Vendor> vendorOptional = vendorService.findById(request.getId());
			if (!vendorOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("vendor.not.found"));
			}
			vendor = vendorOptional.get();
		}
		if (ValidationUtil.isNullOrEmpty(request.getName())) {
			if (ValidationUtil.isValidName(request.getName())) {
			errors.add(messageSource.getMessage("vendor.name.required"));
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getVendorCode())) {
			errors.add(messageSource.getMessage("vendor.code.required"));
		}		
			if (ValidationUtil.isValidEmailId(request.getEmailId())) {
				if (ValidationUtil.isNullOrEmpty(request.getEmailId())) {
			errors.add(messageSource.getMessage("vendor.email.id.required"));
			}
		}		
			if (ValidationUtil.isValidPhoneNo(request.getPhoneNumber())) {
				if (ValidationUtil.isNullOrEmpty(request.getPhoneNumber())) {
			errors.add(messageSource.getMessage("vendor.phone.number.required"));
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getGstTaxNumber())) {
			errors.add(messageSource.getMessage("vendor.gst.required"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		List<VendorContactPerson> vendorContactPersonList = validateVendorContactPerson(requestType, request, errors);
		List<VendorBranch> vendorBranchList = validateVendorBranch(requestType, request, errors);
		List<VendorAccount> vendorAccountList = validateVendorAccount(requestType, request, errors);

		if (null == vendor) {
			vendor = Vendor.builder().name(request.getName()).vendorCode(request.getVendorCode())
					.emailId(request.getEmailId()).phoneNumber(request.getPhoneNumber())
					.gstTaxNumber(request.getGstTaxNumber()).build();
		} else {
			vendor.setName(request.getName());
			vendor.setVendorCode(request.getVendorCode());
			vendor.setEmailId(request.getEmailId());
			vendor.setPhoneNumber(request.getPhoneNumber());
			vendor.setGstTaxNumber(request.getGstTaxNumber());

		}
		vendor.setVendorContactPersonList(vendorContactPersonList);
		vendor.setVendorBranchList(vendorBranchList);
		vendor.setVendorAccountList(vendorAccountList);
		result.setObject(vendor);
		return result;
	}

	private List<VendorContactPerson> validateVendorContactPerson(RequestType requestType, VendorDTO request,
			List<String> errors2) {
		List<VendorContactPerson> vendorContactPersonList = new ArrayList<VendorContactPerson>();
		for (VendorContactPersonCreateDTO vendorContactPersonDtoObj : request.getVendorContactPersonList()) {
			VendorContactPerson vendorContactPersonObj = null;
			if (ValidationUtil.isNullOrEmpty(vendorContactPersonDtoObj.getFullName())) {
				if (ValidationUtil.isValidName(vendorContactPersonDtoObj.getFullName())) {
				errors.add(messageSource.getMessage("vendor.contact.fullname.required"));
				}
			}
			if (ValidationUtil.isNullOrEmpty(vendorContactPersonDtoObj.getEmailId())) {
				errors.add(messageSource.getMessage("vendor.contact.email.required"));
			}
			if (ValidationUtil.isValidPhoneNo(vendorContactPersonDtoObj.getPhoneNumber())) {
			if (ValidationUtil.isNullOrEmpty(vendorContactPersonDtoObj.getPhoneNumber())) {				
				errors.add(messageSource.getMessage("vendor.contact.phone.required"));
			}	
			errors.add(messageSource.getMessage("vendor.contact.invalid.phone"));
			}			
			if (ValidationUtil.isNullOrEmpty(vendorContactPersonDtoObj.getContactPersonRole())) {
				errors.add(messageSource.getMessage("vendor.contact.role.required"));
			}
			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}
			if (null == vendorContactPersonObj) {
				vendorContactPersonObj = VendorContactPerson.builder().fullName(vendorContactPersonDtoObj.getFullName())
						.emailId(vendorContactPersonDtoObj.getEmailId())
						.markAsDefaultDb(vendorContactPersonDtoObj.getMarkAsDefault()=="yes"?true:false)
						.phoneNumber(vendorContactPersonDtoObj.getPhoneNumber())
						.contactPersonRole(vendorContactPersonDtoObj.getContactPersonRole())
						.markAsDefault(vendorContactPersonDtoObj.getMarkAsDefault()).build();
			}
			vendorContactPersonList.add(vendorContactPersonObj);
		}
		return vendorContactPersonList;
	}

	private List<VendorBranch> validateVendorBranch(RequestType requestType, VendorDTO request, List<String> errors2) {
		List<VendorBranch> vendorBranchList = new ArrayList<VendorBranch>();
		for (VendorBranchCreateDTO vendorBranchDtoObj : request.getVendorBranchList()) {
			VendorBranch vendorBranchObj = null;
			if (ValidationUtil.isNullOrEmpty(vendorBranchDtoObj.getBranchName())) {
				errors.add(messageSource.getMessage("vendor.branch.name.required"));
			}
			if (ValidationUtil.isNullOrEmpty(vendorBranchDtoObj.getBranchCode())) {
				errors.add(messageSource.getMessage("vendor.branch.code.required"));
			}
			if (ValidationUtil.isNullOrEmpty(vendorBranchDtoObj.getAddress1())) {
				errors.add(messageSource.getMessage("vendor.branch.address1.required"));
			}
			if (ValidationUtil.isNullOrEmpty(vendorBranchDtoObj.getLocation())) {
				errors.add(messageSource.getMessage("vendor.branch.location.required"));
			}
			if (ValidationUtil.isNullOrEmpty(vendorBranchDtoObj.getPostalCode())) {
				errors.add(messageSource.getMessage("vendor.branch.postal.code.required"));
			}
			Optional<City> cityOptional = null;
			cityOptional = cityService.findById(vendorBranchDtoObj.getCityId());
			if (ValidationUtil.isNull(vendorBranchDtoObj.getCityId())) {
				errors.add(messageSource.getMessage("vendor.branch.city.required"));
			} else {
				cityOptional = cityService.findById(vendorBranchDtoObj.getCityId());
				if (!cityOptional.isPresent())
					errors.add(messageSource.getMessage("city.not.found"));
			}

			Optional<State> stateOptional = null;
			stateOptional = stateService.findById(vendorBranchDtoObj.getStateId());
			if (ValidationUtil.isNull(vendorBranchDtoObj.getStateId())) {
				errors.add(messageSource.getMessage("vendor.branch.state.required"));
			} else {
				stateOptional = stateService.findById(vendorBranchDtoObj.getStateId());
				if (!stateOptional.isPresent())
					errors.add(messageSource.getMessage("state.not.found"));
			}

			Optional<Country> countryOptional = null;
			countryOptional = countryService.findById(vendorBranchDtoObj.getCountryId());
			if (ValidationUtil.isNull(vendorBranchDtoObj.getCountryId())) {
				errors.add(messageSource.getMessage("vendor.branch.country.required"));
			} else {
				countryOptional = countryService.findById(vendorBranchDtoObj.getCountryId());
				if (!countryOptional.isPresent())
					errors.add(messageSource.getMessage("country.not.found"));
			}

			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}

			if (null == vendorBranchObj) {
				vendorBranchObj = VendorBranch.builder().branchName(vendorBranchDtoObj.getBranchName())
						.branchCode(vendorBranchDtoObj.getBranchCode()).address1(vendorBranchDtoObj.getAddress1())
						.address2(vendorBranchDtoObj.getAddress2()).location(vendorBranchDtoObj.getLocation())
						.latitude(vendorBranchDtoObj.getLatitude()).longitude(vendorBranchDtoObj.getLongitude())
						.cityId(vendorBranchDtoObj.getCityId()).stateId(vendorBranchDtoObj.getStateId())
						.countryId(vendorBranchDtoObj.getCountryId()).postalCode(vendorBranchDtoObj.getPostalCode())
						.build();
			}
			vendorBranchList.add(vendorBranchObj);
		}
		return vendorBranchList;
	}

	private List<VendorAccount> validateVendorAccount(RequestType requestType, VendorDTO request,
			List<String> errors2) {
		List<VendorAccount> vendorAccountList = new ArrayList<VendorAccount>();
		for (VendorAccountCreateDTO vendorAccountDtoObj : request.getVendorAccountList()) {
			VendorAccount vendorAccountObj = null;
			if (ValidationUtil.isNull(vendorAccountDtoObj.getCommissionPercentage())) {
				errors.add(messageSource.getMessage("vendor.account.commission.required"));
			}
			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}
			if (null == vendorAccountObj) {
				vendorAccountObj = VendorAccount.builder()
						.commissionPercentage(vendorAccountDtoObj.getCommissionPercentage())
						.payoutPeriod(vendorAccountDtoObj.getPayoutPeriod()).build();
			}
			vendorAccountList.add(vendorAccountObj);
		}
		return vendorAccountList;
	}

	public ValidationResult validateVendor(RequestType requestType, VendorUpdateDTO request) {

		errors = new ArrayList<>();
		Vendor vendor = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<Vendor> vendorOptional = vendorService.findById(request.getId());
			if (!vendorOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("vendor.not.found"));
			}
			vendor = vendorOptional.get();
		}

		if (ValidationUtil.isNullOrEmpty(request.getName())) {
			errors.add(messageSource.getMessage("vendor.name.required"));
		}

		if (ValidationUtil.isNullOrEmpty(request.getVendorCode())) {
			errors.add(messageSource.getMessage("vendor.code.required"));
		}

		if (ValidationUtil.isNullOrEmpty(request.getEmailId())) {
			errors.add(messageSource.getMessage("vendor.email.id.required"));
		}
		if (ValidationUtil.isValidPhoneNo(request.getPhoneNumber())) {
		if (ValidationUtil.isNullOrEmpty(request.getPhoneNumber())) {
			errors.add(messageSource.getMessage("vendor.phone.number.required"));
		}
		errors.add(messageSource.getMessage("vendor.invalid.phone.number.required"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getGstTaxNumber())) {
			errors.add(messageSource.getMessage("vendor.gst.required"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		vendor.setName(request.getName());
		vendor.setVendorCode(request.getVendorCode());
		vendor.setEmailId(request.getEmailId());
		vendor.setPhoneNumber(request.getPhoneNumber());
		vendor.setGstTaxNumber(request.getGstTaxNumber());

		result.setObject(vendor);
		return result;
	}
}
