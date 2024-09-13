package com.app.jobs;

import java.util.Date;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.app.entity.Currency;
import com.app.enumeration.Status;
import com.app.repository.CurrencyRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Component
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CurrencyMasterJob {
	
	private static final Logger log = LoggerFactory.getLogger(CurrencyMasterJob.class);

	private @NonNull CurrencyRepository currencyRepository;
     
    public void run() throws JSONException {
    	System.out.println("CurrencyMasterJob: Job Started: "+new Date());
    	RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("https://openexchangerates.org/api/currencies.json", String.class);
    	JSONObject jsonObject = new JSONObject(result);	
    	
    	Iterator<String> iter = jsonObject.keys();
    	while(iter.hasNext()){
    	   String code = (String) iter.next();
    	   String name = jsonObject.getString(code);
    	   Currency currency = new Currency();
    	   currency.setCode(code);
    	   currency.setName(name);
    	   currency.setBaseCurrency("USD");
    	   currency.setUpdatedDate(new Date());
    	   currency.setStatus(Status.ACTIVE);
    	   currencyRepository.saveAndFlush(currency);
    	}
		System.out.println("CurrencyMasterJob: Job Completed: "+new Date());
    }
    
    
}
