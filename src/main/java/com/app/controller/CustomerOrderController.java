package com.app.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.CustomerOrderSummaryDTO;
import com.app.dto.OrderCancelDTO;
import com.app.dto.OrderRequestDTO;
import com.app.dto.OrderStatusUpdateDTO;
import com.app.email.helper.FlyingSaucerPDFUtil;
import com.app.entity.CustomerOrder;
import com.app.entity.User;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.RequestType;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.ProductImageRepository;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.CustomerOrderService;
import com.app.service.MessagePropertyService;
import com.app.util.message.ResponseMessage;
import com.app.validator.CustomerOrderValidator;
import com.app.validator.FullfillmentValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/customer/order")
@Api(value = "Customer order Rest API", produces = "application/json", consumes = "application/json")
public class CustomerOrderController {

	private static final Logger logger = Logger.getLogger(CustomerOrderController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull CustomerOrderService customerOrderService;

	private @NonNull CustomerOrderRepository customerOrderRepository;

	private @NonNull CustomerOrderValidator validatorService;

	private @NonNull FullfillmentValidator customerValidatorService;

	private @NonNull UserRepository userRepository;
	
	private @NonNull	FlyingSaucerPDFUtil  flyingSaucerPDFUtil;

	private @NonNull ProductImageRepository imageRepository;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new customer order.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(
			@ApiParam(value = "The customer order request payload") @RequestBody OrderRequestDTO request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<User> userOptional = userRepository.findByUserName(principal.getName());

			ValidationResult validationResult = validatorService.validate(RequestType.POST, request,
					userOptional.get());

			CustomerOrder customerOrder = (CustomerOrder) validationResult.getObject();

			customerOrder = customerOrderService.saveOrUpdate(customerOrder);

			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.order.create"),
					customerOrder.getId(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch particular customer order.", response = Response.class)
	@GetMapping(value = "/get/{customerOrderId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("customerOrderId") UUID customerOrderId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		CustomerOrderSummaryDTO responseObj = customerOrderService.findByCustomerOrderId(customerOrderId);

		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.order.get"),
					responseObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch my orders status details.", response = Response.class)
	@GetMapping(value = "/my/orders/{orderStatus}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("orderStatus") OrderStatus orderStatus,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<User> userOptional = userRepository.findByUserName(principal.getName());

		List<AdminViewOrdersDTO> customerOrderList = customerOrderService.findByMyOrderStatus(orderStatus,
				userOptional.get().getReferenceId());
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("my.orders.status.get"),
					customerOrderList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allow to Confirmed order to cancelled Status", response = Response.class)
	@PutMapping(value = "/order/update/cancelled", produces = "application/json")
	public ResponseEntity<?> confirmeOrderStatusToCancel(@RequestBody OrderCancelDTO request,@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		CustomerOrder customerOrder = (CustomerOrder) validationResult.getObject();
		customerOrderService.updateOrderAsCancelled(customerOrder);
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.order.cancelled"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update customer rating and reviwes after order completed", response = Response.class)
	@PutMapping(value = "/review/{customerOrderId}", produces = "application/json")
	public ResponseEntity<?> updateReviews(
			@ApiParam(value = "The customer Rating and Reviwes request payload") @RequestBody OrderStatusUpdateDTO request,
			@PathVariable("customerOrderId") UUID customerOrderId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = customerValidatorService.validate(RequestType.PUT, request,
				customerOrderId);
		customerOrderRepository.saveAndFlush((CustomerOrder) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.order.status.update"),
					HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch customer order reviews.", response = Response.class)
	@GetMapping(value = "/get/review/{customerOrderId}", produces = "application/json")
	public ResponseEntity<?> get1(@PathVariable("customerOrderId") UUID customerOrderId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<CustomerOrder> customerOrderOptional = customerOrderService.findById(customerOrderId);
		if (!customerOrderOptional.isPresent()) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}

		CustomerOrder customerOrderObj = customerOrderOptional.get();

		OrderStatusUpdateDTO responseObj = new OrderStatusUpdateDTO();

		responseObj.setNoOfStars(customerOrderObj.getNoOfStars());
		responseObj.setReviews(customerOrderObj.getReviews());
		// customerOrderObj.add(responseObj);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("customer.orders.reviews"),
					responseObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch customer order invoice", response = Response.class)
	@RequestMapping(value = "/print/{customerOrderId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> generateInvoicePdf(@PathVariable UUID customerOrderId,
			@RequestHeader HttpHeaders httpHeader) {

		logger.info("Resolved template path: {}");
		
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		CustomerOrderSummaryDTO customerObj = customerOrderService.findByCustomerOrderId(customerOrderId);

		String invoiceFileName = "Invoice-" + customerObj.getOrderNo() + ".pdf";
		Map<String, Object> response = new HashMap<>();
		response.put("customerObj", customerObj);
		try {
			byte[] report = flyingSaucerPDFUtil.generatePdf("invoice/pdf-Customer", response);

			InputStream pdfInputStream = new ByteArrayInputStream(report);
			return ResponseEntity.ok().header("Access-Control-Expose-Headers", "Content-Disposition")
					.header("Content-Disposition", "attachment; filename=" + invoiceFileName)
					.body(new InputStreamResource(pdfInputStream));

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}

