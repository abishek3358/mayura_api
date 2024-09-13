package com.app.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

import com.app.dto.CustomerOffersOrderDTO;
import com.app.dto.OfferDetailsDTO;
import com.app.dto.OffersDTO;
import com.app.entity.Customer;
import com.app.entity.CustomerOrder;
import com.app.entity.OfferDetails;
import com.app.entity.User;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.RequestType;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.CustomerRepository;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.MessagePropertyService;
import com.app.service.OfferDetailsService;
import com.app.service.UserService;
import com.app.util.message.ResponseMessage;
import com.app.validator.OfferDetailsValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/offer/details")
@Api(value = "Offer Details Rest API", produces = "application/json", consumes = "application/json")
public class OfferDetailsController {
	private static final Logger logger = Logger.getLogger(OfferDetailsController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull OfferDetailsService offerDetailsService;

	private @NonNull OfferDetailsValidator validatorService;

	private @NonNull UserRepository userRepository;

	private @NonNull UserService userService;

	private @NonNull CustomerRepository customerRepository;

	private @NonNull CustomerOrderRepository customerOrderRepository;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new Offer details.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(
			@ApiParam(value = "The Offer details request payload") @RequestBody OfferDetailsDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		offerDetailsService.saveOrUpdate((OfferDetails) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("offer.details.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing Offer details.", response = Response.class)
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<?> update(
			@ApiParam(value = "The Offer Details request payload") @RequestBody OfferDetailsDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		offerDetailsService.saveOrUpdate((OfferDetails) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("offer.details.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all Active Offer details.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("offer.details.get"),
					offerDetailsService.getAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch all Offer details.", response = Response.class)
	@GetMapping(value = "/get/active", produces = "application/json")
	public ResponseEntity<?> getActiveAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("offer.details.active.get"),
					offerDetailsService.getActiveAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch all Expired Offer details.", response = Response.class)
	@GetMapping(value = "/get/expiry", produces = "application/json")
	public ResponseEntity<?> getExpiryAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("offer.details.expiry.get"),
					offerDetailsService.getExpiryAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}


	@ApiOperation(value = "Allows to fetch Offer details by Offer details id.", response = Response.class)
	@GetMapping(value = "/get/{offerDetailsId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("offerDetailsId") UUID offerDetailsId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<OfferDetails> offerDetailsOptional = offerDetailsService.findById(offerDetailsId);
			if (!offerDetailsOptional.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			OfferDetailsDTO offerDetailsObj = offerDetailsService.getById(offerDetailsId);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("offer.details.get.one"),
					offerDetailsObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete Offer details by Offer details id.", response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			offerDetailsService.deleteById(id);
			return responseGenerator.successResponse(context, messageSource.getMessage("offer.details.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("offer.details.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}

	@ApiOperation(value = "Allows to fetch customer offers by customer.", response = Response.class)
	@GetMapping(value = "/offer/get", produces = "application/json")
	public ResponseEntity<?> getOffers(@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());

		User userObj = userOptional.get();

		Customer customerObj = customerRepository.findById(userObj.getReferenceId()).get();

		List<OfferDetails> customerOfferList = offerDetailsService.findAll();

		List<OffersDTO> OffersDTOList = new ArrayList<OffersDTO>();

		for (OfferDetails OfferDetailsObj : customerOfferList) {

			List<CustomerOrder> customerOrderList = customerOrderRepository
					.findByOrder(OfferDetailsObj.getMinimumOrderAmount(), customerObj);
			if (!customerOrderList.isEmpty()) {
				OffersDTO offersDtoObj = new OffersDTO();
				offersDtoObj.setId(OfferDetailsObj.getId());
				offersDtoObj.setOfferName(OfferDetailsObj.getOfferName());
				offersDtoObj.setOfferCode(OfferDetailsObj.getOfferCode());
				offersDtoObj.setDetails(OfferDetailsObj.getTermsAndConditions());
				offersDtoObj.setExpiryDate(OfferDetailsObj.getExpiryDateAndTime());
				LocalDate localDate = LocalDate.now();
				Date currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				if (OfferDetailsObj.getExpiryDateAndTime().before(currentDate)) {
					offersDtoObj.setFlag(false);
				}

				OffersDTOList.add(offersDtoObj);
			}
		}

		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.offers"),
					OffersDTOList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch customer offers by customer.", response = Response.class)
	@GetMapping(value = "/offer/code/{offerCode}", produces = "application/json")
	public ResponseEntity<?> getOfferCode(@RequestHeader HttpHeaders httpHeader, Principal principal,
			@PathVariable("offerCode") String offerCode) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {

			Boolean offerStatus = false;

			List<CustomerOffersOrderDTO> customerOffersOrderDtoList = new ArrayList<CustomerOffersOrderDTO>();

			Optional<User> userOptional = userRepository.findByUserName(principal.getName());

			User userObj = userOptional.get();

			Customer customerObj = customerRepository.findById(userObj.getReferenceId()).get();

			LocalDate localDate = LocalDate.now();
			Date currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

			OfferDetails OfferCodeObj = offerDetailsService.findByOfferCode(offerCode).get();
			
			

			if (OfferCodeObj.getExpiryDateAndTime().before(currentDate)) {
				return responseGenerator.errorResponse(context, messageSource.getMessage("offer.date.expiry"),
						HttpStatus.BAD_REQUEST);

			} else {

				List<CustomerOrder> customerOrderList = customerOrderRepository.findByComfirmOrder(
						OfferCodeObj.getMinimumOrderAmount(), customerObj, OrderStatus.CONFIRMED, offerStatus);

				if (customerOrderList.isEmpty()) {

					return responseGenerator.errorResponse(context, messageSource.getMessage("no.orders.available"),
							HttpStatus.BAD_REQUEST);
				} else {
					for (CustomerOrder customerOrderObj : customerOrderList) {
						CustomerOffersOrderDTO customerOffersOrderDTO = new CustomerOffersOrderDTO();
						customerOffersOrderDTO.setCustomerOrderId(customerOrderObj.getId());
						customerOffersOrderDTO.setCustomerOrderNo(customerOrderObj.getOrderNo());
						customerOffersOrderDTO.setGrandTotal(customerOrderObj.getGrandTotal());
						customerOffersOrderDTO.setDiscountAmount(OfferCodeObj.getOfferValue());
						customerOffersOrderDTO.setOfferType(OfferCodeObj.getOfferType());
						customerOffersOrderDtoList.add(customerOffersOrderDTO);
					}
				}
			}

			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.offers"),
					customerOffersOrderDtoList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch customer offers by customer.", response = Response.class)
	@GetMapping(value = "/offer/update/{customerOrderId}/{grandTotal}", produces = "application/json")
	public ResponseEntity<?> getOfferconfirmed(@RequestHeader HttpHeaders httpHeader,@RequestBody 
			CustomerOffersOrderDTO request,@PathVariable("customerOrderId") UUID customerOrderId,@PathVariable("grandTotal") Double grandTotal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {
			
			CustomerOrder customerOrderObj= customerOrderRepository.findById(customerOrderId).get();
			
			
//			Double discountAmount=0.0;
//			Double grandTotal=0.0;
//			if(request.getOfferType().equals(OfferType.PERCENTAGE)) {
//				discountAmount=customerOrderObj.getGrandTotal()-
//						((customerOrderObj.getGrandTotal()*request.getDiscountAmount())/100);
//				
//			}else if(request.getOfferType().equals(OfferType.FLAT)) {
//				
//				discountAmount=request.getDiscountAmount();
//			}
//			grandTotal=customerOrderObj.getGrandTotal()-discountAmount;
			customerOrderObj.setGrandTotal(grandTotal);
			customerOrderObj.setOfferStatus(true);
			
			
			customerOrderRepository.saveAndFlush(customerOrderObj);
			
				return responseGenerator.successResponse(context, messageSource.getMessage("offer.details.update"),
						HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}

}
