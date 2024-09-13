package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.City;
import com.app.entity.Discount;
import com.app.entity.GST;
import com.app.entity.Product;
import com.app.entity.Warehouse;
import com.app.enumeration.ProductDiscountType;
import com.app.enumeration.RequestType;
import com.app.repository.ProductDtlRepository;
import com.app.repository.ProductImageRepository;
import com.app.service.DiscountService;
import com.app.service.MessagePropertyService;
import com.app.service.ProductCategoryService;
import com.app.service.ProductService;
import com.app.service.UnitOfMeasurementService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class DiscountValidator {
	
	@Autowired
    MessagePropertyService messageSource;
	
	private @NonNull DiscountService discountService;
	
	List<String> errors = null;
    List<String> errorsObj = null;
    Optional<Subject> subject = null;
    
    public ValidationResult validate(RequestType requestType,Discount request) {
    	
    	errors = new ArrayList<>();
		Discount discount = null;
		
		if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
            Optional<Discount> DiscountDuplicateObj = discountService.findByDiscountCode(request.getDiscountCode());
    		if(DiscountDuplicateObj.isPresent()) {
    			 String[] params = new String[]{request.getDiscountCode()};
    			 errors.add( messageSource.getMessage("discount.code.duplicate",params));
    		}
            
        }else {
    		if (ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
    		
    		Optional<Discount> discountOptional = discountService.findById(request.getId());
            if (!discountOptional.isPresent()) {  
                throw new ObjectInvalidException(messageSource.getMessage("discount.is.not.found"));
            }
            discount=discountOptional.get();
        }
    		
        if (ValidationUtil.isNullOrEmpty(request.getDiscountCode())) {
            errors.add(messageSource.getMessage("discount.code.required"));
        } else {
        	request.setDiscountCode(ValidationUtil.getFormattedString(request.getDiscountCode()));
        }
        
        if (ValidationUtil.isNullObject(request.getDiscountType())) {
            errors.add(messageSource.getMessage("discount.type.required"));
        } 
        
        if (ValidationUtil.isNullOrEmpty(request.getDiscountValue())){
        	errors.add(messageSource.getMessage("discount.value.required"));
        }   else {
        	if(request.getDiscountType().equals(ProductDiscountType.PERCENTAGE)) {
        		 
        		if (100 < Integer.parseInt(request.getDiscountValue())) {
 		            errors.add(messageSource.getMessage("dicount.precentage.value.required"));
 		        }  else {
 		        	request.setDiscountValue(request.getDiscountValue());
 		        }
        	}
        }
        
        
        if (ValidationUtil.isNull(request.getMinOrderValue())) {
            errors.add(messageSource.getMessage("min.order.value.required"));
        }  
        
        if (ValidationUtil.isNullOrEmpty(request.getDiscountDescription())) {
            errors.add(messageSource.getMessage("discount.description.required"));
        } else {
        	request.setDiscountDescription(ValidationUtil.getFormattedString(request.getDiscountDescription()));
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getStartDate())) {
            errors.add(messageSource.getMessage("startdate.required"));
        } else {
        	request.setStartDate(ValidationUtil.getFormattedString(request.getStartDate()));
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getExpiryDate())) {
            errors.add(messageSource.getMessage("expirydate.required"));
        } else {
        	request.setExpiryDate(ValidationUtil.getFormattedString(request.getExpiryDate()));
        }
        
        if(0<(request.getStartDate().compareTo(request.getExpiryDate()))) {
       	 errors.add(messageSource.getMessage("less.than.in.startdate"));
       }else {
       	request.setExpiryDate(ValidationUtil.getFormattedString(request.getExpiryDate()));
       }
        
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        
        if(null == discount) {
        	discount = Discount.builder().discountCode(request.getDiscountCode())
            		.discountType(request.getDiscountType())
            		.discountValue(request.getDiscountValue())
            		.discountDescription(request.getDiscountDescription())
            		.minOrderValue(request.getMinOrderValue())
            		.startDate(request.getStartDate())
            		.expiryDate(request.getExpiryDate())
            		.build();
        }else {
        	discount.setDiscountCode(request.getDiscountCode());
        	discount.setDiscountType(request.getDiscountType());
        	discount.setDiscountValue(request.getDiscountValue());
        	discount.setDiscountDescription(request.getDiscountDescription());
        	discount.setMinOrderValue(request.getMinOrderValue());
        	discount.setStartDate(request.getStartDate());
        	discount.setExpiryDate(request.getExpiryDate());
        	discount.setStatus(request.getStatus());
        }
        result.setObject(discount);
        return result;
    }
    
}

    
	 			

   
    

