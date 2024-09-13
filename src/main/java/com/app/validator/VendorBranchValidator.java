package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.VendorBranchDTO;
import com.app.entity.City;
import com.app.entity.Country;
import com.app.entity.GST;
import com.app.entity.State;
import com.app.entity.VendorBranch;
import com.app.enumeration.RequestType;
import com.app.service.CityService;
import com.app.service.CountryService;
import com.app.service.MessagePropertyService;
import com.app.service.StateService;
import com.app.service.VendorBranchService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorBranchValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull VendorBranchService vendorBranchService;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull CountryService countryService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for product type validation Added by Ulaganathan
	 * @param vendorId 
	 * 
	 * @param vendorBranch
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, VendorBranchDTO request, UUID vendorId) {

		errors = new ArrayList<>();
		VendorBranch vendorBranch = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<VendorBranch> vendorBranchOptional = vendorBranchService.findById(request.getId());
			if (!vendorBranchOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("vendor.branch.not.found"));
			}
			vendorBranch = vendorBranchOptional.get();
		}

		if (ValidationUtil.isNullOrEmpty(request.getBranchName())) {
			errors.add(messageSource.getMessage("vendor.branch.name.required"));
		}

		if (ValidationUtil.isNullOrEmpty(request.getBranchCode())) {
			errors.add(messageSource.getMessage("vendor.branch.code.required"));
		}

		if (ValidationUtil.isNullOrEmpty(request.getLocation())) {
			errors.add(messageSource.getMessage("vendor.branch.location.required"));
		}

		if (ValidationUtil.isNullOrEmpty(request.getAddress1())) {
			errors.add(messageSource.getMessage("vendor.branch.address1.required"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getPostalCode())) {
			errors.add(messageSource.getMessage("vendor.branch.postal.code.required"));
		}

		Optional<City> cityOptional = null;
		if (ValidationUtil.isNull(request.getCityId())) {
			errors.add(messageSource.getMessage("mentor.city.required"));
		} else {
			cityOptional = cityService.findById(request.getCityId());
			if (!cityOptional.isPresent())
				errors.add(messageSource.getMessage("city.not.found"));
		}

		Optional<State> stateOptional = null;
		if (ValidationUtil.isNull(request.getStateId())) {
			errors.add(messageSource.getMessage("mentor.state.required"));
		} else {
			stateOptional = stateService.findById(request.getStateId());
			if (!stateOptional.isPresent())
				errors.add(messageSource.getMessage("state.not.found"));
		}

		Optional<Country> countryOptional = null;
		if (ValidationUtil.isNull(request.getCountryId())) {
			errors.add(messageSource.getMessage("mentor.country.required"));
		} else {
			countryOptional = countryService.findById(request.getCountryId());
			if (!countryOptional.isPresent())
				errors.add(messageSource.getMessage("country.not.found"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		if (null == vendorBranch) {
			vendorBranch = VendorBranch.builder().vendorId(vendorId).branchName(request.getBranchName())
					.branchCode(request.getBranchCode()).location(request.getLocation()).latitude(request.getLatitude())
					.longitude(request.getLongitude()).address1(request.getAddress1()).address2(request.getAddress2())
					.postalCode(request.getPostalCode()).cityId(request.getCityId()).stateId(request.getStateId())
					.countryId(request.getCountryId()).build();
		} else {
			vendorBranch.setVendorId(vendorId);
			vendorBranch.setBranchName(request.getBranchName());
			vendorBranch.setBranchCode(request.getBranchCode());
			vendorBranch.setLocation(request.getLocation());
			vendorBranch.setLatitude(request.getLatitude());
			vendorBranch.setLongitude(request.getLongitude());
			vendorBranch.setAddress1(request.getAddress1());
			vendorBranch.setAddress2(request.getAddress2());
			vendorBranch.setPostalCode(request.getPostalCode());
			vendorBranch.setCityId(request.getCityId());
			vendorBranch.setStateId(request.getStateId());
			vendorBranch.setCountryId(request.getCountryId());

		}
		result.setObject(vendorBranch);
		return result;
	}

}
