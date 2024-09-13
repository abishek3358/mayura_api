package com.app.controller;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ProductCategoryDTO;
import com.app.entity.Product;
import com.app.entity.ProductCategory;
import com.app.entity.ProductImage;
import com.app.enumeration.ImageType;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.FileService;
import com.app.service.MessagePropertyService;
import com.app.service.ProductCategoryService;
import com.app.util.message.ResponseMessage;
import com.app.validator.ProductCategoryValidator;
import com.app.validator.ValidationResult;
import com.twilio.rest.api.v2010.Account.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/product/category")
@Api(value = "Product Category Rest API", produces = "application/json", consumes = "application/json")
public class ProductCategoryController {
	
	private static final Logger logger = Logger.getLogger(ProductCategoryController.class);
	
	private @NonNull ResponseGenerator responseGenerator;
	
	private @NonNull ProductCategoryService productCategoryService;
	
	private @NonNull ProductCategoryValidator validatorService;
	
	private @NonNull FileService fileService;
	 
	@Autowired
	MessagePropertyService messageSource;
	
	@ApiOperation(value = "Allows to create new product category.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The product category request payload") @RequestBody ProductCategory request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		productCategoryService.saveOrUpdate((ProductCategory) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("product.category.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to update existing product category.", response = Response.class)
	@PutMapping(value = "/update",  produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The product category request payload") @RequestBody ProductCategory request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		 
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		productCategoryService.saveOrUpdate((ProductCategory) validationResult.getObject());
		
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("product.category.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	

	@ApiOperation(value = "Allows to fetch all product categories.", response = Response.class)
	@GetMapping(value = "/get/{productCategoryId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("productCategoryId") UUID productCategoryId,@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<ProductCategory> productCategory = productCategoryService.findById(productCategoryId);
			if(!productCategory.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT, HttpStatus.BAD_REQUEST);
			}
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.category.get"),productCategory.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//download image	

	@ApiOperation(value = "Allows to fetch student document dwonloads.", response = Response.class)
	@GetMapping(value = "/download/{studentId}", produces = "application/json")
	public ResponseEntity<?> download(@PathVariable("studentId") UUID studentId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<ProductCategory> product = productCategoryService.findById(studentId);
		if (!product.isPresent()) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}

		//List<ProductImage> productImageList = productImageRepository.findByProductObj(product);
		// for(ProductImage obj:productImageList) {
		byte[] data = fileService.downloadImage(ImageType.CATEGORY, studentId, product.get().getFileName());

		String fileNameText = product.get().getFileName();
		String fileName = URLEncoder.encode(fileNameText, "UTF-8").replaceAll("\\+", "%20");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(data.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);
		httpHeaders.set("fileName", fileName);
		httpHeaders.add("fileName", fileName);

		try {
			return new ResponseEntity<>(data, httpHeaders, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Allows to fetch all product categories.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
	List<ProductCategoryDTO> productcategoryList = productCategoryService.getAllCategorielst();
//			
//			Comparator<ProductCategoryDTO> compareByName = (ProductCategoryDTO o1, ProductCategoryDTO o2) -> o1.getName().compareToIgnoreCase( o2.getName() );
//			 
//			Collections.sort(productcategoryList, compareByName);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.category.get"), productcategoryList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	 
}
