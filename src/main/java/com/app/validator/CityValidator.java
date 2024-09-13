package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.CityDTO;
import com.app.entity.City;
import com.app.entity.Country;
import com.app.entity.State;
import com.app.enumeration.RequestType;
import com.app.service.CityService;
import com.app.service.CountryService;
import com.app.service.MessagePropertyService;
import com.app.service.StateService;
import com.app.service.UnitOfMeasurementService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CityValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;
	
	private @NonNull CountryService countryService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for UnitOfMeasurement validation Added by Ulaganathan
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, CityDTO request) {

		errors = new ArrayList<>();
		City city = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<City> cityDuplicateObj = cityService.findByName(request.getName());
			if (cityDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getName() };
				errors.add(messageSource.getMessage("city.name.duplicate", params));
			}
		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

			Optional<City> cityOptional = cityService.findById(request.getId());
			if (!cityOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("city.not.found"));
			}

			city = cityOptional.get();
		}

		if (ValidationUtil.isNullOrEmpty(request.getName())) {
			errors.add(messageSource.getMessage("city.name"));
		} else {
			request.setName(ValidationUtil.getFormattedString(request.getName()));
			if (!ValidationUtil.isValidName(request.getName())) {
				errors.add(messageSource.getMessage("city.name.invalid"));
			}
		}

		if (ValidationUtil.isNullOrEmpty(request.getShortName())) {
			errors.add(messageSource.getMessage("city.short.name.required"));
		}

		if (ValidationUtil.isNullOrEmpty(request.getZipCode())) {
			errors.add(messageSource.getMessage("city.zip.code.required"));
		}

		if (ValidationUtil.isNull(request.getLatitude())) {
			errors.add(messageSource.getMessage("city.latitude.required"));
		}
		if (ValidationUtil.isNull(request.getLongitude())) {
			errors.add(messageSource.getMessage("city.longitude.required"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		if (null == city) {
			city = City.builder().stateId(request.getStateId()).countryId(request.getCountryId()).name(request.getName()).shortName(request.getShortName())
					.longitude(request.getLongitude()).latitude(request.getLatitude()).zipCode(request.getZipCode())
					.build();
		} else {
			city.setLatitude(request.getLatitude());
			city.setLongitude(request.getLongitude());
			city.setZipCode(request.getZipCode());
			city.setShortName(request.getShortName());
			city.setName(request.getName());			
			city.setStateId(request.getStateId());
			city.setCountryId(request.getCountryId());
		}
		result.setObject(city);
		return result;
	}

}