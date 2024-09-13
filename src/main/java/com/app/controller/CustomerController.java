
package com.app.controller;

import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.advice.ObjectInvalidException;
import com.app.dto.CustomerDTO;
import com.app.dto.CustomerRewardDTO;
import com.app.dto.CustomerRewardHistoryDTO;
import com.app.dto.PasswordUpdateDTO;
import com.app.dto.ProfileMobileDTO;
import com.app.dto.UserProfileDTO;
import com.app.entity.City;
import com.app.entity.ContactUs;
import com.app.entity.Customer;
import com.app.entity.CustomerDeliveryAddress;
import com.app.entity.CustomerReward;
import com.app.entity.CustomerRewardHistory;
import com.app.entity.ProfileMobile;
import com.app.entity.User;
import com.app.enumeration.ImageType;
import com.app.enumeration.MobileType;
import com.app.enumeration.RequestType;
import com.app.enumeration.Status;
import com.app.repository.CustomerRepository;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.CityService;
import com.app.service.ContactUsService;
import com.app.service.CountryService;
import com.app.service.CustomerDeliveryAddressService;
import com.app.service.CustomerRewardHistoryService;
import com.app.service.CustomerRewardService;
import com.app.service.CustomerService;
import com.app.service.FileService;
import com.app.service.MessagePropertyService;
import com.app.service.ProfileMobileService;
import com.app.service.StateService;
import com.app.service.UserService;
import com.app.util.PasswordUtil;
import com.app.util.ValidationUtil;
import com.app.util.file.AWSS3DirectoryUtil;
import com.app.util.message.ResponseMessage;
import com.app.validator.ContactUsValidator;
import com.app.validator.CustomerRewardValidator;
import com.app.validator.CustomerValidator;
import com.app.validator.ProfileMobileValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/customer")
@Api(value = "customer Rest API", produces = "application/json", consumes = "application/json")
public class CustomerController {

	private static final Logger logger = Logger.getLogger(CustomerController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull CustomerDeliveryAddressService customerDeliveryAddressService;

	private @NonNull CustomerService customerService;

	private @NonNull CustomerValidator validatorService;

	private @NonNull UserRepository userRepository;

	private @NonNull UserService userService;

	private @NonNull CountryService countryService;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull ContactUsService contactUsService;

	private @NonNull ContactUsValidator contactUsValidator;

	private @NonNull CustomerRepository customerRepository;

	private @NonNull ProfileMobileService profileMobileService;

	private @NonNull ProfileMobileValidator profileMobileValidator;

	private @NonNull FileService fileUploadService;

	private @NonNull CustomerRewardValidator customerRewardValidator;

	private @NonNull CustomerRewardService customerRewardService;

	private @NonNull CustomerRewardHistoryService customerRewardHistoryService;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new delivery address.", response = Response.class)
	@PostMapping(value = "/delivery-address/create", produces = "application/json")
	public ResponseEntity<?> createDeliveryAddress(
			@ApiParam(value = "The customer delivery address request payload") @RequestBody CustomerDeliveryAddress request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {

			Optional<User> userOptional = userRepository.findByUserName(principal.getName());

			ValidationResult validationResult = validatorService.validate(RequestType.POST, request,
					userOptional.get());
			customerDeliveryAddressService.saveOrUpdate((CustomerDeliveryAddress) validationResult.getObject());

			return responseGenerator.successResponse(context, messageSource.getMessage("customer.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing customer delivery address.", response = Response.class)
	@PutMapping(value = "/delivery-address/update", produces = "application/json")
	public ResponseEntity<?> updateDeliveryAddress(
			@ApiParam(value = "The customer delivery address request payload") @RequestBody CustomerDeliveryAddress request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<User> userOptional = userRepository.findByUserName(principal.getName());
			ValidationResult validationResult = validatorService.validate(RequestType.PUT, request, userOptional.get());
			customerDeliveryAddressService.saveOrUpdate((CustomerDeliveryAddress) validationResult.getObject());
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.address.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch logged in customer delivery address.", response = Response.class)
	@GetMapping(value = "/delivery-address/get", produces = "application/json")
	public ResponseEntity<?> getAllDeliveryAddress(@RequestHeader HttpHeaders httpHeader, Principal principal)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		
		try {
			Optional<User> userOptional = userRepository.findByUserName(principal.getName());
			Optional<Customer> customerOptional = customerService.findById(userOptional.get().getReferenceId());
			Customer customer = customerOptional.get();
			List<CustomerDeliveryAddress> customerDeliveryAddressList = customerDeliveryAddressService
					.findByCustomerAndStatus(customer,Status.ACTIVE);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.get"),
					customerDeliveryAddressList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch  particular delivery address.", response = Response.class)
	@GetMapping(value = "/delivery-address/get/{deliveryAddressId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("deliveryAddressId") UUID deliveryAddressId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {
			Optional<CustomerDeliveryAddress> customerdelivery = customerDeliveryAddressService
					.findById(deliveryAddressId);

			if (!customerdelivery.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}

			CustomerDeliveryAddress customerDeliveryAddressObj = customerdelivery.get();

			City cityObj = cityService.findById(customerDeliveryAddressObj.getCityObj().getId()).get();
			customerDeliveryAddressObj.setCityId(cityObj.getId());
			customerDeliveryAddressObj.setCityName(cityObj.getName());
			customerDeliveryAddressObj.setStateId(cityObj.getStateId());
			customerDeliveryAddressObj.setStateName(stateService.findById(cityObj.getStateId()).get().getName());
			customerDeliveryAddressObj.setCountryId(cityObj.getCountryId());
			customerDeliveryAddressObj.setCountryName(countryService.findById(cityObj.getCountryId()).get().getName());

			return responseGenerator.successResponse(context, customerDeliveryAddressObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete Customer Delivery Address by Customer Delivery Address id.", response = Response.class)
	@DeleteMapping(value = "/delivery-address/delete/{deliveryAddressId}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("deliveryAddressId") UUID deliveryAddressId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		
		Optional<CustomerDeliveryAddress> customerdelivery = customerDeliveryAddressService
				.findById(deliveryAddressId);
		CustomerDeliveryAddress obj = customerdelivery.get();
		obj.setStatus(Status.INACTIVE);
		customerDeliveryAddressService.saveOrUpdate(obj);
		try {
			
			return responseGenerator.successResponse(context,
					messageSource.getMessage("customer.delivery.address.delete"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context,
					messageSource.getMessage("customer.delivery.address.invalid.delete"), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all Customer Details", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		List<CustomerDTO> responseObjList = new ArrayList<CustomerDTO>();
		CustomerDTO dto = null;
		for (Customer customerDtoObj : customerService.findAll()) {
			dto = new CustomerDTO();
			dto.setId(customerDtoObj.getId());
			dto.setFullName(customerDtoObj.getFullName());
			dto.setEmail(customerDtoObj.getEmail());
			dto.setMobileNo(customerDtoObj.getMobileNo());
			dto.setDeliveryAddressList(customerDtoObj.getDeliveryAddressList());
			responseObjList.add(dto);
		}
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.get"),
					responseObjList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch user profile details by user.", response = Response.class)
	@GetMapping(value = "/user/profile/get", produces = "application/json")
	public ResponseEntity<?> get(@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());

		User userObj = userOptional.get();

		String url = AWSS3DirectoryUtil.getBaseUrl() + "customer/download";

		// String url = "api/customer/download";

		UserProfileDTO profileDto = new UserProfileDTO();
		profileDto.setUserId(userObj.getUserId());
		profileDto.setFullName(userObj.getFullName());
		profileDto.setEmail(userObj.getEmail());
		profileDto.setMobileNo(userObj.getMobileNo());
		profileDto.setMobileNoList(profileMobileService.findByCustomerId(userObj.getReferenceId()));		
		String img = "";
		if (userObj.getFileName() != null) {
			img = userObj.getFileName().toString();
		}
		profileDto.setFileName(img);
		//obj.setFileName(img);		
		//profileDto.setFileUrl(url + "/" + userObj.getUserId());
		 profileDto.setFileUrl(AWSS3DirectoryUtil.getProfileImageUrl(userObj.getUserId(), img));

		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("user.profile.get"),
					profileDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allow to  update user profile ", response = Response.class)
	@PutMapping(value = "/user/profile/update", produces = "application/json")
	public ResponseEntity<?> updateOrderStatusToCompleted(
			@ApiParam(value = "The customer order's change order status request payload") @RequestBody UserProfileDTO request,
			Principal principal, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		List<String> errors = null;
		errors = new ArrayList<String>();

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());

		if (!userOptional.isPresent()) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}
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

		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		User userObj = userOptional.get();
		Optional<Customer> customerOptional = customerService.findById(userObj.getReferenceId());
		Customer customerObj = customerOptional.get();
		customerObj.setFullName(request.getFullName());
		customerObj.setEmail(request.getEmail());
		customerObj.setMobileNo(request.getMobileNo());
		customerRepository.saveAndFlush(customerObj);

		userObj.setFullName(request.getFullName());
		userObj.setEmail(request.getEmail());
		userObj.setMobileNo(request.getMobileNo());
		userObj.setFileName(request.getFileName());
		if (!ValidationUtil.isNullOrEmpty(request.getFileBase64())) {
			userObj.setFileUrl(AWSS3DirectoryUtil.getProfileImageUrl(request.getUserId(), request.getFileName()));
			fileUploadService.uploadImage(ImageType.PROFILE, request.getUserId(), request.getFileName(),
					request.getFileBase64());
		}
		userRepository.saveAndFlush(userObj);
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("user.profile.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch student document dwonloads.", response = Response.class)
	@GetMapping(value = "/download/{userId}", produces = "application/json")
	public ResponseEntity<?> download(@PathVariable("userId") UUID userId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> product = userService.findById(userId);
		if (!product.isPresent()) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}

		// List<ProductImage> productImageList =
		// productImageRepository.findByProduct(product);
		// for(ProductImage obj:productImageList) {
		byte[] data = fileUploadService.downloadImage(ImageType.PROFILE, userId, product.get().getFileName());

		String fileNameText = product.get().getFileName();
		String fileName = URLEncoder.encode(fileNameText, "UTF-8").replaceAll("\\+", "%20");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(data.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);
		httpHeaders.set("fileName", fileName);
		httpHeaders.add("fileName", fileName);

		try {
			return new ResponseEntity<>(data, httpHeaders, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to change current password", response = Response.class)
	@PutMapping(value = "/change/password", produces = "application/json")
	public ResponseEntity<?> update(
			@ApiParam(value = "Payload for change current password ") @RequestBody PasswordUpdateDTO request,
			@RequestHeader HttpHeaders httpHeaders, Principal principal) throws Exception {

		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		Optional<User> userOptional = userService.findByUserNameAndPasswordAndIsDeletedFalseAndIsLockedFalse(
				principal.getName(), PasswordUtil.getEncryptedPassword(request.getOldPassword()));

		if (ValidationUtil.isNullOrEmpty(request.getOldPassword())) {
			return responseGenerator.errorResponse(context, messageSource.getMessage("old.password.required"),
					HttpStatus.BAD_REQUEST);
		} else {
			request.setOldPassword(ValidationUtil.getFormattedString(request.getOldPassword()));
			/*
			 * if (!ValidationUtil.isValidCharacters(request.getNewPassword())) {
			 * errors.add(messageSource.getMessage("gst.name.invalid")); }
			 */
		}

		if (ValidationUtil.isNullOrEmpty(request.getNewPassword())) {
			return responseGenerator.errorResponse(context, messageSource.getMessage("new.password.required"),
					HttpStatus.BAD_REQUEST);
		} else {
			request.setNewPassword(ValidationUtil.getFormattedString(request.getNewPassword()));
			/*
			 * if (!ValidationUtil.isValidCharacters(request.getNewPassword())) {
			 * errors.add(messageSource.getMessage("gst.name.invalid")); }
			 */
		}

		if (!userOptional.isPresent()) {
			return responseGenerator.errorResponse(context, messageSource.getMessage("invalid.old.password"),
					HttpStatus.BAD_REQUEST);
		}

		User userObj = userOptional.get();
		userObj.setPassword(PasswordUtil.getEncryptedPassword(request.getNewPassword()));
		userRepository.saveAndFlush(userObj);

		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("password.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create new Contact Us details.", response = Response.class)
	@PostMapping(value = "/contactus/create", produces = "application/json")
	public ResponseEntity<?> createContactUs(
			@ApiParam(value = "The Contact Us details request payload") @RequestBody ContactUs request,
			Principal principal, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());
		User user = userOptional.get();

		ValidationResult validationResult = contactUsValidator.validate(RequestType.POST, request, user);
		contactUsService.saveOrUpdate((ContactUs) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("contact.us.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create new Profile mobile number details.", response = Response.class)
	@PostMapping(value = "/profile/mobile/create", produces = "application/json")
	public ResponseEntity<?> createProfileMobileNo(
			@ApiParam(value = "The Profile mobile number details request payload") @RequestBody ProfileMobileDTO request,
			Principal principal, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());
		User user = userOptional.get();
		ValidationResult validationResult = profileMobileValidator.validate(RequestType.POST, request, user);
		profileMobileService.saveOrUpdate((ProfileMobile) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("user.profile.mobile.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create new Profile mobile number details.", response = Response.class)
	@PutMapping(value = "/profile/mobile/update", produces = "application/json")
	public ResponseEntity<?> updateProfileMobileNo(
			@ApiParam(value = "The Profile mobile number details request payload") @RequestBody ProfileMobileDTO request,
			Principal principal, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());
		User user = userOptional.get();
		ValidationResult validationResult = profileMobileValidator.validate(RequestType.PUT, request, user);

		try {
			Optional<ProfileMobile> profileMobileOptional = profileMobileService.findById(request.getId());
			ProfileMobile profileMobile = profileMobileOptional.get();

			if (profileMobile.getTitle().equals(MobileType.PRIMARY)
					&& request.getTitle().equals(MobileType.SECONDARY)) {
				return responseGenerator.successResponse(context,
						messageSource.getMessage("profile.mobile.invalid.update"), HttpStatus.OK);
			} else {
				profileMobileService.saveOrUpdate((ProfileMobile) validationResult.getObject());
				return responseGenerator.successResponse(context,
						messageSource.getMessage("user.profile.mobile.update"), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch user profile details by user.", response = Response.class)
	@GetMapping(value = "/profile/mobile/get", produces = "application/json")
	public ResponseEntity<?> getProfileMobile(@RequestHeader HttpHeaders httpHeader, Principal principal)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());
		User userObj = userOptional.get();
		try {
			List<ProfileMobile> profileMobileDtoList = profileMobileService
					.findByCustomerIdAndStatus(userObj.getReferenceId(),Status.ACTIVE);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("user.profile.mobile.get"),
					profileMobileDtoList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch user profile details by user.", response = Response.class)
	@GetMapping(value = "/profile/mobile/get/{mobileId}", produces = "application/json")
	public ResponseEntity<?> getMobileById(@PathVariable("mobileId") UUID mobileId,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());
		User userObj = userOptional.get();
		try {
			ProfileMobileDTO profileMobileDtoObject = profileMobileService.getById(mobileId);
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("user.profile.mobile.get.one"), profileMobileDtoObject, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete Profile Mobile Number by Profile Mobile Number id.", response = Response.class)
	@DeleteMapping(value = "/profile/mobile/delete/{mobileNoId}", produces = "application/json")
	public ResponseEntity<?> deleteProfileMobileNo(@PathVariable("mobileNoId") UUID mobileNoId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<ProfileMobile> profileMobileOptional = profileMobileService.findById(mobileNoId);
			ProfileMobile obj = profileMobileOptional.get();
			 //profileMobileDtoObject = profileMobileService.getById(mobileNoId);
			if (obj.getTitle().equals(MobileType.PRIMARY)) {
				return responseGenerator.errorResponse(context,
						messageSource.getMessage("profile.mobile.invalid.delete"), HttpStatus.BAD_REQUEST);
			} else {
				obj.setStatus(Status.INACTIVE);
				profileMobileService.saveOrUpdate(obj);
			}
			return responseGenerator.successResponse(context, messageSource.getMessage("profile.mobile.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("profile.mobile.invalid.delete"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create new rewards.", response = Response.class)
	@PostMapping(value = "/rewards/create", produces = "application/json")
	public ResponseEntity<?> createCustomerRewards(
			@ApiParam(value = "The customer reward create request payload") @RequestBody CustomerReward request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {

			Optional<User> userOptional = userRepository.findByUserName(principal.getName());
			if (!userOptional.isPresent()) {
				return responseGenerator.errorResponse(context, messageSource.getMessage("customer.not.found"),
						HttpStatus.BAD_REQUEST);
			}
			ValidationResult validationResult = customerRewardValidator.validate(RequestType.POST, request,
					userOptional.get());
			customerRewardService.saveOrUpdate((CustomerReward) validationResult.getObject());
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.rewards.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update rewards.", response = Response.class)
	@PutMapping(value = "/rewards/update", produces = "application/json")
	public ResponseEntity<?> updateCustomerRewards(
			@ApiParam(value = "The customer reward update request payload") @RequestBody CustomerReward request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<User> userOptional = userRepository.findByUserName(principal.getName());
			ValidationResult validationResult = customerRewardValidator.validate(RequestType.PUT, request,
					userOptional.get());
			List<CustomerRewardHistory> customerList = customerRewardValidator
					.validateCustomerRewardHistoryList(RequestType.PUT, request, validationResult.getErrors());
			customerRewardService.saveOrUpdate((CustomerReward) validationResult.getObject());
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.rewards.update"),

					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all customer rewards.", response = Response.class)
	@GetMapping(value = "/rewards/get", produces = "application/json")
	public ResponseEntity<?> getCustomerRewards(
			@ApiParam(value = "The customer reward getAll request payload") @RequestHeader HttpHeaders httpHeader,
			Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		List<CustomerReward> customerRewards = customerRewardService.findAll();
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.rewards.get"),
					customerRewards, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch customer rewards by customer reward id.", response = Response.class)
	@GetMapping(value = "/reward/get/{id}", produces = "application/json")
	public ResponseEntity<?> getCustomerRewardId(@PathVariable("id") UUID id,
			@ApiParam(value = "The customer reward getId request payload") @RequestHeader HttpHeaders httpHeader,
			Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<CustomerReward> customerRewards = customerRewardService.findById(id);
		try {

			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.rewards.get"),
					customerRewards, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch customer rewards by customer reward id.", response = Response.class)
	@GetMapping(value = "/rewards/get/{id}", produces = "application/json")
	public ResponseEntity<?> getById(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<CustomerReward> customerRewards = customerRewardService.findById(id);
			if (!customerRewards.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}

			CustomerReward obj = customerRewards.get();
			CustomerRewardDTO customerRewardObj = new CustomerRewardDTO();
			customerRewardObj.setId(obj.getId());
			customerRewardObj.setCustomerId(obj.getCustomerId());
			customerRewardObj.setActiveRewards(obj.getActiveRewards());
			customerRewardObj.setUsedRewards(obj.getUsedRewards());
			customerRewardObj.setLockedRewards(obj.getLockedRewards());

			List<CustomerRewardHistory> list = customerRewardHistoryService.findByRewardId(id);
			List<CustomerRewardHistoryDTO> customerRewardHistoryList = new ArrayList<CustomerRewardHistoryDTO>();
			for (CustomerRewardHistory customerRewardHistoryObj : list) {
				CustomerRewardHistoryDTO customerRewardHistoryDTO = new CustomerRewardHistoryDTO();
				customerRewardHistoryDTO.setId(customerRewardHistoryObj.getId());
				customerRewardHistoryDTO.setRewardId(customerRewardHistoryObj.getRewardId());
				customerRewardHistoryDTO.setRewards(customerRewardHistoryObj.getRewards());
				customerRewardHistoryDTO.setTransactionDate(customerRewardHistoryObj.getTransactionDate());
				customerRewardHistoryDTO.setTransactionType(customerRewardHistoryObj.getTransactionType());
				customerRewardHistoryDTO.setNotes(customerRewardHistoryObj.getNotes());
				customerRewardHistoryList.add(customerRewardHistoryDTO);
			}
			customerRewardObj.setCustomerRewardHistoryList(customerRewardHistoryList);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.rewards.get"),
					customerRewardObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
