package com.app.controller;


import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CustomerOrderDetailsDTO;
import com.app.dto.OrderCancelDTO;
import com.app.dto.VendorOrderMappingDTO;
import com.app.dto.VendorOrdersDTO;
import com.app.entity.User;
import com.app.entity.VendorOrderMapping;
import com.app.enumeration.RequestType;
import com.app.enumeration.VendorOrderStatus;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.CustomerOrderService;
import com.app.service.MessagePropertyService;
import com.app.service.UserService;
import com.app.service.VendorOrderMappingService;
import com.app.validator.ValidationResult;
import com.app.validator.VendorOrderMappingValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/vendor/mapping")
@Api(value = "Mapping Rest API", produces = "application/json", consumes = "application/json")
public class VendorMappingController {

	private static final Logger logger = Logger.getLogger(VendorMappingController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull VendorOrderMappingValidator validatorService;
	
	private @NonNull UserService userservice;

	private @NonNull CustomerOrderService customerOrderService;

	private @NonNull VendorOrderMappingService vendorOrderMappingService;
	
	private @NonNull UserRepository userRepository;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to fetch particular customer order.", response = Response.class)
	@GetMapping(value = "/information/fetch/{customerOrderId}", produces = "application/json")
	public ResponseEntity<?> getCustomerOrder(@PathVariable("customerOrderId") UUID customerOrderId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		CustomerOrderDetailsDTO responseObj = customerOrderService
				.findByCustomerOrderDetails(customerOrderId);

		try {
			return responseGenerator.successGetResponse(context, messageSource
					.getMessage("customer.order.get"),
					responseObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to new vendor order mapping.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(
			@ApiParam(value = "The vendor order request payload") @RequestBody VendorOrderMapping request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		vendorOrderMappingService.saveOrUpdate((VendorOrderMapping) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource
					.getMessage("vendor.order.mapping.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create ratings vendor order mapping.", response = Response.class)
	@PutMapping(value = "/update-ratings", produces = "application/json")
	public ResponseEntity<?> createRatings(
			@ApiParam(value = "The order ratings request payload") @RequestBody VendorOrderMapping request,Principal principal,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		Optional<User> userOptional = userservice.findByUserName(principal.getName());
		User user = userOptional.get();
		UUID referenceId= user.getReferenceId();		
		ValidationResult validationResult = validatorService.ratingValidate(RequestType.PUT,request,referenceId);
		vendorOrderMappingService.saveOrUpdates((VendorOrderMapping) validationResult.getObject());		
		try {
			return responseGenerator.successResponse(context, messageSource
					.getMessage("vendor.order.mapping.Ratings"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to update existing vendor order mapping.", response = Response.class)
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<?> update(
			@ApiParam(value = "The brand request payload") @RequestBody VendorOrderMapping request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		vendorOrderMappingService.saveOrUpdate((VendorOrderMapping) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource
					.getMessage("vendor.order.mapping.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch vendor order mapping.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
	//List<VendorOrderMapping> vendorMappingList = vendorOrderMappingService.findAll();
	List<VendorOrderMappingDTO> vendorOrderMappingList = vendorOrderMappingService.getAll();

			return responseGenerator.successGetResponse(context, messageSource
					.getMessage("vendor.order.mapping.get"), vendorOrderMappingList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch vendor order mapping by vendor order mapping id.",
			response = Response.class)
	@GetMapping(value = "/get/{orderMappingId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("orderMappingId") UUID orderMappingId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {		

			VendorOrderMappingDTO responceObj = vendorOrderMappingService.getById(orderMappingId);
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("vendor.order.mapping.get.one"), responceObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete vendor order mapping by vendor order mapping id.",
			response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			vendorOrderMappingService.deleteById(id);
			return responseGenerator.successResponse(context, messageSource
					.getMessage("vendor.order.mapping.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource
					.getMessage("invalid.delete"),HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@ApiOperation(value = "Allows to delete vendor order mapping by vendor order mapping id.",response = Response.class)
	@PutMapping(value = "/vendor/reject/orders", produces = "application/json")
	public ResponseEntity<?> vendorRejectOrders(@RequestBody OrderCancelDTO request, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		
			ValidationResult validationResult = validatorService.validateVendorReject(RequestType.PUT, request);
			VendorOrderMapping vendorOrderMapping = (VendorOrderMapping) validationResult.getObject();
			vendorOrderMappingService.updateOrderAsReject(vendorOrderMapping);
			try {
				return responseGenerator.successResponse(context, messageSource.getMessage("order.rejected"),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("invalid.cancel"),HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch orders status details.", response = Response.class)
	@GetMapping(value = "/orders/status/{orderStatus}", produces = "application/json")
	public ResponseEntity<?> getVendorByStatus(@PathVariable("orderStatus") VendorOrderStatus orderStatus,
			Principal principal, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			
			String username = principal.getName();
			Optional<User> userOptional = userRepository.findByUserName(username);
			List<VendorOrdersDTO> vendorOrderOrdersList=
			vendorOrderMappingService.findByVendorOrders(orderStatus);			
	
		 if(orderStatus.equals(VendorOrderStatus.ALLOCATED)) {	     
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("vendor.allocated.orders.get"), vendorOrderOrdersList, HttpStatus.OK);}
		 return responseGenerator.successGetResponse(context,
					messageSource.getMessage(orderStatus.equals(VendorOrderStatus.SHIPPED)?"vendor.shipped.orders.get":"vendor.delivered.orders.get"), vendorOrderOrdersList, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
//@RequestBody AllocatedOrderSearchDTO request,
