package com.app.jobs;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.entity.Currency;
import com.app.repository.CurrencyRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Component
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CurrencyUtil implements InitializingBean {
	
	private @NonNull CurrencyRepository currencyRepository;
    
    private static Map<UUID, Currency> currencyIdMap = new HashMap<UUID, Currency>();
    private static Map<String, Currency> currencyCodeMap = new HashMap<String, Currency>();
	
    
	@Override
	public void afterPropertiesSet() throws Exception {
		run();
	}
	
	public static Currency getCurrencyObjById(UUID currencyId) {
		return currencyIdMap.get(currencyId);
	}
	
	public static Currency getCurrencyObjByCode(String currencyCode) {
		return currencyCodeMap.get(currencyCode.toLowerCase());
	}
	
	public static Collection<Currency> getAllCurrencies() {
		return currencyIdMap.values();
	}
	
    public void run() {
    	List<Currency> list = currencyRepository.findAll();
    	Map<UUID, Currency> currencyIdMapTemp = new HashMap<UUID, Currency>();
    	Map<String, Currency> currencyCodeMapTemp = new HashMap<String, Currency>();
    	for (Currency currency : list) {
    		if(null == currency.getName() || currency.getName().isEmpty()) {
    			continue;
    		}
    		currencyIdMapTemp.put(currency.getId(), currency);
    		currencyCodeMapTemp.put(currency.getCode().toLowerCase(), currency);
		}
    	currencyIdMap.clear();
    	currencyIdMap.putAll(currencyIdMapTemp);
    	currencyCodeMap.clear();
    	currencyCodeMap.putAll(currencyCodeMapTemp);
    }
    
}
