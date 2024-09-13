package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.OfferDetailsDTO;
import com.app.entity.OfferDetails;
import com.app.enumeration.RequestType;
import com.app.service.MessagePropertyService;
import com.app.service.OfferDetailsService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class OfferDetailsValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull OfferDetailsService offerDetailsService;

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
	public ValidationResult validate(RequestType requestType, OfferDetailsDTO request) {

		errors = new ArrayList<>();
		OfferDetails offerDetails = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
		}else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<OfferDetails> offerDetailsOptional = offerDetailsService.findById(request.getId());
			if (!offerDetailsOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("offer.details.not.found"));
			}
			offerDetails = offerDetailsOptional.get();
		}
		if (ValidationUtil.isNullOrEmpty(request.getOfferCode())) {
			errors.add(messageSource.getMessage("offer.code.required"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getOfferName())) {
			errors.add(messageSource.getMessage("offer.name.required"));
		}
		if (ValidationUtil.isNull(request.getOfferValue())) {
			errors.add(messageSource.getMessage("offer.value.required"));
		}
		if (ValidationUtil.isNull(request.getMinimumOrderAmount())) {
			errors.add(messageSource.getMessage("offer.minimum.order.amount.required"));
		}		
		if (ValidationUtil.isNullOrEmpty(request.getTermsAndConditions())) {
			errors.add(messageSource.getMessage("offer.terms.conditions.required"));
		}
		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == offerDetails) {
			offerDetails = OfferDetails.builder().offerCode(request.getOfferCode()).offerName(request.getOfferName())
					.startDateAndTime(request.getStartDateAndTime()).expiryDateAndTime(request.getExpiryDateAndTime())
					.offerType(request.getOfferType()).offerValue(request.getOfferValue())
					.minimumOrderAmount(request.getMinimumOrderAmount())
					.termsAndConditions(request.getTermsAndConditions()).build();
		} else {
			offerDetails.setOfferName(request.getOfferName());
			offerDetails.setOfferCode(request.getOfferCode());
			offerDetails.setStartDateAndTime(request.getStartDateAndTime());
			offerDetails.setExpiryDateAndTime(request.getExpiryDateAndTime());
			offerDetails.setOfferType(request.getOfferType());
			offerDetails.setOfferValue(request.getOfferValue());
			offerDetails.setMinimumOrderAmount(request.getMinimumOrderAmount());
			offerDetails.setTermsAndConditions(request.getTermsAndConditions());
		}
		result.setObject(offerDetails);
		return result;
	}
}