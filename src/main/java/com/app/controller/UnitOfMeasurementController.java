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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.UnitOfMeasurementResponse;
import com.app.entity.UnitOfMeasurement;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.MessagePropertyService;
import com.app.service.UnitOfMeasurementService;
import com.app.util.message.ResponseMessage;
import com.app.validator.UnitOfMeasurementValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/uom")
@Api(value = "Unit of Measurement Rest API", produces = "application/json", consumes = "application/json")
public class UnitOfMeasurementController {

	private static final Logger logger = Logger.getLogger(UnitOfMeasurementController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull UnitOfMeasurementService uomService;
	
	private @NonNull UnitOfMeasurementValidator validatorService;

	@Autowired
	MessagePropertyService messageSource;
	
	
	@ApiOperation(value = "Allows to create new uom.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The UnitOfMeasurement request payload") @RequestBody UnitOfMeasurement request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		 
		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		uomService.saveOrUpdate((UnitOfMeasurement) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("uom.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@ApiOperation(value = "Allows to update existing uom.", response = Response.class)
	@PutMapping(value = "/update",  produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The UnitOfMeasurement request payload") @RequestBody UnitOfMeasurement request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		uomService.saveOrUpdate((UnitOfMeasurement) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("uom.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	

	@ApiOperation(value = "Allows to fetch UOM by uom id.", response = Response.class)
	@GetMapping(value = "/get/{uomId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("uomId") UUID uomId,@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<UnitOfMeasurement> uom = uomService.findById(uomId);
			if(!uom.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT, HttpStatus.BAD_REQUEST);
			}
			return responseGenerator.successGetResponse(context, messageSource.getMessage("uom.get"),uom.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to delete UOM by UOM id.", response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			uomService.deleteById(id);

			return responseGenerator.successResponse(context, messageSource.getMessage("uom.delete"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("uom.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}
	
	@ApiOperation(value = "Allows to fetch all UOM's.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<UnitOfMeasurementResponse> uomList = uomService.findByGetAll();
			return responseGenerator.successGetResponse(context, messageSource.getMessage("uom.get"),uomList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
