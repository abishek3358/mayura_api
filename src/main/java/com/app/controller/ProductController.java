package com.app.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AllProductDTO;
import com.app.dto.ProductDTO;
import com.app.dto.ProductRequestDTO;
import com.app.dto.ProductResponseDTO;
import com.app.entity.Product;
import com.app.entity.ProductImage;
import com.app.enumeration.ImageType;
import com.app.enumeration.RequestType;
import com.app.repository.ProductImageRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.FileService;
import com.app.service.MessagePropertyService;
import com.app.service.ProductService;
import com.app.util.message.ResponseMessage;
import com.app.validator.ProductValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/product")
@Api(value = "Product Rest API", produces = "application/json", consumes = "application/json")
public class ProductController {

	private static final Logger logger = Logger.getLogger(ProductController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull ProductService productService;

	private @NonNull ProductImageRepository imageRepository;

	private @NonNull ProductValidator validatorService;

	private @NonNull FileService fileService;

	@Autowired
	private @NonNull ProductImageRepository  productImageRepository;

	@Autowired
	MessagePropertyService messageSource;
	
	@ApiOperation(value = "Allows to create new product.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The Product type request payload") @RequestBody Product request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
		productService.saveOrUpdate((Product) validationResult.getObject());

		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("product.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing product.", response = Response.class)
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The Product type request payload") @RequestBody Product request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		productService.saveOrUpdate((Product) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("product.update"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	
	@ApiOperation(value = "Allows to fetch particular product.", response = Response.class)
	@GetMapping(value = "/get/{productId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("productId") UUID productId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			Optional<Product> product = productService.findById(productId);
			if (!product.isPresent()) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
						HttpStatus.BAD_REQUEST);
			}
//			Product productObj = product.get();
//			productObj.setProductImageList(imageRepository.findByProductObj(productObj));
			ProductResponseDTO productresponseObj = productService.getById(productId);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.get"), productresponseObj,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	public static void main(String[] args) throws IOException {

		File file = new File(
				"C:\\Users\\ulaga\\2020Works\\UPWORK\\HASTIN-CONTAINER\\NOTIFICATIONS\\hastin-logo-tp.png");

		byte[] content = Files.readAllBytes(file.toPath());

		System.out.println(Base64.getEncoder().encodeToString(content));
		;

	}


	@ApiOperation(value = "Allows to fetch E-Queue document dwonloads.", response = Response.class)
	@GetMapping(value = "/download/{studentId}", produces = "application/json")
	public ResponseEntity<?> download(@PathVariable("studentId") UUID studentId, @RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		Optional<Product> product = productService.findById(studentId);
		if (!product.isPresent()) {
			return responseGenerator.errorResponse(context, ResponseMessage.INVALID_REQUEST_FORMAT,
					HttpStatus.BAD_REQUEST);
		}

		List<ProductImage> productImageList = productImageRepository.findByProductObj(product);
		// for(ProductImage obj:productImageList) {
		byte[] data = fileService.downloadImage(ImageType.PRODUCT, studentId, productImageList.get(0).getFileName());

		String fileNameText = productImageList.get(0).getFileName();
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
	@ApiOperation(value = "Allows to fetch all products.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<Product> productList = productService.findAll();

			Comparator<Product> compareByName = (Product o1, Product o2) -> o1.getProductName()
					.compareToIgnoreCase(o2.getProductName());

			Collections.sort(productList, compareByName);
			List<ProductResponseDTO> productsList = productService.getAll();
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.get"), productsList,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}


	@RequestMapping(value = "/uomdelete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deletedByUOMId(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			productService.deleteUomId(id);
			
			return responseGenerator.successResponse(context, messageSource.getMessage("productDtl.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/imagedelete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deletedByImageId(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			productService.deleteImageId(id);
			
			return responseGenerator.successResponse(context, messageSource.getMessage("productImage.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
