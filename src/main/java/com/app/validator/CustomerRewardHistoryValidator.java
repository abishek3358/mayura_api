package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.CustomerReward;
import com.app.entity.CustomerRewardHistory;
import com.app.enumeration.RequestType;
import com.app.service.CustomerRewardHistoryService;
import com.app.service.CustomerRewardService;
import com.app.service.MessagePropertyService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan 
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerRewardHistoryValidator {
	
    @Autowired
    MessagePropertyService messageSource; 
    
    private @NonNull CustomerRewardService customerRewardService;
    
    private @NonNull CustomerRewardHistoryService customerRewardHistoryService;

	List<String> errors = null;
    List<String> errorsObj = null;
    Optional<Subject> subject = null;

    /**
     * method for UnitOfMeasurement validation Added by Ulaganathan
     * 
     * @param httpHeader
     * @return ValidationResult
     * @throws Exception
     */
	 public ValidationResult validate(RequestType requestType,CustomerRewardHistory request) {
		 
		errors = new ArrayList<>();
		CustomerRewardHistory customerRewardHistory = null;

        if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
           
        }else {
    		if (ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            } 
    		
    		Optional<CustomerRewardHistory> uomOptional = customerRewardHistoryService.findById(request.getId());
            if (!uomOptional.isPresent()) {  
                throw new ObjectInvalidException(messageSource.getMessage("customer.reward.not.found"));
            }
    		

            customerRewardHistory = uomOptional.get();
        }
    	Optional<CustomerReward> customerReward = null;
		if (ValidationUtil.isNull(request.getRewardId())) {
			errors.add(messageSource.getMessage("reward.id.not.found"));
		} else {
			customerReward = customerRewardService.findById(request.getRewardId());
			if (!customerReward.isPresent()) {
				errors.add(messageSource.getMessage("reward.not.found"));
			}
		}
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        
        if(null == customerRewardHistory) {
        	customerRewardHistory = CustomerRewardHistory.builder()
        		
        			.rewardId(request.getRewardId())
        			.rewards(request.getRewards()) 
        			.transactionDate(request.getTransactionDate())        			
        			.build();
        }else {
        	customerRewardHistory.setRewardId(request.getRewardId());
        	customerRewardHistory.setRewards(request.getRewards());
        	customerRewardHistory.setTransactionDate(request.getTransactionDate());
        	
        }
        result.setObject(customerRewardHistory);
        return result;
	 }
	
}