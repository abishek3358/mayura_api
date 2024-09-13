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
import com.app.entity.Vendor;
import com.app.entity.VendorContactPerson;
import com.app.enumeration.RequestType;
import com.app.service.MessagePropertyService;
import com.app.service.VendorContactPersonService;
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
public class VendorContactPersonValidator {
	
    @Autowired
    MessagePropertyService messageSource; 
    
    private @NonNull VendorContactPersonService contactPersonService;

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
	 public ValidationResult validate(RequestType requestType,VendorContactPerson request, UUID vendorId) {
		 
		errors = new ArrayList<>();
		VendorContactPerson vendorContactPerson = null;

		String defaultMarkUs="Yes";
		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<VendorContactPerson> contactPersonOptional = contactPersonService.findById(request.getId());
			if (!contactPersonOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("vendor.contact.person.not.found"));
			}
			vendorContactPerson = contactPersonOptional.get();

		}

		  if (ValidationUtil.isNullOrEmpty(request.getFullName())) {
				errors.add(messageSource.getMessage("vendor.contactperson.name.required"));
			}
			if (ValidationUtil.isNullOrEmpty(request.getEmailId())) {
				errors.add(messageSource.getMessage("vendor.contactperson.mail.required"));
			}
			if (ValidationUtil.isNullOrEmpty(request.getPhoneNumber())) {
				errors.add(messageSource.getMessage("vendor.contactperson.phno.required"));
			}
			if (ValidationUtil.isNullOrEmpty(request.getContactPersonRole())) {
				errors.add(messageSource.getMessage("vendor.contactperson.role.required"));
			}
			
			if (ValidationUtil.isNullObject(request.getVendorId())) {
				errors.add(messageSource.getMessage("vendor.name.required"));
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
        if(null == vendorContactPerson) {
        	vendorContactPerson = VendorContactPerson.builder()
        			                    .fullName(request.getFullName())
        			                    .vendorId(vendorId)
        			                    .emailId(request.getEmailId())
        			                    .phoneNumber(request.getPhoneNumber())
        			                    .contactPersonRole(request.getContactPersonRole())
        			                    .markAsDefaultDb(request.getMarkAsDefault().equals(defaultMarkUs)?true:false)
        			                    .build();
        }else {
        	vendorContactPerson.setFullName(request.getFullName());
        	vendorContactPerson.setVendorId(vendorId);
        	vendorContactPerson.setEmailId(request.getEmailId());
        	vendorContactPerson.setPhoneNumber(request.getPhoneNumber());
        	vendorContactPerson.setContactPersonRole(request.getContactPersonRole());
        	vendorContactPerson.setMarkAsDefaultDb(request.getMarkAsDefault().equals(defaultMarkUs)?true:false);
        }
        result.setObject(vendorContactPerson);
        return result;
	 }
	
}