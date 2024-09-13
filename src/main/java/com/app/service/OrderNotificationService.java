package com.app.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.CustomerOrder;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
public class OrderNotificationService {
	
	private static final Logger logger = Logger.getLogger(OrderNotificationService.class);
	
	@Autowired(required=true)
	private @NonNull SMSService smsService;
	
	@Autowired(required=true)
	private @NonNull AppEmailService emailService;
	
	String message = "", title="";
	
	public void orderConfirmation(CustomerOrder obj) throws Exception {
		message = "Thank you for placing order with us. Your order number is"+obj.getOrderNo()+". "
				+ "Amount to be paid on delivery:Rs."+obj.getGrandTotal()+". "
				+ "Please check your email for more details.";
		title = "Your VNF order #"+obj.getOrderNo()+"";
		
		smsService.send(obj.getCustomerObj().getMobileNo(), message);
		
		emailService.triggerOrderConfirmation(obj, title, message);
	}
	
	public void outForDelivery(CustomerOrder obj) throws Exception {
	    message = "Out for Delivery: Your order "+obj.getOrderNo()+" from vnf, "
				+ "will be delivered today by our delivery master.";
	    
	    String mobileNo=obj.getCustomerObj().getMobileNo().toString();
		smsService.send(mobileNo, message);
	}
	 
	
	public void orderDelivered(CustomerOrder obj) throws Exception {
		message = "Order Delivered: Hi Keerthana, Smile, your order (ID: "+obj.getOrderNo()+") "
				+ "has been successfully delivered. Keep spreading goodness and shopping with us!\r\n"
				+ "Please share your valuable feedback.";
		smsService.send(obj.getCustomerObj().getMobileNo(), message);
	}
	
	public void orderCancelled(CustomerOrder obj) throws Exception {
		message = "Your request for cancellation of your order no. "+obj.getOrderNo()+" has been processed."
				+ "For further details please check your email";
		smsService.send(obj.getCustomerObj().getMobileNo(), message);
	}
	
	 
}
