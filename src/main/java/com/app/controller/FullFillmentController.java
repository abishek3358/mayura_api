package com.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.AllocatedOrderSearchDTO;
import com.app.dto.OrderCancelDTO;
import com.app.dto.OrderStatusUpdateDTO;
import com.app.entity.CustomerOrder;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.RequestType;
import com.app.enumeration.VendorOrderStatus;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.CustomerOrderService;
import com.app.service.MessagePropertyService;
import com.app.service.ProductService;
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
@RequestMapping("/api/full-fillment")
@Api(value = "Order Full fillment Rest API", produces = "application/json", consumes = "application/json")
public class FullFillmentController {

	private static final Logger logger = Logger.getLogger(FullFillmentController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull CustomerOrderService customerOrderService;


	private @NonNull ProductService productService;


	private @NonNull FullfillmentValidator validatorService;

	private @NonNull UserRepository userRepository;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to fetch orders status details.", response = Response.class)
	@PutMapping(value = "/orders/{orderStatus}", produces = "application/json")
	public ResponseEntity<?> getOrdersByStatus(@PathVariable("orderStatus") OrderStatus orderStatus,
			@RequestBody AllocatedOrderSearchDTO request, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<AdminViewOrdersDTO> ordersList = new ArrayList<AdminViewOrdersDTO>();

			if (orderStatus.equals(OrderStatus.CONFIRMED)) {
				ordersList = customerOrderService.findByConfirmedOrders(orderStatus);
			}
			if (orderStatus.equals(OrderStatus.ALLOCATED) || orderStatus.equals(OrderStatus.PROGRESS)
					|| orderStatus.equals(OrderStatus.SHIPPED)) {
				ordersList = customerOrderService.findByProgressingOrders(orderStatus, request.getVendorName(),request.getStartDate(),request.getEndDate());
			}

			if (orderStatus.equals(OrderStatus.DELIVERED)) {
				ordersList = customerOrderService.findByDeliveredOrders(orderStatus, request);
			}

			if (orderStatus.equals(OrderStatus.ALLORDERS)) {
				ordersList = customerOrderService.findByAllOrderStatus(orderStatus, request);
			}

//			List<CustomerOrder> customerOrderList = customerOrderService.findByOrderStatus(orderStatus);
//			AdminViewOrdersDTO OrdrsdtoObj = null;

//			List<AdminViewOrdersDTO> ordersOrdersList = new ArrayList<AdminViewOrdersDTO>();
//			if (orderStatus.equals(OrderStatus.CONFIRMED)) {
//				ordersOrdersList = customerOrderService.findByConfirmedOrders(orderStatus);
//			} else if (orderStatus.equals(OrderStatus.PROGRESS)) {
//				ordersOrdersList = customerOrderService.findByProgressingOrders(orderStatus, request);
//			}

			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("admin.view.orders.status.get"), ordersList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch confirm order status.", response = Response.class)
	@GetMapping(value = "/reject/orders/{vendorOrderStatus}", produces = "application/json")
	public ResponseEntity<?> getOrdersByRejected(@PathVariable("vendorOrderStatus") VendorOrderStatus vendorOrderStatus,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<AdminViewOrdersDTO> ordersList = new ArrayList<AdminViewOrdersDTO>();
			if (vendorOrderStatus.equals(VendorOrderStatus.REJECTED)) {
				ordersList = customerOrderService.findByVendorRejectedOrders(vendorOrderStatus);
			}
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("admin.view.orders.status.get"), ordersList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

		
	@ApiOperation(value = "Allows to fetch confirm order status.", response = Response.class)
	@PutMapping(value = "/confirm/orders/{orderStatus}", produces = "application/json")
	public ResponseEntity<?> getOrdersByConfirmed(@PathVariable("orderStatus") OrderStatus orderStatus,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<AdminViewOrdersDTO> ordersList = new ArrayList<AdminViewOrdersDTO>();
			if (orderStatus.equals(OrderStatus.CONFIRMED)) {
				ordersList = customerOrderService.findByCustomerConfirmedOrders(orderStatus);
			}
			return responseGenerator.successGetResponse(context,
					messageSource.getMessage("admin.view.orders.status.get"), ordersList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows admin to reject orders", response = Response.class)
	@PutMapping(value = "/admin/reject/orders", produces = "application/json")
	public ResponseEntity<?> adminRejectOrders(@RequestBody OrderCancelDTO request,@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		CustomerOrder customerOrder = (CustomerOrder) validationResult.getObject();
		customerOrderService.updateOrderAsCancelled(customerOrder);
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("order.rejected"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allow to  update confirmed order to progress status", response = Response.class)
	@PutMapping(value = "/order/update/progress/{customerOrderId}", produces = "application/json")
	public ResponseEntity<?> updateOrderStatusToProgress(
			@ApiParam(value = "The customer order's change order status request payload") @RequestBody OrderStatusUpdateDTO request,
			@PathVariable("customerOrderId") UUID customerOrderId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request, customerOrderId);
		CustomerOrder customerOrder = (CustomerOrder) validationResult.getObject();

		customerOrderService.updateOrderAsProgress(customerOrder);
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.order.status.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allow to  update shipped order to delivered status", response = Response.class)
	@PutMapping(value = "/order/update/delivered/{customerOrderId}", produces = "application/json")
	public ResponseEntity<?> updateOrderStatusToDelivered(
			@ApiParam(value = "The customer order's change order status request payload") @RequestBody OrderStatusUpdateDTO request,
			@PathVariable("customerOrderId") UUID customerOrderId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request, customerOrderId);
		CustomerOrder customerOrder = (CustomerOrder) validationResult.getObject();
		customerOrderService.updateOrderAsDelivered(customerOrder);
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.order.status.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allow to  update status progress to completed ", response = Response.class)
	@PutMapping(value = "/order/update/completed/{customerOrderId}", produces = "application/json")
	public ResponseEntity<?> updateOrderStatusToCompleted(
			@ApiParam(value = "The customer order's change order status request payload") @RequestBody OrderStatusUpdateDTO request,
			@PathVariable("customerOrderId") UUID customerOrderId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request, customerOrderId);

		CustomerOrder customerOrder = (CustomerOrder) validationResult.getObject();

		customerOrderService.updateOrderAsCompleted(customerOrder);

		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("customer.order.status.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}