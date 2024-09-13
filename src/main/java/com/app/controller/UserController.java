package com.app.controller;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AdminDTO;
import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.SignUpDTO;
import com.app.dto.UserView;
import com.app.dto.UserViewDTO;
import com.app.entity.User;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.RequestType;
import com.app.enumeration.Status;
import com.app.enumeration.UserRoleType;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.security.JwtTokenUtil;
import com.app.service.CustomerOrderService;
import com.app.service.MessagePropertyService;
import com.app.service.UserService;
import com.app.util.message.ResponseMessage;
import com.app.validator.UserValidator;
import com.app.validator.ValidationResult;
import com.app.validator.VendorAdminLoginValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/user")
@Api(value = "User Rest API's", produces = "application/json", consumes = "application/json")

public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);
	
	private @NonNull ResponseGenerator responseGenerator; 
	
	private @NonNull UserValidator userValidator;
	
	private @NonNull VendorAdminLoginValidator vendorAdminLoginValidator;
	
	private @NonNull CustomerOrderService customerOrderService;
	
	private @NonNull UserService userService;
	private @NonNull UserRepository userRepository;
	
	private @NonNull  JwtTokenUtil jwtTokenUtil;

	private @NonNull  MessageSource messageSource;
	
	private @NonNull MessagePropertyService messagePropertySource;
	
	@ApiOperation(value = "Allows to create new user.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The user request payload") @RequestBody SignUpDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {			
			ValidationResult validationResult = userValidator.validate(RequestType.POST, request);
			userService.saveOrUpdate((User) validationResult.getObject());

			return responseGenerator.successResponse(context, messagePropertySource.getMessage("user.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	@ApiOperation(value = "Allows to create vendor user.", response = Response.class)
	@PostMapping(value = "/create/vendor-user", produces = "application/json")
	public ResponseEntity<?> createVendorAdmin(@ApiParam(value = "The Adminvendor login request payload") @RequestBody SignUpDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			if (request.getUserRole().equals(UserRoleType.VENDOR_ADMIN)) {
				ValidationResult validationResult = vendorAdminLoginValidator.validatevendoradmin(RequestType.POST,
						request);
				userService.saveOrUpdate((User) validationResult.getObject());
			} else {
				ValidationResult validationResult = vendorAdminLoginValidator.validatevendorstaff(RequestType.POST,
						request);
				userService.saveOrUpdate((User) validationResult.getObject());
			}

			return responseGenerator.successResponse(context, messagePropertySource.getMessage("vendor.login.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
		
	@ApiOperation(value = "Allows to update existing user.", response = Response.class)
	@PutMapping(value = "/update",  produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The User request payload") @RequestBody SignUpDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = userValidator.validate(RequestType.PUT, request);
		userService.saveOrUpdate((User) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messagePropertySource.getMessage("user.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch vendor details.", response = Response.class)
	@GetMapping(value = "get/vendor_user", produces = "application/json")
	public ResponseEntity<?> getVendor_user(@RequestHeader HttpHeaders httpHeader,Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());
		List<UserView> userList=userService.findByVendorStaffs(userOptional.get().getReferenceId());		
		try {
			return responseGenerator.successGetResponse(context, messagePropertySource.getMessage("vendor.user.list"),
					userList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}	
	
	
	@ApiOperation(value = "Allows to fetch all attendance list.", response = Response.class)
	@RequestMapping(value = "/getVendorStaff", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<?> getVendorStaff(@RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {

			List<User> getRole = userService.getVendorStaff();
			return responseGenerator.successGetResponse(context, messagePropertySource.getMessage("vendor.user.list"),
					getRole, HttpStatus.OK);
		} catch (Exception e) {

			logger.error("error occured while get attendance {}", e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}
	
	
	
	
	@ApiOperation(value = "Allows to fetch all user  list.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			
			List<UserViewDTO> userList=userService.getAll();
			userList.sort((o1,o2)->o1.getFullName().compareToIgnoreCase(o2.getFullName()));
			
			return responseGenerator.successGetResponse(context, messagePropertySource.getMessage("user.get.list"),
					
					userList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch user by user id.", response = Response.class)
	@GetMapping(value = "/get/{userId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("userId") UUID userId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		Optional<User> userOptional = userService.findById(userId);
		if (!userOptional.isPresent()) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}
		try {
			UserViewDTO userViewOptional = userService.getById(userId);	
	
			return responseGenerator.successGetResponse(context, messagePropertySource.getMessage("user.get.one"),userViewOptional,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to delete use by user id.", response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			User userOject = userService.findById(id).get();

			if (null == userOject) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_OBJECT_REFERENCE,
						HttpStatus.BAD_REQUEST);
			}
			userOject.setStatus(Status.INACTIVE);
			userService.saveOrUpdate(userOject);

			return responseGenerator.successResponse(context, messagePropertySource.getMessage("user.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messagePropertySource.getMessage("user.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}

	@ApiOperation(value = "Allows to update existing admin.", response = Response.class)
	@PutMapping(value = "/admin/update", produces = "application/json")
	public ResponseEntity<?> AdminUpdate(@ApiParam(value = "The Admin request payload") @RequestBody AdminDTO request,
	        @RequestHeader HttpHeaders httpHeader) {
	    TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

	    try {
	        userService.UpdateAdmin(request);
	        return responseGenerator.successResponse(context, messagePropertySource.getMessage("admin.update"), HttpStatus.OK);
	    } catch (IllegalArgumentException e) {
	        e.printStackTrace();
	        logger.error(e.getMessage(), e);
	        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error(e.getMessage(), e);
	        return responseGenerator.errorResponse(context, "An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}





