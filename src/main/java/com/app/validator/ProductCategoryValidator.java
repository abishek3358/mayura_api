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
import com.app.entity.ProductCategory;
import com.app.entity.ProductType;
import com.app.enumeration.RequestType;
import com.app.service.GSTService;
import com.app.service.MessagePropertyService;
import com.app.service.ProductCategoryService;
import com.app.service.ProductTypeService;
import com.app.service.WarehouseService;
import com.app.util.ValidationUtil;
import com.app.util.file.AWSS3DirectoryUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProductCategoryValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull ProductCategoryService productCategoryService;

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
	public ValidationResult validate(RequestType requestType, ProductCategory request) {

		errors = new ArrayList<>();
		ProductCategory productCategory = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<ProductCategory> productCatDuplicateObj = productCategoryService.findByName(request.getName());
			if (productCatDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getName() };
				errors.add(messageSource.getMessage("product.category.duplicate", params));
			}
		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

			Optional<ProductCategory> productCatOptional = productCategoryService.findById(request.getId());
			if (!productCatOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("product.category.not.found"));
			}

			Optional<ProductCategory> productCatDuplicateObj = productCategoryService
					.findByNameExcludeId(request.getName(), request.getId());
			if (productCatDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getName() };
				errors.add(messageSource.getMessage("product.category.duplicate", params));
			}
			productCategory = productCatOptional.get();
		}

		if (ValidationUtil.isNullOrEmpty(request.getName())) {
			errors.add(messageSource.getMessage("product.category.name.required"));
		} /*
			 * else { request.setName(ValidationUtil.getFormattedString(request.getName()));
			 * if (!ValidationUtil.isValidName(request.getName())) {
			 * errors.add(messageSource.getMessage("product.category.name.invalid")); } }
			 */

		if (ValidationUtil.isNullOrEmpty(request.getShortName())) {
			errors.add(messageSource.getMessage("product.category.short.name.required"));
		} /*
			 * else {
			 * request.setShortName(ValidationUtil.getFormattedString(request.getShortName()
			 * )); if (!ValidationUtil.isValidName(request.getShortName())) {
			 * errors.add(messageSource.getMessage("product.category.short.name.invalid"));
			 * } }
			 */
		 if (ValidationUtil.isNullOrEmpty(request.getFileName())) {
	            errors.add(messageSource.getMessage("product.category.filename.required"));
	        } 
	        else {
	        	if(!(request.getFileName().toLowerCase().endsWith(".jpg") || request.getFileName().toLowerCase().endsWith(".jpeg")
	        			 || request.getFileName().toLowerCase().endsWith("png")))
	        	{
	        		errors.add(messageSource.getMessage("product.category.filename.invalid"));
	        	}        
	        }

		Optional<ProductType> productType = null;
		if (ValidationUtil.isNull(request.getProductTypeId())) {
			errors.add(messageSource.getMessage("product.type.not.found"));
		} else {
			productType = productTypeService.findById(request.getProductTypeId());
			if (!productType.isPresent()) {
				errors.add(messageSource.getMessage("product.type.not.found"));
			}
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == productCategory) {
			productCategory = ProductCategory.builder()

					.name(request.getName())
					.shortName(request.getShortName())
					.fileName(request.getFileName())
					.fileBase64(request.getFileBase64())					
					.productTypeId(request.getProductTypeId())
					.status(request.getStatus())
					.name(request.getName()).shortName(request.getShortName())
					.fileName(request.getFileName()).fileBase64(request.getFileBase64())
					.description(request.getDescription())
					.productTypeId(productType.get().getId()).status(request.getStatus())
					.build();
		} else {
			productCategory.setName(request.getName());
			productCategory.setShortName(request.getShortName());
			productCategory.setProductTypeId(request.getProductTypeId());
			productCategory.setStatus(request.getStatus());
			productCategory.setDescription(request.getDescription());
			productCategory.setFileName(request.getFileName());
			productCategory.setFileUrl(request.getFileUrl());
			productCategory.setFileBase64(request.getFileBase64());
			productCategory.setStatus(request.getStatus());
		}
		result.setObject(productCategory);
		return result;
	}

}
