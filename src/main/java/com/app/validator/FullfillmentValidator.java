package com.app.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.OrderCancelDTO;
import com.app.dto.OrderStatusUpdateDTO;
import com.app.entity.CustomerOrder;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.RequestType;
import com.app.enumeration.Status;
import com.app.repository.UserRepository;
import com.app.response.ResponseGenerator;
import com.app.service.CustomerOrderService;
import com.app.service.MessagePropertyService;

import lombok.AllArgsConstructor;
import lombok.NonNull;


@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class FullfillmentValidator {
	
    @Autowired
    MessagePropertyService messageSource;
    
    private @NonNull ResponseGenerator responseGenerator;
    
	private @NonNull CustomerOrderService customerOrderService;
	
	private @NonNull UserRepository userRepository;

	List<String> errors = null;
  
	 public ValidationResult validate(RequestType requestType,OrderStatusUpdateDTO request,UUID customerOrderId) {
		 
			errors = new ArrayList<>();

			Optional<CustomerOrder> customerOrderOptional = customerOrderService.findById(customerOrderId);

			if (!customerOrderOptional.isPresent()) {   
				throw new ObjectInvalidException(messageSource.getMessage("customer.order.not.found"));
			}

			CustomerOrder customerOrderObj = customerOrderOptional.get();
			if (customerOrderObj.getOrderStatus().equals(OrderStatus.PROGRESS)) {

				/*
				 * if (request.getEstimatedDeliveryDate() == null) {
				 * errors.add(messageSource.getMessage(
				 * "customer.order.enter.estimated.delivery.date")); }
				 * 
				 * if(request.getEstimatedDeliveryDate().after(customerOrderObj.
				 * getActualDeliveryDate())&&
				 * request.getEstimatedDeliveryDate().before(customerOrderObj.getCreatedOn())) {
				 * errors.add(messageSource.getMessage(
				 * "customer.order.valid.estimated.delivery.date")); }
				 */
				
			}
			
			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}

			ValidationResult result = new ValidationResult();

			if (customerOrderObj.getOrderStatus().equals(OrderStatus.PROGRESS)) {
				customerOrderObj.setOrderStatus(OrderStatus.SHIPPED);
				customerOrderObj.setEstimatedDeliveryDate(request.getEstimatedDeliveryDate());
				customerOrderObj.setNotes(request.getNotes());
				customerOrderObj.setOrderShippedDate(new Date());  

			} else if (customerOrderObj.getOrderStatus().equals(OrderStatus.SHIPPED)) {

				customerOrderObj.setOrderStatus(OrderStatus.DELIVERED);
				customerOrderObj.setOrderDeliveredDate(new Date());

			} else if (customerOrderObj.getOrderStatus().equals(OrderStatus.DELIVERED)) {
				customerOrderObj.setOrderStatus(OrderStatus.COMPLETED);
				customerOrderObj.setNoOfStars(request.getNoOfStars());
				customerOrderObj.setReviews(request.getReviews());
				customerOrderObj.setNotes(request.getNotes());
			}

			result.setObject(customerOrderObj);
			return result;
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
			if (customerOrderObj.getOrderStatus().equals(OrderStatus.CONFIRMED)) {

				customerOrderObj.setStatus(Status.INACTIVE);
				customerOrderObj.setReason(request.getReason());
				customerOrderObj.setOrderStatus(OrderStatus.REJECTED);
				//customerOrderObj.setOrderRejectedDate(new Date());
			}

			result.setObject(customerOrderObj);
			return result;

		}
	 
	 
	
}
