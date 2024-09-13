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
import com.app.dto.SignUpDTO;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.enumeration.UserRoleType;
import com.app.repository.RoleRepository;
import com.app.service.MessagePropertyService;
import com.app.service.UserService;
import com.app.util.IConstant;
import com.app.util.PasswordUtil;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class UserValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull UserService userService;

	private @NonNull RoleRepository roleRepository;

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

	public ValidationResult validate(RequestType requestType, SignUpDTO request) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();
		User user = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getUserId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			if (ValidationUtil.isNullOrEmpty(request.getFullName())) {
				errors.add(messageSource.getMessage("full.name.required"));
			} else {
				request.setFullName(ValidationUtil.getFormattedString(request.getFullName()));
				if (!ValidationUtil.isValidName(request.getFullName())) {
					errors.add(messageSource.getMessage("full.name.invalid"));
				}
			}
			Optional<User> userOptional = userService.getByMobileNo(request.getMobileNo());
			if (userOptional.isPresent()) {
				String[] params = new String[] { request.getMobileNo() };
				errors.add(messageSource.getMessage("user.mobile.duplicate", params));
			}
			Optional<User> userDuplicateObj = userService.findByUserName(request.getUserName());
			if (userDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getUserName() };
				errors.add(messageSource.getMessage("user.name.duplicate", params));
			}
			Optional<User> userDuplicateMailObj = userService.findByUserEmail(request.getEmail());
			if (userDuplicateMailObj.isPresent()) {				
				errors.add(messageSource.getMessage("user.email.duplicate"));
			}
		} else {
			if (ValidationUtil.isNull(request.getUserId()))
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));

			Optional<User> userOptional = userService.findById(request.getUserId());
			if (!userOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("user.not.found"));
			}
			user = userOptional.get();
		}
		if (ValidationUtil.isNullOrEmpty(request.getFullName())) {
			errors.add(messageSource.getMessage("full.name.required"));
		} else {
			request.setFullName(ValidationUtil.getFormattedString(request.getFullName()));
			if (!ValidationUtil.isValidName(request.getFullName())) {
				errors.add(messageSource.getMessage("full.name.invalid"));
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getEmail())) {
			errors.add(messageSource.getMessage("user.email.required"));
		} else {
			request.setEmail(ValidationUtil.getFormattedString(request.getEmail()));
			if (!ValidationUtil.isValidEmailId(request.getEmail())) {
				errors.add(messageSource.getMessage("user.email.invalid"));
			}
		}
		if (ValidationUtil.isNullObject(request.getUserRole())) {
			errors.add(messageSource.getMessage("user.type.required"));
		}
		if (ValidationUtil.isValidPhoneNo(request.getMobileNo())) {
		if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
			errors.add(messageSource.getMessage("user.phone.no.required"));
		}
		errors.add(messageSource.getMessage("user.phone.number.invalid"));
		}else {
			request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
			if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
				errors.add(messageSource.getMessage("user.phone.invalid"));
			}
		}		
		if (ValidationUtil.isNullOrEmpty(request.getPassword())) {
			errors.add(messageSource.getMessage("password.required"));
		}
		if (!request.getPassword().equals(request.getConfirmPassword())) {
			errors.add(messageSource.getMessage("password.mismatch"));
		}
		String encrptPassword = PasswordUtil.getEncryptedPassword(request.getPassword());
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == user) {
			String admin="ADMIN";
			Optional<User> userOption=userService.findByUserRoleType(admin);
			
			Optional<Role> role = null;			
			role = roleRepository.findByRoleName(IConstant.ROLE_USER);	
			user = User.builder().fullName(request.getFullName()).isDeleted(false).isLocked(false).isOtpVerified(false)
					.forcePasswordChange(false).userRole(request.getUserRole()).email(request.getEmail())
					.userName(request.getUserName()).password(encrptPassword).mobileNo(request.getMobileNo()).roleObj(role.get())
					.referenceId(userOption.get().getReferenceId()).build();
		} else {
			user.setFullName(request.getFullName());
			user.setIsDeleted(false);
			user.setIsLocked(false);
			user.setIsOtpVerified(false);
			user.setForcePasswordChange(false);
			user.setUserRole(request.getUserRole());
			user.setEmail(request.getEmail());
			user.setMobileNo(request.getMobileNo());
			user.setStatus(request.getStatus());
		}
		result.setObject(user);
		return result;
	}
}
