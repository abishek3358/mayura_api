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

import com.app.dto.GstResponse;
import com.app.entity.GST;
import com.app.entity.ProductType;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.GSTService;
import com.app.service.MessagePropertyService;
import com.app.util.message.ResponseMessage;
import com.app.validator.GSTValidator;
import com.app.validator.ValidationResult;
import com.twilio.rest.api.v2010.Account.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/gst")
@Api(value = "GST Rest API", produces = "application/json", consumes = "application/json")
public class GSTController {
	
	private static final Logger logger = Logger.getLogger(GSTController.class);
	
	private @NonNull ResponseGenerator responseGenerator;
	
	private @NonNull GSTService gstService;
	
	private @NonNull GSTValidator validatorService;
	 
	@Autowired
	MessagePropertyService messageSource;

	
	
	@ApiOperation(value = "Allows to fetch all product types.", response = Response.class)
	@GetMapping(value = "/get/{gstId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("gstId") UUID gstId,@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<GST> gst = gstService.findById(gstId);
			if(!gst.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT, HttpStatus.BAD_REQUEST);
			}
			return responseGenerator.successGetResponse(context, messageSource.getMessage("gst.get"),gst.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch all GST.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<GstResponse> gstList = gstService.findByGet();
			return responseGenerator.successGetResponse(context, messageSource.getMessage("gst.get"),gstList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to create new gst.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The GST request payload") @RequestBody GST request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		gstService.saveOrUpdate((GST) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("gst.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing GST info.", response = Response.class)
	@PutMapping(value = "/update",  produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The GST request payload") @RequestBody GST request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		gstService.saveOrUpdate((GST) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("gst.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	 
}
