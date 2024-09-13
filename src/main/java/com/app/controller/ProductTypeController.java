package com.app.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ProductTypeResponse;
import com.app.entity.ProductType;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.MessagePropertyService;
import com.app.service.ProductTypeService;
import com.app.util.message.ResponseMessage;
import com.app.validator.ProductTypeValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/product/type")
@Api(value = "Product Type Rest API", produces = "application/json", consumes = "application/json")
public class ProductTypeController {
	
	private static final Logger logger = Logger.getLogger(ProductTypeController.class);
	
	private @NonNull ResponseGenerator responseGenerator;
	
	private @NonNull ProductTypeService productTypeService;
	
	private @NonNull ProductTypeValidator validatorService;
	 
	@Autowired
	MessagePropertyService messageSource;
	
	@ApiOperation(value = "Allows to create new product type.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The product type request payload") @RequestBody ProductType request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		productTypeService.saveOrUpdate((ProductType) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("product.type.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@ApiOperation(value = "Allows to update existing product type.", response = Response.class)
	@PutMapping(value = "/update",  produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The product type request payload") @RequestBody ProductType request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		productTypeService.saveOrUpdate((ProductType) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("product.type.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@ApiOperation(value = "Allows to fetch all product types.", response = Response.class)
	@GetMapping(value = "/get/{productTypeId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("productTypeId") UUID productTypeId,@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<ProductType> productType = productTypeService.findById(productTypeId);
			if(!productType.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT, HttpStatus.BAD_REQUEST);
			}
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.type.get"),productType.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	 
	@ApiOperation(value = "Allows to fetch all product types.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			
//	List<ProductType> producttypeList = productTypeService.findByStatus(Status.ACTIVE);
//			
//			Comparator<ProductType> compareByName = (ProductType o1, ProductType o2) -> o1.getName().compareToIgnoreCase( o2.getName() );
//			 
//			Collections.sort(producttypeList, compareByName);
			List<ProductTypeResponse> producttypeList = productTypeService.findByGet();
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.type.get"), producttypeList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	 }
