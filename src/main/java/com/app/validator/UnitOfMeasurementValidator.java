package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.UnitOfMeasurement;
import com.app.enumeration.RequestType;
import com.app.service.MessagePropertyService;
import com.app.service.UnitOfMeasurementService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan 
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class UnitOfMeasurementValidator {
	
    @Autowired
    MessagePropertyService messageSource; 
    
    private @NonNull UnitOfMeasurementService uomService;

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
	 public ValidationResult validate(RequestType requestType,UnitOfMeasurement request) {
		 
		errors = new ArrayList<>();
		UnitOfMeasurement unitOfMeasurement = null;

        if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
            Optional<UnitOfMeasurement> uomDuplicateObj = uomService.findByName(request.getName());
    		if(uomDuplicateObj.isPresent()) {
    			 String[] params = new String[]{request.getName()};
    			 errors.add( messageSource.getMessage("uom.duplicate",params));
    		}
        }else {
    		if (ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            } 
    		
    		Optional<UnitOfMeasurement> uomOptional = uomService.findById(request.getId());
            if (!uomOptional.isPresent()) {  
                throw new ObjectInvalidException(messageSource.getMessage("uom.not.found"));
            }
    		
            Optional<UnitOfMeasurement> uomDuplicateObj = uomService.findByNameExcludeId(request.getName(),request.getId());
    		if(uomDuplicateObj.isPresent()) {
    			String[] params = new String[]{request.getName()};
    			errors.add(messageSource.getMessage("uom.duplicate",params));
    		}
    		unitOfMeasurement = uomOptional.get();
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getName())) {
            errors.add(messageSource.getMessage("uom.name.required"));
        } else {
        	request.setName(ValidationUtil.getFormattedString(request.getName()));
            if (!ValidationUtil.isValidName(request.getName())) {
                errors.add(messageSource.getMessage("uom.name.invalid"));
            }
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getShortName())) {
            errors.add(messageSource.getMessage("uom.short.name.required"));
        }
//        else {
//        	request.setShortName(ValidationUtil.getFormattedString(request.getShortName()));
//            if (!ValidationUtil.isValidName(request.getShortName())) {
//                errors.add(messageSource.getMessage("uom.short.name.invalid"));
//            }
//        }
         
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        
        if(null == unitOfMeasurement) {
        	unitOfMeasurement = UnitOfMeasurement.builder()
        			.name(request.getName())
        			.shortName(request.getShortName())
        			.status(request.getStatus())
        			.build();
        }else {
        	unitOfMeasurement.setShortName(request.getShortName());
        	unitOfMeasurement.setName(request.getName());
        	unitOfMeasurement.setStatus(request.getStatus());
        }
        result.setObject(unitOfMeasurement);
        return result;
	 }
	
}