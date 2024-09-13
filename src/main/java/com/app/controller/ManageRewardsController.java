package com.app.controller;

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

import com.app.entity.ManageRewards;
import com.app.enumeration.RequestType;
import com.app.enumeration.Status;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.ManageRewardsService;
import com.app.service.MessagePropertyService;
import com.app.util.message.ResponseMessage;
import com.app.validator.ManageRewardsValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/manage/rewards")
@Api(value = "Manage Rewards Rest API", produces = "application/json", consumes = "application/json")
public class ManageRewardsController {

	private static final Logger logger = Logger.getLogger(ManageRewardsController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull ManageRewardsService manageRewardsService;

	private @NonNull ManageRewardsValidator validatorService;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new ManageRewards.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(
			@ApiParam(value = "The ManageRewards request payload") @RequestBody ManageRewards request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);

		manageRewardsService.saveOrUpdate((ManageRewards) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("manage.rewards.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing ManageRewards.", response = Response.class)
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<?> update(
			@ApiParam(value = "The ManageRewards request payload") @RequestBody ManageRewards request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		manageRewardsService.saveOrUpdate((ManageRewards) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("manage.rewards.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch ManageRewards by ManageRewards id.", response = Response.class)
	@GetMapping(value = "/get/{managerewardsId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("managerewardsId") UUID managerewardsId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<ManageRewards> manageRewards = manageRewardsService.findById(managerewardsId);
			if (!manageRewards.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			return responseGenerator.successGetResponse(context, messageSource.getMessage("manage.rewards.get"),
					manageRewards.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all ManageRewards.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("manage.rewards.get"),
					manageRewardsService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete use by id.", response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			ManageRewards rewards = manageRewardsService.findById(id).get();

			if (null == rewards) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_OBJECT_REFERENCE,
						HttpStatus.BAD_REQUEST);
			}
			rewards.setStatus(Status.INACTIVE);
			manageRewardsService.saveOrUpdate(rewards);

			return responseGenerator.successResponse(context, messageSource.getMessage("manage.rewards.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("manage.rewards.invalid.delete"),
					HttpStatus.BAD_REQUEST);
		}
	}
}