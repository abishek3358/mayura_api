package com.app.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.app.dto.ErrorDto;
import com.app.dto.LoginRequest;
import com.app.dto.LoginResponse;
import com.app.dto.OtpDTO;
import com.app.dto.PasswordUpdateDTO;
import com.app.dto.RoleDTO;
import com.app.dto.SignUpDTO;
import com.app.dto.UpdatePasswordDTO;
import com.app.dto.ValidateOtpDTO;
import com.app.email.helper.MailHelper;
import com.app.entity.Customer;
import com.app.entity.OneTimePassword;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.enumeration.Status;
import com.app.repository.OneTimePasswordRepository;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.security.JwtTokenUtil;
import com.app.service.CustomerService;
import com.app.service.MessagePropertyService;
import com.app.service.UserService;
import com.app.util.PasswordUtil;
import com.app.util.message.ResponseMessage;
import com.app.validator.SignUpValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Api(value = "Authorization Rest API", description = "Defines endpoints that can be hit only when the user is not logged in. It's not secured by default.")
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class AuthenticationController {
	private static final Logger logger = Logger.getLogger(AuthenticationController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull UserRepository userRepository;

	private @NonNull UserService userservice;

	private @NonNull OneTimePasswordRepository otpRepository;

	private @NonNull JwtTokenUtil jwtTokenUtil;

	private @NonNull MessageSource messageSource;

	private @NonNull MessagePropertyService messagePropertySource;

	private @NonNull ApplicationEventPublisher applicationEventPublisher;

	private @NonNull SignUpValidator validatorService;

	private @NonNull CustomerService customerService;
	
	private @NonNull OneTimePasswordRepository oneTimePasswordRepository;

	@Autowired
	JavaMailSender mailSender;

	@Autowired(required = true)
	@NonNull
	MailHelper mailHelper;

	@ApiOperation(value = "Logs the user in to the system and return the auth tokens")
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> login(@ApiParam(value = "The LoginRequest payload") @RequestBody LoginRequest request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		ErrorDto errorDto = null;
		Map<String, Object> response = new HashMap<String, Object>();
		if (null == request) {
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Invalid request payload.!");
			response.put("status", 0);
			response.put("error", errorDto);
			return ResponseEntity.badRequest().body(response);
		}

		Optional<User> userOptional = userRepository.findByUserName(request.getUserName());		
		if (!userOptional.isPresent()) {			
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Invalid username.!");
			response.put("status", 0);
			response.put("error", errorDto);
			return ResponseEntity.badRequest().body(response);
		}
		User user = userOptional.get();
		String enPassword = PasswordUtil.getEncryptedPassword(request.getPassword());
		
		if(!user.getUserName().equals(request.getUserName())) {
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Invalid username.!");
			response.put("status", 0);
			response.put("error", errorDto);
			return ResponseEntity.badRequest().body(response);
		}
		if (!user.getPassword().equals(enPassword)) {
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Password is wrong.!");
			response.put("status", 0);
			response.put("error", errorDto);
			return ResponseEntity.badRequest().body(response);
		}

		final String token = jwtTokenUtil.generateToken(user);
		response.put("status", 1);
		response.put("message", "Logged in successfully.!");
		response.put("jwt", token);
		response.put("isOtpVerified", user.getIsOtpVerified());
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		/*
		 * logger.info(response.toString()); return
		 * ResponseEntity.accepted().body(response);
		 */
	}

	@ApiOperation(value = "Get logged in user information based from JWT")
	@RequestMapping(value = "/user-details", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getUserDetails(@RequestHeader HttpHeaders httpHeader, Principal principle)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//		ErrorDto errorDto = null;
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<User> userOptional = userRepository.findByUserName(principle.getName());
		User user = userOptional.get();
		List<RoleDTO> roles = new ArrayList<RoleDTO>();
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setFullName(user.getFullName());
		loginResponse.setPhone(user.getMobileNo());
		loginResponse.setEmail(user.getEmail());
		loginResponse.setUserId(user.getUserId());
		loginResponse.setUserName(user.getUserName());
		loginResponse.setReferenceId(user.getReferenceId());
		loginResponse.setForcePasswordChange(user.getForcePasswordChange());

		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleId(user.getRoleObj().getId());
		roleDto.setRoleName(user.getRoleObj().getRoleName());
		loginResponse.setRoleObj(roleDto);
		response.put("isCustomer", true);
		response.put("loginObj", loginResponse);
		try {
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		/*
		 * logger.info(response.toString()); return
		 * ResponseEntity.accepted().body(response);
		 */
	}

	//neww
	@ApiOperation(value = "Allows to send new password", response = Response.class)
	@PutMapping(value = "/send/password", produces = "application/json")
	public ResponseEntity<?> sendPassword(@ApiParam(value = "otpDto request payload") @RequestBody OtpDTO request,
	                                      @RequestHeader HttpHeaders httpHeader) {
	    TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

	    try {
	        if (request.getUserNameOrEmail() == null || request.getUserNameOrEmail().isEmpty()) {
	            return responseGenerator.errorResponse(context, messagePropertySource.getMessage("payload.invalid"),
	                    HttpStatus.BAD_REQUEST);
	        }

	        Optional<User> userOptional = userRepository.findByUserNameOrPassword(request.getUserNameOrEmail());
	        if (userOptional.isEmpty()) {
	            return responseGenerator.errorResponse(context, messagePropertySource.getMessage("username.email.invalid"),
	                    HttpStatus.BAD_REQUEST);
	        }
	        User userObj = userOptional.get();

	        String generatedPassword = generateNewPassword();
	        String passcode = generatedPassword.substring(generatedPassword.length() / 2);
	        String mail_subject = "Verification for code ";
	        String mail_template = "password/passwordSend";

	        // Send email
	        Context emailContext = new Context();
	        emailContext.setVariable("password", passcode.toString());
	        mailHelper.sendMail(userObj.getEmail(), null, mail_subject, mail_template, emailContext);

	        userObj.setPassword(PasswordUtil.getEncryptedPassword(passcode));
	        userObj.setForcePasswordChange(true);
	        userRepository.saveAndFlush(userObj);

	        // Update OneTimePassword
	        Optional<OneTimePassword> otpOptional = otpRepository.findByUserNameOrEmail(userObj.getMobileNo());
	        if (otpOptional.isPresent()) {
	            OneTimePassword oneTimePasswordObj = otpOptional.get();
	            oneTimePasswordObj.setOtp(passcode);
	            oneTimePasswordObj.setStatus(Status.INACTIVE);
	            otpRepository.saveAndFlush(oneTimePasswordObj);
	        }

	        return responseGenerator.successResponse(context, messagePropertySource.getMessage("password.send"),
	                HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("Error occurred while sending password: " + e.getMessage(), e);
	        return responseGenerator.errorResponse(context, "Error occurred while sending password", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	private String generateNewPassword() {
	    String password1 = RandomString.make(4);
	    Random r = new Random();
	    Integer password2 = 100000 + r.nextInt(900000);
	    return password1 + password2.toString();
	}

	@RequestMapping(value = "/password/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<?> passwordUpdate(@RequestBody UpdatePasswordDTO updatePasswordDTO,
	        @RequestHeader HttpHeaders httpHeader) throws Exception {
	    TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

	    // Verify OTP
	    List<OneTimePassword> verifyOTPList = oneTimePasswordRepository.findByOtp(updatePasswordDTO.getOtp());

	    if (verifyOTPList.isEmpty()) {
	        return responseGenerator.errorResponse(context, "Invalid OTP", HttpStatus.BAD_REQUEST);
	    }

	    OneTimePassword otpEntity = verifyOTPList.get(0);

	    // Update password
	    Optional<User> userOptional = userRepository.findByUsersName(otpEntity.getMobileNo());

	    if (userOptional.isPresent()) {
	        User userObj = userOptional.get();
	        userObj.setPassword(PasswordUtil.getEncryptedPassword(updatePasswordDTO.getNewPassword()));
	        userObj.setForcePasswordChange(false);
	        userObj.setModifiedOn(new Date());
	        userRepository.saveAndFlush(userObj);

	        // Delete the used OTP
	     //   oneTimePasswordRepository.delete(otpEntity);

	        return responseGenerator.successResponse(context, "Password updated successfully", HttpStatus.OK);
	    } else {
	        return responseGenerator.errorResponse(context, "User not found", HttpStatus.NOT_FOUND);
	    }
	}


	@ApiOperation(value = "Allows to change current password", response = Response.class)
	@PutMapping(value = "/change/password", produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "Payload for change current password ") @RequestBody PasswordUpdateDTO request,
			@RequestHeader HttpHeaders httpHeaders,Principal principle) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);		
		String userName = principle.getName();	
		
		Optional<User> userOptional = userservice.findByPasswordAndIsDeletedFalseAndIsLockedFalse(
				PasswordUtil.getEncryptedPassword(request.getOldPassword()),userName);
		
		if (!userOptional.isPresent()) {
			return responseGenerator.errorResponse(context, messagePropertySource.getMessage("invalid.user.password"),
					HttpStatus.BAD_REQUEST);
		}
		User userObj = userOptional.get();
		
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			return responseGenerator.errorResponse(context,
					messagePropertySource.getMessage("invalid.new.and.confirm.password"), HttpStatus.BAD_REQUEST);
		}
		
		 if (request.getConfirmPassword().equals(request.getOldPassword() )){
			return responseGenerator.errorResponse(context,
					messagePropertySource.getMessage("invalid.old.and.new.password"), HttpStatus.BAD_REQUEST);
		}
		 
		 
		userObj.setPassword(PasswordUtil.getEncryptedPassword(request.getNewPassword()));
		userObj.setForcePasswordChange(false);
		userObj.setModifiedOn(new Date());		
		//userObj.setPassword(PasswordUtil.getEncryptedPassword(request.getNewPassword()));
		userservice.saveOrUpdate(userObj);
		try {
			return responseGenerator.successResponse(context, messagePropertySource.getMessage("password.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to signup new user")
	@RequestMapping(value = "/signup", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> signup(@ApiParam(value = "The Signup request payload") @RequestBody SignUpDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		Customer customer = (Customer) validationResult.getObject();
		User user = customerService.doSignUp(customer);

		Map<String, Object> response = new HashMap<String, Object>();
		final String token = jwtTokenUtil.generateToken(user);
		response.put("status", 1);
		response.put("message", "You have registered successfully.");
		response.put("jwt", token);
		response.put("isOtpVerified", false);
		response.put("otp", "12345");
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		/*
		 * logger.info(response.toString()); return
		 * ResponseEntity.accepted().body(response);
		 */
	}
	@ApiOperation(value = "Allows to validate customer using one time passsword")
	@RequestMapping(value = "/validate/otp", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> validateOTP(
			@ApiParam(value = "The Signup request payload") @RequestBody ValidateOtpDTO request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {

		Optional<User> userOptional = userRepository.findByUserName(principal.getName(),request.getMobileNo());
		User user = userOptional.get();
		ValidationResult validationResult = validatorService.validateOtp(RequestType.POST, request, user);
		OneTimePassword otpObj = (OneTimePassword) validationResult.getObject();
		customerService.validateCustomer(user, otpObj);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("status", 1);
		response.put("message", messagePropertySource.getMessage("validation.otp.verified"));
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successResponse(context, "OTP verified successfully.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		/*
		 * logger.info(response.toString()); return
		 * ResponseEntity.accepted().body(response);
		 */
	}
	
	
	@ApiOperation(value = "Allows to validate customer using one-time password")
	@RequestMapping(value = "/verify/otp", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> verifyOTP(
	        @ApiParam(value = "The Signup request payload") @RequestBody ValidateOtpDTO request,
	        @RequestHeader HttpHeaders httpHeader) throws Exception {

	    TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

	    try {
	        // Assuming OneTimePassword is a model/entity and you want to retrieve it from the database
	        List<OneTimePassword> verifyOTPList = oneTimePasswordRepository.findByOtp(request.getOtp());

	        if (verifyOTPList.isEmpty()) {
	            return responseGenerator.errorResponse(context, "Invalid OTP", HttpStatus.BAD_REQUEST);
	        }

	        OneTimePassword otpObj = verifyOTPList.get(0);

	        // Compare input payload value with OTP value in the OneTimePassword entity
	        if (otpObj.getOtp().equals(request.getOtp())) {
	            return responseGenerator.successResponse(context, "valid OTP", HttpStatus.OK);
	        } 
       	        Map<String, Object> response = new HashMap<>();
        response.put("status", 1);
        response.put("message", messagePropertySource.getMessage("validation.otp.verified"));
        return responseGenerator.successResponse(context, response, HttpStatus.OK); 
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error(e.getMessage(), e);
	        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	@ApiOperation(value = "Allows to resend one time password")
	@RequestMapping(value = "/resend/otp", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> resendOtp(@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());
		User user = userOptional.get();

		Optional<OneTimePassword> otpObj = otpRepository.findByMobileNoAndStatus(user.getMobileNo(), Status.ACTIVE);

		String otp = "";
		if (otpObj.isPresent()) {
			otp = otpObj.get().getOtp();
		} else {
			OneTimePassword oneTimePassword = OneTimePassword.builder().mobileNo(user.getMobileNo()).otp("12345")
					.userId(user.getUserId()).build();
			oneTimePassword.setStatus(Status.INACTIVE);
			otpRepository.saveAndFlush(oneTimePassword);
			otp = "12345";
			user.setIsOtpVerified(true);
			userRepository.saveAndFlush(user);
		}

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", "OTP resend successfully.");
		response.put("otp", otp);
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		/*
		 * logger.info(response.toString()); return
		 * ResponseEntity.accepted().body(response);
		 */
	}

	

	@RequestMapping(value = "/demo", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> demo(@RequestHeader HttpHeaders httpHeader) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("customerList", customerService.findAll());
		response.put("userList", userRepository.findAll());
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		/*
		 * logger.info(response.toString()); return
		 * ResponseEntity.accepted().body(response);
		 */
	}

//	@ApiOperation(value = " Allows to delete specific user.", response = Response.class)
//	@DeleteMapping(value = "/delete/{userId}", produces = "application/json")
//	public ResponseEntity<?> delete(@ApiParam(value = "User Id to be deleted") @PathVariable("userId") UUID userId,
//			 @RequestBody User request, @RequestHeader HttpHeaders httpHeader,Principal principal) throws Exception {
//		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//		
//		Optional<User> userOptional =userRepository.findByUserName(principal.getName());
//		{
//			User userObj = userOptional.get();
//			if (null == userObj) {
//				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_OBJECT_REFERENCE,
//						HttpStatus.BAD_REQUEST);
//			}
//			try {
//				userObj.setDeletedBy(userObj.getFullName());
//				userObj.setIsDeleted(userObj.getIsDeleted());			
//				userservice.deleteByName(userObj.getFullName());
//				return responseGenerator.successResponse(context, messagePropertySource.getMessage("user.delete"), HttpStatus.OK);
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(e.getMessage(), e);
//				return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//
//			}
//		}
//	}

}
