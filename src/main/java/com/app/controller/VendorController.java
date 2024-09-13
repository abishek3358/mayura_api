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

import com.app.dto.VendorAccountDTO;
import com.app.dto.VendorBranchDTO;
import com.app.dto.VendorContactPersonDTO;
import com.app.dto.VendorDTO;
import com.app.dto.VendorUpdateDTO;
import com.app.dto.VendorViewDTO;
import com.app.entity.User;
import com.app.entity.Vendor;
import com.app.entity.VendorAccount;
import com.app.entity.VendorBranch;
import com.app.entity.VendorContactPerson;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.MessagePropertyService;
import com.app.service.VendorAccountService;
import com.app.service.VendorBranchService;
import com.app.service.VendorContactPersonService;
import com.app.service.VendorService;
import com.app.util.message.ResponseMessage;
import com.app.validator.ValidationResult;
import com.app.validator.VendorAccountValidator;
import com.app.validator.VendorBranchValidator;
import com.app.validator.VendorContactPersonValidator;
import com.app.validator.VendorValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/vendor")
@Api(value = "Vendor Rest API", produces = "application/json", consumes = "application/json")
public class VendorController {

	private static final Logger logger = Logger.getLogger(VendorController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull VendorService vendorService;

	private @NonNull VendorContactPersonService contactPersonService;

	private @NonNull VendorContactPersonValidator vendorContactPersonValidator;

	private @NonNull VendorValidator validatorService;

	private @NonNull VendorBranchValidator vendorBranchValidator;

	private @NonNull VendorBranchService vendorBranchService;

	private @NonNull VendorAccountService vendorAccountService;

	private @NonNull VendorAccountValidator vendorAccountValidator;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new Vendor.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The Vendor request payload") @RequestBody VendorDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		vendorService.saveOrUpdate((Vendor) validationResult.getObject());

		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all Vendor Info.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<VendorViewDTO> vendorObj = vendorService.getAll();
			return responseGenerator.successGetResponse(context, messageSource.getMessage("vendor.get"), vendorObj,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to get Vendor by Vendor Id.", response = Response.class)
	@GetMapping(value = "/get/{id}", produces = "application/json")
	public ResponseEntity<?> getById(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<Vendor> vendorOptional = vendorService.findById(id);
			if (!vendorOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			VendorViewDTO vendorObj = vendorService.getById(id);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("vendor.get.one"), vendorObj,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing Vendor Info.", response = Response.class)
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<?> update(
	    @ApiParam(value = "The Vendor request payload") @RequestBody VendorUpdateDTO request,
	    @RequestHeader HttpHeaders httpHeader) throws Exception {
	    TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
	    ValidationResult validationResult = validatorService.validateVendor(RequestType.PUT, request);
	    
	    Vendor studentDuplicate = vendorService.findByphone(request.getPhoneNumber(), request.getId());
		if (null != studentDuplicate) {
			String[] params = new String[] { request.getPhoneNumber() };
			return responseGenerator.errorResponse(context, messageSource.getMessage("MobileNo.duplicate", params),
					HttpStatus.BAD_REQUEST);

		}
		Vendor studentDuplicate1 = vendorService.findByemails(request.getEmailId(), request.getId());
		if (null != studentDuplicate1) {
			String[] params = new String[] { request.getEmailId() };
			return responseGenerator.errorResponse(context, messageSource.getMessage("email.duplicate", params),
					HttpStatus.BAD_REQUEST);

		}

	   		
	    if (validationResult.hasErrors()) {
	        // Handle validation errors and return a response as needed.
	    }

	    Vendor vendor = new Vendor();
	    vendor.setId(request.getId());
	    vendor.setName(request.getName());
	    vendor.setVendorCode(request.getVendorCode());
	    vendor.setPhoneNumber(request.getPhoneNumber());
	    vendor.setEmailId(request.getEmailId());
	    vendor.setGstTaxNumber(request.getGstTaxNumber());
	    vendor.setStatus(request.getStatus());  // Set the status

	    vendorService.saveOrUpdates(vendor);

	    try {
	        return responseGenerator.successResponse(context, messageSource.getMessage("vendor.update"), HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error(e.getMessage(), e);
	        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	@ApiOperation(value = "Allows to delete Vendor by Vendor id.", response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			vendorService.deleteById(id);

			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.delete"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("vendor.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}

	@ApiOperation(value = "Allows to create new vendor contact person.", response = Response.class)
	@PostMapping(value = "/{vendorId}/contact_person/create", produces = "application/json")
	public ResponseEntity<?> create(
			@ApiParam(value = "The VendorContact Person request payload") @RequestBody VendorContactPerson request,
			@PathVariable UUID vendorId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = vendorContactPersonValidator.validate(RequestType.POST, request, vendorId);

		contactPersonService.saveOrUpdate((VendorContactPerson) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.contact.person.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing uom.", response = Response.class)
	@PutMapping(value = "/{vendorId}/contact_person/update", produces = "application/json")
	public ResponseEntity<?> update(
			@ApiParam(value = "The UnitOfMeasurement request payload") @RequestBody VendorContactPerson request,
			@PathVariable UUID vendorId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = vendorContactPersonValidator.validate(RequestType.PUT, request, vendorId);
		contactPersonService.saveOrUpdate((VendorContactPerson) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.contact.person.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete Mentor by Mentor id.", response = Response.class)
	@DeleteMapping(value = "/{vendorId}/contact_person/delete/{id}", produces = "application/json")
	public ResponseEntity<?> deleteContactPerson(@PathVariable("id") UUID id, @PathVariable("vendorId") UUID vendorId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<VendorContactPerson> contactPersonOptional = contactPersonService.findById(id);
			if (!contactPersonOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			contactPersonService.deleteById(id);

			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.contact.person.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context,
					messageSource.getMessage("vendor.contact.person.invalid.delete"), HttpStatus.BAD_REQUEST);

		}
	}

	@ApiOperation(value = "Allows to get Mentor by Mentor Id.", response = Response.class)
	@GetMapping(value = "/get/{vendorId}/contact_person/get/{id}", produces = "application/json")
	public ResponseEntity<?> getByVendorIdByContactPersonId(@PathVariable("vendorId") UUID vendorId,
			@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<VendorContactPerson> contactPersonOptional = contactPersonService.findById(id);
			if (!contactPersonOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			VendorContactPersonDTO mentorObj = contactPersonService.getById(id);
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("vendor.contact.person.get.one"), mentorObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create new Vendor Branch.", response = Response.class)
	@PostMapping(value = "/{vendorId}/branch/create", produces = "application/json")
	public ResponseEntity<?> createVendorBranch(
			@ApiParam(value = "The Vendor Branch request payload") @RequestBody VendorBranchDTO request,
			@PathVariable("vendorId") UUID vendorId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = vendorBranchValidator.validate(RequestType.POST, request, vendorId);
		vendorBranchService.saveOrUpdate((VendorBranch) validationResult.getObject());

		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.branch.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing Vendor Branch Info.", response = Response.class)
	@PutMapping(value = "/{vendorId}/branch/update", produces = "application/json")
	public ResponseEntity<?> updateVendorBranch(
			@ApiParam(value = "The Vendor Branch request payload") @RequestBody VendorBranchDTO request,
			@RequestHeader HttpHeaders httpHeader, @PathVariable("vendorId") UUID vendorId) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = vendorBranchValidator.validate(RequestType.PUT, request, vendorId);
		vendorBranchService.saveOrUpdate((VendorBranch) validationResult.getObject());

		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.branch.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to get Vendor Branch by Vendor Branch Id.", response = Response.class)
	@GetMapping(value = "/{vendorId}/branch/get/{id}", produces = "application/json")
	public ResponseEntity<?> getVendorToBranchId(@PathVariable("id") UUID id, @PathVariable("vendorId") UUID vendorId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<VendorBranch> ventorOptional = vendorBranchService.findById(id);
			if (!ventorOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			VendorBranchDTO vendorBranchObj = vendorBranchService.getById(id);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("vendor.branch.get.one"),
					vendorBranchObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete Vendor Branch by Vendor Branch id.", response = Response.class)
	@DeleteMapping(value = "/{vendorId}/branch/delete/{id}", produces = "application/json")
	public ResponseEntity<?> deleteVendorBranch(@PathVariable("id") UUID id, @PathVariable("vendorId") UUID vendorId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {

			Optional<VendorBranch> ventorOptional = vendorBranchService.findById(id);
			if (!ventorOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			vendorBranchService.deleteById(id);

			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.branch.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("vendor.branch.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}

	@ApiOperation(value = "Allows to create new vendor account.", response = Response.class)
	@PostMapping(value = "/{vendorId}/account/create", produces = "application/json")
	public ResponseEntity<?> vendorAccountCreate(
			@ApiParam(value = "The VendorContact Person request payload") @RequestBody VendorAccountDTO request,
			@PathVariable("vendorId") UUID vendorId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = vendorAccountValidator.validate(RequestType.POST, request, vendorId);
		vendorAccountService.saveOrUpdate((VendorAccount) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.account.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update vendor accoumt.", response = Response.class)
	@PutMapping(value = "/{vendorId}/account/update", produces = "application/json")
	public ResponseEntity<?> vendorAccountUpdate(
			@ApiParam(value = "The Vendor account request payload") @RequestBody VendorAccountDTO request,
			@RequestHeader HttpHeaders httpHeader, @PathVariable("vendorId") UUID vendorId) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = vendorAccountValidator.validate(RequestType.PUT, request, vendorId);
		vendorAccountService.saveOrUpdate((VendorAccount) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.account.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to get Vendor Account by Vendor Account Id.", response = Response.class)
	@GetMapping(value = "/{vendorId}/account/get/{id}", produces = "application/json")
	public ResponseEntity<?> getByVendorByAccountId(@PathVariable("id") UUID id,
			@PathVariable("vendorId") UUID vendorId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<VendorAccount> vendorAccountOptional = vendorAccountService.findById(id);
			if (!vendorAccountOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			VendorAccountDTO vendorAccountObj = vendorAccountService.getById(id);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("vendor.account.get.one"),
					vendorAccountObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete Vendor Account by Vendor Account id.", response = Response.class)
	@DeleteMapping(value = "/{vendorId}/account/delete/{id}", produces = "application/json")
	public ResponseEntity<?> deleteVendorByAccountId(@PathVariable("id") UUID id,
			@PathVariable("vendorId") UUID vendorId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<VendorAccount> vendorAccountOptional = vendorAccountService.findById(id);
			if (!vendorAccountOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}

			vendorAccountService.deleteById(id);

			return responseGenerator.successResponse(context, messageSource.getMessage("vendor.account.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("vendor.account.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}

}
