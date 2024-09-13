package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.Brand;
import com.app.entity.UnitOfMeasurement;
import com.app.enumeration.RequestType;
import com.app.service.BrandService;
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
public class BrandValidator {
	
    @Autowired
    MessagePropertyService messageSource; 
    
    private @NonNull BrandService brandService;

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
	 public ValidationResult validate(RequestType requestType,Brand request) {
		 
		errors = new ArrayList<>();
		Brand brand = null;

        if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
            Optional<Brand> uomDuplicateObj = brandService.findByBrandName(request.getBrandName());
    		if(uomDuplicateObj.isPresent()) {
    			 String[] params = new String[]{request.getBrandName()};
    			 errors.add( messageSource.getMessage("brand.duplicate",params));
    		}
        }else {
    		if (ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            } 
    		
    		Optional<Brand> uomOptional = brandService.findById(request.getId());
            if (!uomOptional.isPresent()) {  
                throw new ObjectInvalidException(messageSource.getMessage("brand.not.found"));
            }
    		

    		brand = uomOptional.get();
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getBrandName())) {
            errors.add(messageSource.getMessage("brand.name.required"));
        } else {
        	request.setBrandName(ValidationUtil.getFormattedString(request.getBrandName()));
            if (!ValidationUtil.isValidName(request.getBrandName())) {
                errors.add(messageSource.getMessage("brand.name.invalid"));
            }
        }
        if (ValidationUtil.isNullOrEmpty(request.getFileName())) {
            errors.add(messageSource.getMessage("brand.filename.required"));
        } 
        else {
        	if(!(request.getFileName().toLowerCase().endsWith(".jpg") || request.getFileName().toLowerCase().endsWith(".jpeg")
        			 || request.getFileName().toLowerCase().endsWith("png")))
        	{
        		errors.add(messageSource.getMessage("brand.filename.invalid"));
        	}        
        }
        
  
         
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        
        if(null == brand) {
        	brand = Brand.builder()
        			.brandName(request.getBrandName())
        			.description(request.getDescription()) 
        			.fileName(request.getFileName())
        			.fileUrl(request.getFileUrl())
        			.fileBase64(request.getFileBase64())
        			.build();
        }else {
        	brand.setBrandName(request.getBrandName());
        	brand.setDescription(request.getDescription());
        	brand.setFileName(request.getFileName());
        	brand.setFileUrl(request.getFileUrl());
        	brand.setFileBase64(request.getFileBase64());
        	
        }
        result.setObject(brand);
        return result;
	 }
	
}