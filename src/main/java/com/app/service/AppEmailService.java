package com.app.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import com.app.email.helper.MailHelper;
import com.app.entity.CustomerOrder;
import com.app.util.ValidationUtil;

import lombok.NonNull;

@Service
@Transactional
@Component
//@AllArgsConstructor(onConstructor_ = { @Autowired })
public class AppEmailService {
	
	private static final Logger logger = Logger.getLogger(AppEmailService.class);
	
	@Value("${email.order.bcc}")
	private String EMAIL_ORDER_BCC;
	
	@Autowired(required=true)
    private @NonNull MailHelper mailHelper;
	
    public void triggerOrderConfirmation(CustomerOrder obj, String title, String body ) {
    	
    	if(ValidationUtil.isNullOrEmpty(obj.getCustomerObj().getEmail())) {
    		return;
    	}
        String toEmail = obj.getCustomerObj().getEmail();
       
        Context context = new Context();
        context.setVariable("custName",obj.getCustomerObj().getFullName());
        context.setVariable("message",body);
        String mail_template = "otp/signUp";
        
        try {
        	mailHelper.sendMail(toEmail, EMAIL_ORDER_BCC, title, mail_template, context,null);
            logger.debug("Order confirmation email triggered for order id :"+obj.getId());
        } catch (Exception e) {
        	logger.error("Sending Order Confirmation failed for user..", e);
        }
    } 
		
}
