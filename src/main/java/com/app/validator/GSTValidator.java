package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.GST;
import com.app.entity.ProductType;
import com.app.enumeration.RequestType;
import com.app.service.GSTService;
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
public class GSTValidator {
	
    @Autowired
    MessagePropertyService messageSource;
    
    private @NonNull GSTService gstService;

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
	 public ValidationResult validate(RequestType requestType,GST request) {
		 
		errors = new ArrayList<>();
		GST gstObj = null;

        if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
            Optional<GST> gstDuplicateObj = gstService.findByName(request.getName());
    		if(gstDuplicateObj.isPresent()) {
    			 String[] params = new String[]{request.getName()};
    			 errors.add( messageSource.getMessage("gst.duplicate",params));
    		}
        }else {
    		if (ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            } 
    		
    		Optional<GST> gstOptional = gstService.findById(request.getId());
            if (!gstOptional.isPresent()) {  
                throw new ObjectInvalidException(messageSource.getMessage("gst.not.found"));
            }
    		
            Optional<GST> gstDuplicateObj = gstService.findByNameExcludeId(request.getName(),request.getId());
    		if(gstDuplicateObj.isPresent()) {
    			String[] params = new String[]{request.getName()};
    			errors.add(messageSource.getMessage("gst.duplicate",params));
    		}
    		gstObj = gstOptional.get();
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getName())) {
            errors.add(messageSource.getMessage("gst.name.required"));
        } else {
        	//request.setName(ValidationUtil.getFormattedString(request.getName()));
            if (!ValidationUtil.validAlphaNumericsNumbers(request.getName())) {
                errors.add(messageSource.getMessage("gst.name.invalid"));
            }
        }
        
        if (ValidationUtil.isNull(request.getGst())) {
            errors.add(messageSource.getMessage("gst.gst.percentage.required"));
        } 
        
        if (ValidationUtil.isNull(request.getCgst())) {
            errors.add(messageSource.getMessage("gst.cgst.percentage.required"));
        } 
        
        if (ValidationUtil.isNull(request.getSgst())) {
            errors.add(messageSource.getMessage("gst.sgst.percentage.required"));
        } 
       
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        
        if(null == gstObj) {
        	gstObj = GST.builder().name(request.getName()).gst(request.getGst()).cgst(request.getCgst()).sgst(request.getSgst()).igst(request.getIgst())
        			.description(request.getDescription())
            		.build();
        }else {
        	gstObj.setName(request.getName());
        	gstObj.setDescription(request.getDescription());
        	gstObj.setGst(request.getGst());
        	gstObj.setCgst(request.getCgst());
        	gstObj.setSgst(request.getSgst());
        	gstObj.setIgst(request.getIgst());
        	gstObj.setStatus(request.getStatus());
        }
        result.setObject(gstObj);
        return result;
	 }
	
}
