package com.app.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.FeedbackSetting;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.FeedbackSettingService;
import com.app.service.MessagePropertyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/feedbackSetting")
@Api(value = "feedbackSetting rest api", description = "Defines endpoints for the feedbackSetting. It's  secured by default.", consumes = "application/json", produces = "application/json")
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class FeedbackSettingController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MessagePropertyService messageSource;

	@Autowired
	ResponseGenerator responseGenerator;

	private @NonNull FeedbackSettingService feedbackSettingService ;

	@ApiOperation(value = "Allows to create new feedbackSetting .", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> createPayrollGenerate(@RequestBody FeedbackSetting feedbackSetting,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		logger.info("payroll generate create started {}", Thread.currentThread().getName(), LocalDateTime.now());
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		
		try {
			 
			feedbackSettingService.saveFeedbackSetting(feedbackSetting);
					       
			return responseGenerator.successResponse(context, messageSource.getMessage("feedbackSetting.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			logger.error("error occured while create payroll generate  {}", e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}
	
	
}
