package com.app.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AllProductDTO;
import com.app.dto.BrandDTO;
import com.app.dto.FilterProductDTO;
import com.app.dto.ProductCategoryInterface;
import com.app.dto.ProductDTO;
import com.app.dto.ProductRequestDTO;
import com.app.dto.ProductResponseDTO;
import com.app.dto.WebProductDTO;
import com.app.dto.WebProductListingDTO;
import com.app.dto.WebProductPriceDTO;
import com.app.entity.Brand;
import com.app.entity.CustomerReward;
import com.app.entity.CustomerRewardHistory;
import com.app.entity.Product;
import com.app.entity.ProductDtl;
import com.app.entity.ProductImage;
import com.app.entity.User;
import com.app.enumeration.ProductDiscountType;
import com.app.enumeration.ProductImageType;
import com.app.enumeration.RequestType;
import com.app.repository.ProductDtlRepository;
import com.app.repository.ProductImageRepository;
import com.app.repository.UserRepository;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.BrandService;
import com.app.service.CustomerRewardHistoryService;
import com.app.service.CustomerRewardService;

import com.app.service.MessagePropertyService;
import com.app.service.OfferDetailsService;
import com.app.service.ProductCategoryService;
import com.app.service.ProductService;
import com.app.util.file.AWSS3DirectoryUtil;
import com.app.validator.CustomerRewardHistoryValidator;
import com.app.validator.CustomerRewardValidator;
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
@RequestMapping("/api/web")
@Api(value = "Product Rest API", produces = "application/json", consumes = "application/json")
public class WebController {

	private static final Logger logger = Logger.getLogger(WebController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull ProductService productService;

	private @NonNull ProductService productDaoService;

	private @NonNull ProductCategoryService productCategoryService;

	private @NonNull ProductDtlRepository productDtlRepository;

	private @NonNull ProductImageRepository productImageRepository;

	private @NonNull ProductValidator validatorService;

	private @NonNull UserRepository userRepository;

	private @NonNull OfferDetailsService offerDetailsService;

	private @NonNull ProductImageRepository imageRepository;

	private @NonNull BrandService productBrandService;

	private @NonNull CustomerRewardService customerRewardService;

	private @NonNull CustomerRewardValidator customerRewardValidator;

	private @NonNull CustomerRewardHistoryValidator customerRewardHistoryValidator;

	private @NonNull CustomerRewardHistoryService customerRewardHistoryService;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to fetch all products.", response = Response.class)
	@GetMapping(value = "/products/get", produces = "application/json")
	public ResponseEntity<?> getAllProducts(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			response.put("listings", productService.findAllProducts());
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all products.", response = Response.class)
	@GetMapping(value = "/products/getall", produces = "application/json")
	public ResponseEntity<?> getAllProducts(@RequestHeader HttpHeaders httpHeader,
			@RequestParam(required = false) String customerInput) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			if (customerInput != null) {

				List<WebProductListingDTO> productListing = new ArrayList<WebProductListingDTO>();

				WebProductListingDTO responseObj = null;

				String discType = null;
				boolean discOption = true;

				List<Product> productList = productService.findByUserReq(customerInput);
				for (Product dtoObj : productList) {

					List<ProductDtl> dtlList = dtoObj.getProductDtlList();
					Comparator<ProductDtl> compareByName = (ProductDtl o1, ProductDtl o2) -> o1.getSalesRate()
							.compareTo(o2.getSalesRate());

					Collections.sort(dtlList, compareByName);

					ProductDtl dtlObj = dtlList.get(0);

					if (dtlObj.getDiscountType().equals(ProductDiscountType.FLAT)) {
						discType = "â‚¹";
					} else if (dtlObj.getDiscountType().equals(ProductDiscountType.PERCENTAGE)) {
						discType = "%";
					} else {
						discType = "";
						discOption = false;
					}

					List<ProductImage> imageList = imageRepository.findByProductObj(dtlObj.getProduct());
					ProductImage imageObj = imageList.get(0);

					responseObj = WebProductListingDTO.builder().productId(dtlObj.getProductId())
							.name(dtoObj.getProductName())
							.fileUrl(AWSS3DirectoryUtil
									.getProductImageUrl(dtlObj.getProduct().getId(), imageObj.getFileName()))
							.price(dtlObj.getSalesRate())
							.priceWithUom("â‚¹ " + dtlObj.getSalesRate() + " / "
									+ dtlObj.getUnitOfMeasurement().getShortName())
							.discOption(discOption).discAmount(discOption ? dtlObj.getDiscount() : 0.00)
							.discValue(discOption ? dtlObj.getDiscount() + " " + discType : "").build();
					productListing.add(responseObj);
				}

				response.put("listings", productListing);
			} else {
				response.put("listings", productService.findAllProducts());
			}
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch single product informations.", response = Response.class)
	@GetMapping(value = "/product/{productDtlId}", produces = "application/json")
	public ResponseEntity<?> viewProduct(@RequestHeader HttpHeaders httpHeader,
			@PathVariable("productDtlId") UUID productDtlId) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			WebProductListingDTO productObj = productService.getProductsByProductDtlId(productDtlId);
			response.put("product", productObj);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.details.get"),
					response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all product brands.", response = Response.class)
	@GetMapping(value = "/brands", produces = "application/json")
	public ResponseEntity<?> getAllBrands(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context,messageSource.getMessage("brand.get"), productBrandService.getAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all products by brand id.", response = Response.class)
	@GetMapping(value = "/brands/{brandId}", produces = "application/json")
	public ResponseEntity<?> get(@PathVariable("brandId") UUID brandId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<BrandDTO> brandObj = productBrandService.getBrandProdcuctsById(brandId);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("brand.get.one"), brandObj,
					HttpStatus.OK);
		} catch (Exception e) { 
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}

	}

	@ApiOperation(value = "Allows to fetch all product categories.", response = Response.class)
	@GetMapping(value = "/categories", produces = "application/json")
	public ResponseEntity<?> getAllCategories(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ProductCategoryInterface> productCategoryList = productCategoryService.getAllCategories();
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.categories.get"),
					productCategoryList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all productsbyCategory.", response = Response.class)
	@GetMapping(value = "/categories/{categoryId}", produces = "application/json")
	public ResponseEntity<?> getAllProductsByCategory(@PathVariable("categoryId") UUID categoryId,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<WebProductListingDTO> productCategoriesList = productService.findAllProductsByCategory(categoryId);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.categories.get.one"),
					productCategoriesList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all Top Rate products.", response = Response.class)
	@GetMapping(value = "/get/products", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<WebProductListingDTO> productList = productService.findTopRatedProducts();

			return responseGenerator.successResponse(context, productList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all Offer details.", response = Response.class)
	@GetMapping(value = "/get/active/offers", produces = "application/json")
	public ResponseEntity<?> getActiveAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("offer.details.active.get"),
					offerDetailsService.getActiveOffers(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to search all products.", response = Response.class)
	@PutMapping(value = "/product/search", produces = "application/json")
	public ResponseEntity<?> getSearchProduct(
			@ApiParam(value = "The Product search request payload") @RequestBody ProductRequestDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			List<AllProductDTO> responseList = productService.findByProductSearch(request);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.get"), responseList,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch filter products.", response = Response.class)
	@PutMapping(value = "/product/item/filter/counts/get", produces = "application/json")
	public ResponseEntity<?> getItemCountProduct(
			@ApiParam(value = "The Product filter request payload") @RequestBody ProductRequestDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			FilterProductDTO productFilterList = productService.findByFilterProduct(request);
			return responseGenerator.successGetResponse(context, messageSource.getMessage("product.get"),
					productFilterList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create new customer reward.", response = Response.class)
	@PostMapping(value = "/reward/create", produces = "application/json")
	public ResponseEntity<?> createDeliveryAddress(
			@ApiParam(value = "The customer reward request payload") @RequestBody CustomerReward request,
			@RequestHeader HttpHeaders httpHeader, Principal principal) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {

			Optional<User> userOptional = userRepository.findByUserName(principal.getName());

			ValidationResult validationResult = customerRewardValidator.validate(RequestType.POST, request,
					userOptional.get());
			customerRewardService.saveOrUpdate((CustomerReward) validationResult.getObject());

			return responseGenerator.successResponse(context, messageSource.getMessage("reward.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to create new reward history.", response = Response.class)
	@PostMapping(value = "/reward/history/create", produces = "application/json")
	public ResponseEntity<?> create(
			@ApiParam(value = "The  reward history request payload") @RequestBody CustomerRewardHistory request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		ValidationResult validationResult = customerRewardHistoryValidator.validate(RequestType.POST, request);
		customerRewardHistoryService.saveOrUpdate((CustomerRewardHistory) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("reward.history.create"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/get/brands/test", produces = "application/json")
	public ResponseEntity<?> getAllBrandsForTesting(@RequestParam("searchKey") String searchKey,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("brand.get"),
					productBrandService.getAllBrand(searchKey), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
