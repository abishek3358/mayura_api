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
import com.app.entity.ContactUs;
import com.app.entity.OfferDetails;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.service.ContactUsService;
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
public class ContactUsValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull ContactUsService contactUsService;

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
	public ValidationResult validate(RequestType requestType, ContactUs request,User user) {

		errors = new ArrayList<>();
		ContactUs contactUs = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
		}else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<ContactUs> contactUsOptional = contactUsService.findById(request.getId());
			if (!contactUsOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("offer.details.not.found"));
			}
			contactUs = contactUsOptional.get();
		}
		if (ValidationUtil.isNullOrEmpty(request.getName())) {
			errors.add(messageSource.getMessage("offer.code.required"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getEmail())) {
			errors.add(messageSource.getMessage("offer.name.required"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getSubject())) {
			errors.add(messageSource.getMessage("offer.value.required"));
		}
		if (ValidationUtil.isNullOrEmpty(request.getMessage())) {
			errors.add(messageSource.getMessage("offer.minimum.order.amount.required"));
		}		
		
		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == contactUs) {
			contactUs = ContactUs.builder()
					.userId(user.getUserId())
					.name(request.getName()).email(request.getEmail())
					.subject(request.getSubject()).message(request.getMessage()).build();
		} else {
			contactUs.setUserId(user.getUserId());
			contactUs.setName(request.getName());
			contactUs.setEmail(request.getEmail());
			contactUs.setSubject(request.getSubject());;
			contactUs.setMessage(request.getMessage());			
		}
		result.setObject(contactUs);
		return result;
	}
}