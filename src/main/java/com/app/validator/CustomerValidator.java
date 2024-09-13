package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.CustomerDeliveryAddressDTO;
import com.app.entity.City;
import com.app.entity.Customer;
import com.app.entity.CustomerDeliveryAddress;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.repository.CustomerDeliveryAddressRepository;
import com.app.service.CityService;
import com.app.service.CountryService;
import com.app.service.CountryStateService;
import com.app.service.CustomerDeliveryAddressService;
import com.app.service.CustomerService;
import com.app.service.MessagePropertyService;
import com.app.service.ProductCategoryService;
import com.app.service.ProductTypeService;
import com.app.service.StateService;
import com.app.service.WarehouseService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull ProductCategoryService productCategoryService;

	private @NonNull ProductTypeService productTypeService;

	private @NonNull CustomerService customerService;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull CountryService countryService;

	private @NonNull CustomerDeliveryAddressRepository customerDeliveryAddressRepository;

	private @NonNull CustomerDeliveryAddressService customerDeliveryAddressService;

	@Autowired(required = true)
	CountryStateService countryStateService;

	private @NonNull WarehouseService warehouseService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for product type validation Added by Ulaganathan
	 *
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */

	public ValidationResult validate(RequestType requestType, CustomerDeliveryAddress request, User user) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();

		CustomerDeliveryAddress customerDeliveryAddress = null;

		if (!ValidationUtil.isNull(request.getId())) {
			Optional<CustomerDeliveryAddress> customerOptional = customerDeliveryAddressRepository
					.findById(request.getId());
			customerDeliveryAddress = customerOptional.get();
		}

		Customer customer = customerService.findById(user.getReferenceId()).get();
		if (null == customer) {
			errors.add(messageSource.getMessage("customer.not.found"));
		}

		Optional<City> cityOptional = null;
		if (ValidationUtil.isNull(request.getCityId())) {
			errors.add(messageSource.getMessage("city.not.found"));
		} else {
			cityOptional = cityService.findById(request.getCityId());
			if (!cityOptional.isPresent()) {
				errors.add(messageSource.getMessage("city.not.found"));
			}
		}

		if (ValidationUtil.isNullOrEmpty(request.getName())) {
			errors.add(messageSource.getMessage("customer.name.required"));
		}
		if (ValidationUtil.isValidPhoneNo(request.getMobileNo())) {
			if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
				errors.add(messageSource.getMessage("customer.mobile.no.required"));
			}
			errors.add(messageSource.getMessage("customer.invalid.mobile.number"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getLandMark())) {
			errors.add(messageSource.getMessage("customer.land.mark.required"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getAddressLine1())) {
			errors.add(messageSource.getMessage("customer.address.line1.required"));
		}
//		if (ValidationUtil.isNullOrEmpty(request.getAddressLine2())) {
//			errors.add(messageSource.getMessage("customer.address.line2.required"));
//		}
		if (ValidationUtil.isNullOrEmpty(request.getPostalCode())) {
			errors.add(messageSource.getMessage("customer.postal.code.required"));
		}
		if (ValidationUtil.isNullObject(request.getAddressType())) {
			errors.add(messageSource.getMessage("customer.address.type.required"));
		}
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		if (null == customerDeliveryAddress) {
			customerDeliveryAddress = CustomerDeliveryAddress.builder().cityObj(cityOptional.get())
					.name(request.getName()).location(request.getLocation()).latitude(request.getLatitude())
					.longitude(request.getLongitude()).mobileNo(request.getMobileNo()).customer(customer)
					.addressType(request.getAddressType()).addressLine1(request.getAddressLine1())
					.addressLine2(request.getAddressLine2()).postalCode(request.getPostalCode())
					.landMark(request.getLandMark()).build();
		} else {
			customerDeliveryAddress.setName(request.getName());
			customerDeliveryAddress.setMobileNo(request.getMobileNo());
			customerDeliveryAddress.setAddressType(request.getAddressType());
			customerDeliveryAddress.setLandMark(request.getLandMark());
			customerDeliveryAddress.setAddressLine1(request.getAddressLine1());
			customerDeliveryAddress.setAddressLine2(request.getAddressLine2());
			customerDeliveryAddress.setLatitude(request.getLatitude());
			customerDeliveryAddress.setLongitude(request.getLongitude());
			customerDeliveryAddress.setLocation(request.getLocation());
			customerDeliveryAddress.setPostalCode(request.getPostalCode());
			customerDeliveryAddress.setCityObj(cityOptional.get());
			customerDeliveryAddress.setCustomer(customer);
		}
		result.setObject(customerDeliveryAddress);
		return result;
	}
}
