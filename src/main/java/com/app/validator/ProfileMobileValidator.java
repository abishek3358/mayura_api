package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.ProfileMobileDTO;
import com.app.entity.ProfileMobile;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.service.MessagePropertyService;
import com.app.service.ProfileMobileService;
import com.app.service.UserService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProfileMobileValidator {
	@Autowired
	MessagePropertyService messageSource;

	private @NonNull ProfileMobileService profileMobileService;

	private @NonNull UserService userService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for UnitOfMeasurement validation Added by Ulaganathan
	 * 
	 * @param user
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, ProfileMobileDTO request, User user) {

		errors = new ArrayList<>();
		ProfileMobile profileMobile = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

			Optional<ProfileMobile> mobileDuplicateObj = profileMobileService.findByMobileNo(request.getMobileNo(),
					user.getReferenceId());
			if (mobileDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getMobileNo() };
				errors.add(messageSource.getMessage("profile.mobile.duplicate", params));
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<ProfileMobile> mobileDuplicateObj = profileMobileService.findByMobileNo(request.getMobileNo(),
					user.getReferenceId());
			if (mobileDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getMobileNo() };
				errors.add(messageSource.getMessage("profile.mobile.duplicate", params));
			}

			Optional<ProfileMobile> customerMobileOptional = profileMobileService.findById(request.getId());
			if (!customerMobileOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("profile.mobile.not.found"));
			}
			profileMobile = customerMobileOptional.get();
		}
		if (ValidationUtil.isValidPhoneNo(request.getMobileNo())) {
			if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
				errors.add(messageSource.getMessage("mobile.number.required"));
			}
			errors.add(messageSource.getMessage("valid.mobile.number.required"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == profileMobile) {
			profileMobile = ProfileMobile.builder().customerId(user.getReferenceId()).mobileNo(request.getMobileNo())
					.title(request.getTitle()).build();
		} else {
			profileMobile.setCustomerId(user.getReferenceId());
			profileMobile.setMobileNo(request.getMobileNo());
			profileMobile.setTitle(request.getTitle());

		}
		result.setObject(profileMobile);
		return result;
	}
}
