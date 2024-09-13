package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.dto.VendorAccountDTO;
import com.app.entity.Vendor;
import com.app.entity.VendorAccount;
import com.app.enumeration.RequestType;
import com.app.service.MessagePropertyService;
import com.app.service.VendorAccountService;
import com.app.service.VendorService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan 
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorAccountValidator {
	
    @Autowired
    MessagePropertyService messageSource; 
    
    private @NonNull VendorAccountService vendorAccountService;

    private @NonNull VendorService vendorService;
    
	List<String> errors = null;
    List<String> errorsObj = null;
    Optional<Subject> subject = null;

    /**
     * method for UnitOfMeasurement validation Added by Ulaganathan
     * @param vendorId 
     * 
     * @param httpHeader
     * @return ValidationResult
     * @throws Exception
     */
	 public ValidationResult validate(RequestType requestType,VendorAccountDTO request, UUID vendorId) {
		 
		errors = new ArrayList<>();
		VendorAccount vendorAccount = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<VendorAccount> vendorAccountOptional = vendorAccountService.findById(request.getId());
			if (!vendorAccountOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("vendor.account.not.found"));
			}
			vendorAccount = vendorAccountOptional.get();

		}

		  if (ValidationUtil.isNull(request.getCommissionPercentage())) {
				errors.add(messageSource.getMessage("vendor.account.commissionpercentage.required"));
			}
			
			if (ValidationUtil.isNullObject(request.getVendorId())) {
				errors.add(messageSource.getMessage("vendor.id.required"));
			}
			Optional<Vendor> vendorOptional = vendorService.findById(request.getVendorId());
			if (!vendorOptional.isPresent()) {
				errors.add(messageSource.getMessage("vendor.not.found"));
			}
		    ValidationResult result = new ValidationResult();
			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}
        if(null == vendorAccount) {
        	vendorAccount = VendorAccount.builder()        			                  
        			                    .vendorId(vendorId)
        			                    .commissionPercentage(request.getCommissionPercentage())
        			                    .payoutPeriod(request.getPayoutPeriod())
        			                    .build();
        }else {        	
        	vendorAccount.setVendorId(vendorId);
        	vendorAccount.setCommissionPercentage(request.getCommissionPercentage());
        	vendorAccount.setPayoutPeriod(request.getPayoutPeriod());        	
        }
        result.setObject(vendorAccount);
        return result;
	 }
	
}