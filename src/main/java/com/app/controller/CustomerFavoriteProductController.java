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

import com.app.dto.CustomerFavoriteProductDTO;
import com.app.entity.Customer;
import com.app.entity.CustomerFavoriteProduct;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.CustomerFavoriteProductService;
import com.app.service.CustomerService;
import com.app.service.MessagePropertyService;
import com.app.service.UserService;
import com.app.validator.CustomerFavoriteProductValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/customer/favorite-product")
@Api(value = " Customer Favorite Product Rest API", produces = "application/json", consumes = "application/json")
public class CustomerFavoriteProductController {
	private static final Logger logger = Logger.getLogger(CustomerFavoriteProductController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull CustomerFavoriteProductService customerFavoriteProductService;

	private @NonNull CustomerFavoriteProductValidator validatorService;

	private @NonNull CustomerService customerService;

	private @NonNull UserService userService;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new customer favorite product.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> createCustomerFavoriteProduct(
			@ApiParam(value = "The customer favorite product request payload") @RequestBody CustomerFavoriteProduct request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userService.findByUserName(principal.getName());
		UUID customerId = userOptional.get().getReferenceId();
		ValidationResult validationResult = validatorService.validate(RequestType.POST, request, customerId);
		customerFavoriteProductService.saveOrUpdate((CustomerFavoriteProduct) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context,
					messageSource.getMessage("customer.favorite.product.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete customer favorite product by customer id and product id.", response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID productId, @RequestHeader HttpHeaders httpHeader, Principal principal)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<User> userOptional = userService.findByUserName(principal.getName());
			Optional<Customer> customerOptional = customerService.findById(userOptional.get().getReferenceId());
			Customer customer = customerOptional.get();
			customerFavoriteProductService.deleteByFavorite(customer.getId().toString(),productId.toString());
			return responseGenerator.successResponse(context,
					messageSource.getMessage("customer.favorite.product.delete"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context,
					messageSource.getMessage("customer.favorite.product.invalid.delete"), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch customer favorite product", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<User> userOptional = userService.findByUserName(principal.getName());
			Optional<Customer> customerOptional = customerService.findById(userOptional.get().getReferenceId());
			Customer customer = customerOptional.get();
			List<CustomerFavoriteProductDTO> customerDeliveryAddressList = customerFavoriteProductService

					.getAll(customer.getId().toString());
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("customer.favorite.product.get"), customerDeliveryAddressList,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch customer favorite product  by customer favorite product  id. ", response = Response.class)
	@GetMapping(value = "/get/{id}", produces = "application/json")
	public ResponseEntity<?> getById(@PathVariable("id") UUID productId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<CustomerFavoriteProduct> customerDeliveryAddressList = customerFavoriteProductService
					.findById(productId);
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("customer.favorite.product.get.one"), customerDeliveryAddressList,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
