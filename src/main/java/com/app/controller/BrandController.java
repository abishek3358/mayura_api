package com.app.controller;

import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.BrandDTO;
import com.app.entity.Brand;
import com.app.enumeration.ImageType;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.BrandService;
import com.app.service.FileService;
import com.app.service.MessagePropertyService;
import com.app.util.message.ResponseMessage;
import com.app.validator.BrandValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/brand")
@Api(value = "Brand Rest API", produces = "application/json", consumes = "application/json")
public class BrandController {
	private static final Logger logger = Logger.getLogger(BrandController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull BrandService brandService;

	private @NonNull BrandValidator validatorService;
	
	private @NonNull FileService fileService;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new brand.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The Brand request payload") @RequestBody Brand request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		brandService.saveOrUpdate((Brand) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("brand.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing brand.", response = Response.class)
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The brand request payload") @RequestBody Brand brand,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
//		brandService.saveOrUpdate((Brand) validationResult.getObject());
//		try {
//			return responseGenerator.successResponse(context, messageSource.getMessage("brand.update"), HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage(), e);
//			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
		logger.info("brand update started {}");
		if (null == brand) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}
		Brand brandObj = brandService.findByNameId(brand.getBrandName(), brand.getId());
		if (null != brandObj) {

			return responseGenerator.errorResponse(context, messageSource.getMessage("brand.duplicate"),
					HttpStatus.BAD_REQUEST);
		}

		try {

			brandService.updateBrand(brand);

			return responseGenerator.successResponse(context, messageSource.getMessage("brand.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			logger.error("error occured while update attendance {}", e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}
	

	

	@ApiOperation(value = "Allows to fetch Brands by brand id.", response = Response.class)
	@GetMapping(value = "/get/{brandId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("brandId") UUID brandId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<Brand> brand = brandService.findById(brandId);
			if (!brand.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
			BrandDTO brandObj = brandService.getById(brandId);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("brand.get"), brandObj,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to get all Brands.", response = Response.class)
	@GetMapping(value = "/get/brands", produces = "application/json")
	public ResponseEntity<?> getAllBrands(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("brand.get"),
					brandService.getAllBrands(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete Brand by brand id.", response = Response.class)
	@DeleteMapping(value = "/delete/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			brandService.deleteById(id);
			return responseGenerator.successResponse(context, messageSource.getMessage("brand.delete"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("brand.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}
	@ApiOperation(value = "Allows to fetch E-Queue document dwonloads.", response = Response.class)
	@GetMapping(value = "/download/{brandId}", produces = "application/json")
	public ResponseEntity<?> download(@PathVariable("brandId") UUID brandId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<Brand> product = brandService.findById(brandId);
		if (!product.isPresent()) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}
	//	String fileName=product.get().getFileName();
		byte[] data = fileService.downloadImage(ImageType.BRAND,brandId,product.get().getFileName());

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
	@ApiOperation(value = "Allows to fetch all Brands.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("brand.get"),
					brandService.getAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
