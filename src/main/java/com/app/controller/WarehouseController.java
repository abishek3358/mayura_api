package com.app.controller;

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

import com.app.entity.Warehouse;
import com.app.enumeration.RequestType;
import com.app.repository.CityRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.MessagePropertyService;
import com.app.service.WarehouseService;
import com.app.util.message.ResponseMessage;
import com.app.validator.ValidationResult;
import com.app.validator.WarehouseValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/warehouse")
@Api(value = "Warehouse Rest API's", produces = "application/json", consumes = "application/json")
public class WarehouseController {
	
	private static final Logger logger = Logger.getLogger(WarehouseController.class);
	
	private @NonNull ResponseGenerator responseGenerator;
	
	private @NonNull WarehouseService warehouseService;
	
	private @NonNull CityRepository cityRepository;
	
	private @NonNull WarehouseValidator validatorService;
	 
	@Autowired
	MessagePropertyService messageSource;
	
	@ApiOperation(value = "Allows to create new warehouse.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The warehouse request payload") @RequestBody Warehouse request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		 
		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		warehouseService.saveOrUpdate((Warehouse) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("warehouse.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@ApiOperation(value = "Allows to update existing warehouse.", response = Response.class)
	@PutMapping(value = "/update",  produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The warehouse request payload") @RequestBody Warehouse request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		warehouseService.saveOrUpdate((Warehouse) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("warehouse.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@ApiOperation(value = "Allows to fetch all warehouse informations.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("warehouse.get"),warehouseService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch warehouse by warehouse id.", response = Response.class)
	@GetMapping(value = "/get/{warehouseId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("warehouseId") UUID warehouseId,@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<Warehouse> tradingWarehouse = warehouseService.findById(warehouseId);
			if(!tradingWarehouse.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT, HttpStatus.BAD_REQUEST);
			}
			return responseGenerator.successGetResponse(context, messageSource.getMessage("warehouse.get"),tradingWarehouse.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	 
	 
}
