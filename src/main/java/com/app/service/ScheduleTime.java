package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class ScheduleTime {
	  @Autowired
	    private OfferDetailsService offerDetailsService;
	//  @Scheduled(cron = "0 */2 * * * *")
	  // @Scheduled(cron = "0 59 23 * * *") // Runs at 11:59 PM every day
	  @Scheduled(cron = "0 0 0 * * *")
	    public void updateProductionStatusTask() {
	    	offerDetailsService.updateProductionStatus();
	    }
	}