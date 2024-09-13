package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.SignUpDTO;
import com.app.dto.ValidateOtpDTO;
import com.app.entity.Customer;
import com.app.entity.OneTimePassword;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.enumeration.Status;
import com.app.enumeration.UserRoleType;
import com.app.repository.CustomerRepository;
import com.app.repository.OneTimePasswordRepository;
import com.app.repository.UserRepository;
import com.app.service.MessagePropertyService;
import com.app.service.UserService;
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
public class SignUpValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull OneTimePasswordRepository otpRepository;
	private @NonNull UserRepository userRepository;
	private @NonNull CustomerRepository customerRepository;

	private @NonNull UserService userService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for SignUpDTO validation Added by Ulaganathan
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, SignUpDTO request) {

		errors = new ArrayList<>();
		Customer customer = null;

		if (ValidationUtil.isNullOrEmpty(request.getFullName())) {
			errors.add(messageSource.getMessage("signup.full.name.required"));
		} else {
			request.setFullName(ValidationUtil.getFormattedString(request.getFullName()));
			if (!ValidationUtil.isValidName(request.getFullName())) {
				errors.add(messageSource.getMessage("signup.full.name.invalid"));
			}
		}

		if (ValidationUtil.isNullOrEmpty(request.getEmail())) {
			errors.add(messageSource.getMessage("signup.email.required"));
		} else {
			request.setEmail(ValidationUtil.getFormattedString(request.getEmail()));
			if (!ValidationUtil.isValidEmailId(request.getEmail())) {
				errors.add(messageSource.getMessage("signup.email.invalid"));
			}
		}

		if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
			errors.add(messageSource.getMessage("signup.mobile.required"));
		} else {
			request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
			if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
				errors.add(messageSource.getMessage("signup.mobile.invalid"));
			}
		}

		if (ValidationUtil.isNullOrEmpty(request.getPassword())) {
			errors.add(messageSource.getMessage("signup.password.required"));
		} else {
			request.setPassword(ValidationUtil.getFormattedString(request.getPassword()));

		}
		Optional<User> userDuplicateMailObj = userService.findByDuplicateEmail(request.getEmail());
		if (userDuplicateMailObj.isPresent()) {
			// String[] params = new String[] { request.getEmail() };
			errors.add(messageSource.getMessage("customer.signup.email.duplicate"));
		}

		Optional<User> userDuplicateObj = userService.findByDuplicateUserName(request.getUserName());
		if (userDuplicateObj.isPresent()) {
			String[] params = new String[] { request.getUserName() };
			errors.add(messageSource.getMessage("user.name.duplicate", params));
		}

		String enPassword = PasswordUtil.getEncryptedPassword(request.getPassword());

		Optional<User> userExist = userRepository.getByMobileNos(request.getMobileNo());
		if (userExist.isPresent()) {
			String[] params = new String[] { request.getMobileNo() };
			errors.add(messageSource.getMessage("signup.mobileno.exist", params));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		customer = Customer.builder().fullName(request.getFullName()).email(request.getEmail())
				.userName(request.getUserName()).mobileNo(request.getMobileNo()).password(enPassword).build();

		result.setObject(customer);
		return result;
	}

	/**
	 * method for ValidateOtpDTO validation Added by Ulaganathan
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validateOtp(RequestType requestType, ValidateOtpDTO request, User user) {
		errors = new ArrayList<>();

		if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
			errors.add(messageSource.getMessage("signup.mobile.required"));
		} else {
			request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
			if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
				errors.add(messageSource.getMessage("signup.mobile.invalid"));
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getOtp())) {
			errors.add(messageSource.getMessage("validation.otp.required"));
		} else {
			request.setOtp(ValidationUtil.getFormattedString(request.getOtp()));
		}

		Optional<OneTimePassword> otpObj = otpRepository.findByMobileNoAndStatus(request.getMobileNo(), Status.ACTIVE);

		if (!otpObj.isPresent()) {
			errors.add(messageSource.getMessage("validation.otp.invalid"));
		}

		if (!otpObj.get().getOtp().equals(request.getOtp())) {
			errors.add(messageSource.getMessage("validation.otp.wrong"));
		}
		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		result.setObject(otpObj.get());
		return result;
	}

}
