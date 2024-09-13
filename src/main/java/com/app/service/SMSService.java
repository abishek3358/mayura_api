package com.app.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
 

@Service
@Transactional
@Component
//@AllArgsConstructor(onConstructor_ = { @Autowired })
public class SMSService {
	
	private static final Logger logger = Logger.getLogger(SMSService.class);
	
	@Value("${twilio.account.sid}")
	private String ACCOUNT_SID;
	
	@Value("${twilio.auth.token}")
	private String AUTH_TOKEN;
	
	@Value("${twilio.message.id}")
	private String MSG_SID;

	
	public void send(String phoneNo, String text) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+91"+phoneNo),
                MSG_SID,text).create();
		System.out.println("SMS SENT TO PHONE: "+phoneNo+", SID"+message.getSid());
		logger.info("SMS SENT TO PHONE: "+phoneNo+", SID"+message.getSid());
	}
	
	public static void main(String[] args) {
		Twilio.init("AC7a6a044028ee854046b174c8b1e7620a", "58a1d8f84f258ffba8c02739d54207af");
		Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+919884512528"),
                "MG1389b7883d03bc0557ff229846238fa8",
                "Hey Ulaganathan, Welcome to India")
            .create();
		System.out.println(message.getSid());
	}
	
	 
	
}
