package com.app.event.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.app.email.helper.MailHelper;
import com.app.event.OtpEvent;

@Component
public class OtpListnerService {

    private static final Logger logger = LoggerFactory.getLogger(OtpListnerService.class);

    @Autowired(required=true)
    @NonNull MailHelper mailHelper;
     
    @Async
    @EventListener
    public void handleOtpEvent(OtpEvent event) {
    	logger.debug("OtpEvent encountered : {}", event);
        System.out.println("Listener: OtpEvent encountered : {}"+event.toString()+" @initiated at "+new Date());
         
        String toEmail = event.getEmailTo();
       
        Context context = new Context();
        context.setVariable("otp",event.getOtp());
        String mail_subject = "Verification for HASTIN CONTAINER sign up";
        String mail_template = "otp/signUp";
        
        try {
        	mailHelper.sendMail(toEmail, null, mail_subject, mail_template, context);
            logger.debug("Booking CRO Release Confirmation email sent");
        } catch (Exception e) {
            // simply log it and go on...
        	logger.error("Sending CRO Release Confirmation failed for user..", e);
        }
    } 
    
    
   }
