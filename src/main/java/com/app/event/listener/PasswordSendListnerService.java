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
import com.app.event.PasswordSendEvent;

@Component
public class PasswordSendListnerService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordSendListnerService.class);

    @Autowired(required=true)
    @NonNull MailHelper mailHelper;
     
    @Async
    @EventListener
    public void handlePasswordSendEvent(PasswordSendEvent event) {
    	logger.error("PasswordSendEvent encountered : {}", event);
        System.out.println("Listener: PasswordSendEvent encountered : {}"+event.toString()+" @initiated at "+new Date());
         
        String toEmail = event.getEmailTo();
       
        Context context = new Context();
        context.setVariable("password",event.getPassword());
        String mail_subject = "Reset Password For Hastin Container";
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
