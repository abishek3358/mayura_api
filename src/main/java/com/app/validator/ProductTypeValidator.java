package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.ProductType;
import com.app.enumeration.RequestType;
import com.app.service.MessagePropertyService;
import com.app.service.ProductTypeService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan 
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProductTypeValidator {
	
    @Autowired
    MessagePropertyService messageSource;
    
    private @NonNull ProductTypeService productTypeService;

	List<String> errors = null;
    List<String> errorsObj = null;
    Optional<Subject> subject = null;

    /**
     * method for product type validation Added by Ulaganathan
     * 
     * @param httpHeader
     * @return ValidationResult
     * @throws Exception
     */
	 public ValidationResult validate(RequestType requestType,ProductType request) {
		 
		errors = new ArrayList<>();
		ProductType productType = null;

        if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
            Optional<ProductType> productTypeDuplicateObj = productTypeService.findByName(request.getName());
    		if(productTypeDuplicateObj.isPresent()) {
    			 String[] params = new String[]{request.getName()};
    			 errors.add( messageSource.getMessage("product.type.duplicate",params));
    		}
        }else {
    		if (ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            } 
    		
    		Optional<ProductType> productTypeOptional = productTypeService.findById(request.getId());
            if (!productTypeOptional.isPresent()) {  
                throw new ObjectInvalidException(messageSource.getMessage("product.type.not.found"));
            }
    		
            Optional<ProductType> productTypeDuplicateObj = productTypeService.findByNameExcludeId(request.getName(),request.getId());
    		if(productTypeDuplicateObj.isPresent()) {
    			String[] params = new String[]{request.getName()};
    			errors.add(messageSource.getMessage("product.type.duplicate",params));
    		}
    		productType = productTypeOptional.get();
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getName())) {
            errors.add(messageSource.getMessage("product.type.name.required"));
        } else {
        	request.setName(ValidationUtil.getFormattedString(request.getName()));
            if (!ValidationUtil.isValidName(request.getName())) {
                errors.add(messageSource.getMessage("product.type.name.invalid"));
            }
        }
        
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        
        if(null == productType) {
        	productType = ProductType.builder().name(request.getName()).description(request.getDescription())
            		.build();
        }else {
        	productType.setName(request.getName());
        	productType.setDescription(request.getDescription());
        	productType.setStatus(request.getStatus());
        }
        result.setObject(productType);
        return result;
	 }
	
}
