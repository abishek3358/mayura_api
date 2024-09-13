package com.app.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.OrderCancelDTO;
import com.app.dto.OrderItemRequestDTO;
import com.app.dto.OrderRequestDTO;
import com.app.entity.Customer;
import com.app.entity.CustomerDeliveryAddress;
import com.app.entity.CustomerOrder;
import com.app.entity.CustomerOrderItems;
import com.app.entity.ProductDtl;
import com.app.entity.UnitOfMeasurement;
import com.app.entity.User;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.RequestType;
import com.app.enumeration.Status;
import com.app.repository.CustomerOrderItemsRepository;
import com.app.repository.ProductDtlRepository;
import com.app.repository.UnitOfMeasurementRepository;
import com.app.repository.UserRepository;
import com.app.service.CustomerDeliveryAddressService;
import com.app.service.CustomerOrderService;
import com.app.service.CustomerService;
import com.app.service.MessagePropertyService;
import com.app.service.ProductCategoryService;
import com.app.service.ProductService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerOrderValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull ProductCategoryService productCategoryService;

	private @NonNull CustomerOrderService customerOrderService;

	private @NonNull CustomerOrderItemsRepository customerOrderItemsRepository;

	private @NonNull CustomerDeliveryAddressService customerDeliveryAddressService;

	private @NonNull CustomerService customerService;

	private @NonNull ProductService productService;

	private @NonNull ProductDtlRepository productDtlRepository;

	private @NonNull UserRepository userRepository;
	
	private @NonNull UnitOfMeasurementRepository unitOfMeasurementRepository;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for product type validation Added by Ulaganathan
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, OrderRequestDTO request, User user) {

		errors = new ArrayList<>();
		CustomerOrder customerOrderObj = null;

		Customer customer = customerService.findById(user.getReferenceId()).get();
		if (null == customer) {
			errors.add(messageSource.getMessage("customer.not.found"));
		}

		if (ValidationUtil.isNullObject(request.getPaymentMode())) {
			errors.add(messageSource.getMessage("payment.mode.required"));
		}

		if (ValidationUtil.isNull(request.getGrandTotal())) {
			errors.add(messageSource.getMessage("grand.total.required"));
		}

		if (ValidationUtil.isNull(request.getSubTotal())) {
			errors.add(messageSource.getMessage("subtotal.required"));
		}
		
		LocalDate localDate = LocalDate.now();
		
		//Date estimatedDeliveryDate = Date.from(localDate.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date actualDeliverydate = Date.from(localDate.plusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant());

		Optional<CustomerDeliveryAddress> deliverAddressOptional = customerDeliveryAddressService
				.findById(request.getDeliveryAddressId());
		if (!deliverAddressOptional.isPresent()) {
			throw new ObjectInvalidException(messageSource.getMessage("customer.delivery.address.not.found"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		List<CustomerOrderItems> customerOrderItemsList = validateCustomerOrderItemsList(request, errors);
		Double discAmnt = null;
		if (request.getDiscAmount() == null) {
			discAmnt = 0.0;
		} else {
			discAmnt = request.getDiscAmount();
		}
		customerOrderObj = CustomerOrder.builder()
				.location(deliverAddressOptional.get().getLocation())
				.latitude(deliverAddressOptional.get().getLatitude())
				.longitude(deliverAddressOptional.get().getLongitude())
				.paymentMode(request.getPaymentMode())
				.deliveryFee(request.getDeliveryFee())
				.actualDeliveryDate(actualDeliverydate)
				//.estimatedDeliveryDate(estimatedDeliveryDate)
				.subTotal(request.getSubTotal())
				.grandTotal(request.getGrandTotal())
				.discAmount(discAmnt)
				.deliveryAddressObj(deliverAddressOptional.get())
				.customerObj(customer).build();

		Integer noOfProducts = 0;
		for (CustomerOrderItems customerOrderItems : customerOrderItemsList) {
			customerOrderItems.setCustomerOrderObj(customerOrderObj);
			noOfProducts++;
		}
		customerOrderObj.setNoOfProducts(noOfProducts);
		customerOrderObj.setStatus(Status.ACTIVE);
		customerOrderObj.setOrderStatus(OrderStatus.CONFIRMED);
		customerOrderObj.setCustomerOrderItemsList(customerOrderItemsList);
		result.setObject(customerOrderObj);
		return result;
	}

	public List<CustomerOrderItems> validateCustomerOrderItemsList(OrderRequestDTO request, List<String> errors) {
		List<CustomerOrderItems> customerOrderItemsList = new ArrayList<CustomerOrderItems>();
		CustomerOrderItems customerOrderItemsObj = null;

		for (OrderItemRequestDTO orderItem : request.getCustomerOrderItemsList()) {
			Optional<ProductDtl> productDtlOptional = null;
			if (ValidationUtil.isNull(orderItem.getProductDtlId())) {
				errors.add(messageSource.getMessage("product.dtl.required"));
			} else {
				productDtlOptional = productDtlRepository.findById(orderItem.getProductDtlId());
				if (!productDtlOptional.isPresent()) {
					errors.add(messageSource.getMessage("product.dtl.not.found"));
				}
			}
			
			Optional<UnitOfMeasurement> productUomOptional = null;
			if (ValidationUtil.isNull(orderItem.getUomId())) {
				errors.add(messageSource.getMessage("product.uom.required"));
			} else {
				productUomOptional = unitOfMeasurementRepository.findById(orderItem.getUomId());
				if (!productUomOptional.isPresent()) {
					errors.add(messageSource.getMessage("product.uom.not.found"));
				}
			}

			if (ValidationUtil.isNull(orderItem.getOrderQty())) {
				errors.add(messageSource.getMessage("order.quantity.required"));
			}

			if (ValidationUtil.isNull(orderItem.getPrice())) {
				errors.add(messageSource.getMessage("product.amount.required"));
			}

			if (ValidationUtil.isNull(orderItem.getOrderQuantityAmount())) {
				errors.add(messageSource.getMessage("order.quantity.amount.required"));
			}

			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}
			Double discAmnt=null;
			if(orderItem.getDiscAmount() == null) {
				discAmnt=0.0;
			}else {
				discAmnt=orderItem.getDiscAmount();
			}
			customerOrderItemsObj = CustomerOrderItems.builder()
					.productObj(productDtlOptional.get().getProduct())
					.orderQty(orderItem.getOrderQty()).price(orderItem.getPrice())
					.orderQuantityAmount(orderItem.getOrderQuantityAmount())
					.productUom(productUomOptional.get())
					.productDtl(productDtlOptional.get())
					.discAmount(discAmnt).build();
			customerOrderItemsList.add(customerOrderItemsObj);
		}
		return customerOrderItemsList;
	}

	public ValidationResult validate(RequestType requestType,OrderCancelDTO request) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();

		
		Optional<CustomerOrder> customerOrderOptional = customerOrderService.findById(request.getCustomerOrderId());

		if (!customerOrderOptional.isPresent()) {
			throw new ObjectInvalidException(messageSource.getMessage("customer.order.not.found"));
		}
		CustomerOrder customerOrderObj = customerOrderOptional.get();
		 if (customerOrderObj.getOrderStatus().equals(OrderStatus.DELIVERED)){
				errors.add(messageSource.getMessage("invalid.cancel"));
			}
		if (customerOrderObj.getOrderStatus().equals(OrderStatus.CONFIRMED) || customerOrderObj.getOrderStatus().equals(OrderStatus.PROGRESS)
				|| customerOrderObj.getOrderStatus().equals(OrderStatus.SHIPPED)){


			customerOrderObj.setStatus(Status.INACTIVE);

			customerOrderObj.setReason(request.getReason());
			customerOrderObj.setOrderStatus(OrderStatus.CANCELLED);
			customerOrderObj.setOrderCancelledDate(new Date());
		}
//		else if (customerOrderObj.getOrderStatus().equals(OrderStatus.DELIVERED)){
//			errors.add(messageSource.getMessage("invalid.cancel"));
//		}
		result.setObject(customerOrderObj);
		return result;

	}

}
