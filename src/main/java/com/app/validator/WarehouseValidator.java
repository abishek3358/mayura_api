package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.City;
import com.app.entity.Warehouse;
import com.app.enumeration.RequestType;
import com.app.repository.CityRepository;
import com.app.service.MessagePropertyService;
import com.app.service.WarehouseService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan 
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class WarehouseValidator {
	
    @Autowired
    MessagePropertyService messageSource;
    
    private @NonNull CityRepository cityRepository;
    
    private @NonNull WarehouseService warehouseService;

	List<String> errors = null;
    List<String> errorsObj = null;
    Optional<Subject> subject = null;

    /**
     * method for warehouse validation Added by Ulaganathan
     * 
     * @param httpHeader
     * @return ValidationResult
     * @throws Exception
     */
	 public ValidationResult validate(RequestType requestType,Warehouse request) {
		 
		errors = new ArrayList<>();
		Warehouse wareHouseObj = null;

        if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            }
            Optional<Warehouse> wareHouseDuplicateObj = warehouseService.findByName(request.getName());
    		if(wareHouseDuplicateObj.isPresent()) {
    			 String[] params = new String[]{request.getName()};
    			 errors.add( messageSource.getMessage("warehouse.duplicate",params));
    		}
        }else {
    		if (ValidationUtil.isNull(request.getId())) {
                throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
            } 
    		
    		Optional<Warehouse> warehouseOptional = warehouseService.findById(request.getId());
            if (!warehouseOptional.isPresent()) {  
                throw new ObjectInvalidException(messageSource.getMessage("warehouse.not.found"));
            }
    		
    		Optional<Warehouse> wareHouseDuplicateObj = warehouseService.findByNameExcludeId(request.getName(),request.getId());
    		if(wareHouseDuplicateObj.isPresent()) {
    			String[] params = new String[]{request.getName()};
    			errors.add(messageSource.getMessage("warehouse.duplicate",params));
    		}
            wareHouseObj = warehouseOptional.get();
        }

       
        
        if (ValidationUtil.isNullOrEmpty(request.getName())) {
            errors.add(messageSource.getMessage("warehouse.name.required"));
        } else {
        	request.setName(ValidationUtil.getFormattedString(request.getName()));
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getShortName())) {
            errors.add(messageSource.getMessage("warehouse.short.name.required"));
        } else {
        	request.setShortName(ValidationUtil.getFormattedString(request.getShortName()));
        }
        
        if (ValidationUtil.isNullOrEmpty(request.getAddress())) {
            errors.add(messageSource.getMessage("warehouse.address.required"));
        } else {
        	request.setAddress(ValidationUtil.getFormattedString(request.getAddress()));
            if (!ValidationUtil.isValidAddress(request.getAddress())) {
                errors.add(messageSource.getMessage("warehouse.address.invalid"));
            }
        }
        
        Optional<City> city = null;
        if (ValidationUtil.isNull(request.getCityId())) {
            errors.add(messageSource.getMessage("warehouse.city.required"));
        } else {
        	city = cityRepository.findById(request.getCityId());
            if (!city.isPresent()) {
                errors.add(messageSource.getMessage("city.invalid"));
            }
        }
        
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        
        if(null == wareHouseObj) {
        	wareHouseObj = Warehouse.builder().address(request.getAddress()).
            		cityObj(city.get()).name(request.getName()).shortName(request.getShortName()).remarks(request.getRemarks())
            		.build();
        }else {
        	wareHouseObj.setAddress(request.getAddress());
        	wareHouseObj.setCityObj(city.get());
        	wareHouseObj.setShortName(request.getShortName());
        	wareHouseObj.setName(request.getName());
        	wareHouseObj.setRemarks(request.getRemarks());
        	wareHouseObj.setStatus(request.getStatus());
        }
        result.setObject(wareHouseObj);
        return result;
	 }
	
}
