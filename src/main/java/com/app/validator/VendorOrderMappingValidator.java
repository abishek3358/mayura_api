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
import com.app.dto.OrderCancelDTO;
import com.app.entity.CustomerOrder;
import com.app.entity.VendorOrderMapping;
import com.app.enumeration.RequestType;
import com.app.enumeration.VendorOrderStatus;
import com.app.repository.VendorOrderMappingRepository;
import com.app.service.CustomerOrderService;
import com.app.service.MessagePropertyService;
import com.app.service.VendorOrderMappingService;
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
public class VendorOrderMappingValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull VendorOrderMappingService vendorOrderMappingService;
	private @NonNull VendorOrderMappingRepository vendorOrderMappingRepository;
	private @NonNull CustomerOrderService customerOrderService;
	private @NonNull VendorService vendorService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for UnitOfMeasurement validation Added by Ulaganathan
	 * 
	 * @param vendorId
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, VendorOrderMapping request) {

		errors = new ArrayList<>();
		VendorOrderMapping vendorOrderMapping = null;
		
		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<VendorOrderMapping> vendorOrderMappingOptional = vendorOrderMappingService
					.findById(request.getId());
			if (!vendorOrderMappingOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("vendor.contact.person.not.found"));
			}
			
			else  {
				if (vendorOrderMappingOptional.isPresent()) {
					throw new ObjectInvalidException(messageSource.getMessage("vendor.contact.person.not.found"));
				}
				vendorOrderMapping = vendorOrderMappingOptional.get();
			}
		}
		if (ValidationUtil.isNullObject(request.getVendorId())) {
			errors.add(messageSource.getMessage("vendor.id.required"));
		}
		if (ValidationUtil.isNullObject(request.getCustomerId())) {
			errors.add(messageSource.getMessage("customer.id.required"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == vendorOrderMapping) {
			vendorOrderMapping = VendorOrderMapping.builder()
					.vendorId(request.getVendorId())
					.customerId(request.getCustomerId())
					.customerOrderId(request.getCustomerOrderId())
					.vendorOrderStatus(VendorOrderStatus.ALLOCATED)				
					.build();
		} else {			
			vendorOrderMapping.setVendorId(request.getVendorId());
			vendorOrderMapping.setCustomerId(request.getCustomerId());	
			vendorOrderMapping.setCustomerOrderId(request.getCustomerOrderId());
			vendorOrderMapping.setVendorOrderStatus(VendorOrderStatus.ALLOCATED);
			vendorOrderMapping.setVendorOrderStatus(request.getVendorOrderStatus());

		}
		result.setObject(vendorOrderMapping);
		return result;
	}

	public ValidationResult ratingValidate(RequestType requestType, VendorOrderMapping request, UUID referenceId) {
		VendorOrderMapping vendorOrderMappingRate = null;
		ValidationResult result = new ValidationResult();
		if (requestType.equals(RequestType.PUT)) {
			if (ValidationUtil.isNull(referenceId)) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
		}
		Optional<CustomerOrder> customerOrderOptional = customerOrderService.getByCustomerOrderId(request.getCustomerOrderId());
		CustomerOrder customerOrder = customerOrderOptional.get();
		if (!referenceId.equals(customerOrder.getCustomerId())) {
			throw new ObjectInvalidException(messageSource.getMessage("vendor.order.invalid.customer"));
		}		
		Optional<VendorOrderMapping> vendorOrderMappingOptional = vendorOrderMappingRepository.findByCustomerOrderId(request.getCustomerOrderId());
		vendorOrderMappingRate = vendorOrderMappingOptional.get();
		vendorOrderMappingRate.setRatings(request.getRatings());

		result.setObject(vendorOrderMappingRate);
		return result;
	}

		public ValidationResult validateVendorReject(RequestType requestType,OrderCancelDTO request) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();

		

		Optional<VendorOrderMapping> vendorOrderMappingOptional = vendorOrderMappingRepository.findByCustomerOrderId(request.getCustomerOrderId());
		
		if (!vendorOrderMappingOptional.isPresent()) {
			throw new ObjectInvalidException(messageSource.getMessage("customer.order.not.found"));
		}
		VendorOrderMapping vendorOrderMappingObj = vendorOrderMappingOptional.get();
		 if (vendorOrderMappingObj.getVendorOrderStatus().equals(VendorOrderStatus.DELIVERED)){
				errors.add(messageSource.getMessage("invalid.cancel"));
			}
		if (vendorOrderMappingObj.getVendorOrderStatus().equals(VendorOrderStatus.ALLOCATED)){

			//vendorOrderMappingObj.setStatus(Status.INACTIVE);
			vendorOrderMappingObj.setRejectionReason(request.getReason());
			vendorOrderMappingObj.setVendorOrderStatus(VendorOrderStatus.REJECTED);
			//vendorOrderMappingObj.setOrderCancelledDate(new Date());
		}

		result.setObject(vendorOrderMappingObj);
		return result;

	}

}