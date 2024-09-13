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
import com.app.entity.Product;
import com.app.entity.ProductCategory;
import com.app.entity.ProductDtl;
import com.app.entity.ProductImage;
import com.app.entity.UnitOfMeasurement;
import com.app.enumeration.ProductDiscountType;
import com.app.enumeration.RequestType;
import com.app.repository.ProductDtlRepository;
import com.app.repository.ProductImageRepository;
import com.app.service.MessagePropertyService;
import com.app.service.ProductCategoryService;
import com.app.service.ProductService;
import com.app.service.UnitOfMeasurementService;
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
public class ProductValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull ProductCategoryService productCategoryService;

	private @NonNull ProductService productService;

	private @NonNull ProductDtlRepository productDltRepository;

	private @NonNull ProductImageRepository productImageRepository;

	private @NonNull UnitOfMeasurementService uomService;

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
	public ValidationResult validate(RequestType requestType, Product request) {

		errors = new ArrayList<>();
		Product product = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<Product> productDuplicateObj = productService.findByProductName(request.getProductName());
			if (productDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getProductName() };
				errors.add(messageSource.getMessage("product.name.duplicate", params));
			}

			productDuplicateObj = productService.findByProductCode(request.getProductCode());
			if (productDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getProductCode() };
				errors.add(messageSource.getMessage("product.code.duplicate", params));
			}
		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

			Optional<Product> productOptional = productService.findById(request.getId());
			if (!productOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("product.not.found"));
			}

			Optional<Product> productDuplicateObj = productService.findByProductNameExcludeId(request.getProductName(),
					request.getId());
			if (productDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getProductName() };
				errors.add(messageSource.getMessage("product.name.duplicate", params));
			}

			productDuplicateObj = productService.findByProductCodeExcludeId(request.getProductName(), request.getId());
			if (productDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getProductCode() };
				errors.add(messageSource.getMessage("product.code.duplicate", params));
			}

			product = productOptional.get();
		}

		if (ValidationUtil.isNullOrEmpty(request.getProductName())) {
			errors.add(messageSource.getMessage("product.name.required"));
		} else {
			request.setProductName(ValidationUtil.getFormattedString(request.getProductName()));
		}

		if (ValidationUtil.isNullOrEmpty(request.getProductCode())) {
			errors.add(messageSource.getMessage("product.code.required"));
		} else {
			request.setProductCode(ValidationUtil.getFormattedString(request.getProductCode()));
		}
		

		if (ValidationUtil.isNullOrEmpty(request.getHsCode())) {
			errors.add(messageSource.getMessage("product.hscode.required"));
		} else {
			request.setHsCode(ValidationUtil.getFormattedString(request.getHsCode()));
		}

		if (ValidationUtil.isNull(request.getProductValidityInDays())) {
			errors.add(messageSource.getMessage("product.validity.required"));
		}

		Optional<ProductCategory> productCategory = null;
		if (ValidationUtil.isNull(request.getProductCategoryId())) {
			errors.add(messageSource.getMessage("product.catagory.required"));
		} else {
			productCategory = productCategoryService.findById(request.getProductCategoryId());
			if (!productCategory.isPresent()) {
				errors.add(messageSource.getMessage("product.catagory.not.found"));
			}
		}

		String tagLists = "";
		if (null != request.getTagsList() && !request.getTagsList().isEmpty()) {
			for (String tags : request.getTagsList()) {
				tagLists = tags + "," + tagLists;
			}
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		List<ProductDtl> productDtlList = validateProductDtlList(request, errors);

		List<ProductImage> productImageList = validateProductImageList(request, errors);

		if (null == product) {

			product = Product.builder().tag(tagLists).productName(request.getProductName())
					.productCategoryId(request.getProductCategoryId()).hsCode(request.getHsCode())
					.brandId(request.getBrandId()).description(request.getDescription())
					.detailedDescription(request.getDetailedDescription()).productCode(request.getProductCode())
					.productValidityInDays(request.getProductValidityInDays()).build();
		} else {
			product.setHsCode(request.getHsCode());
			product.setProductValidityInDays(request.getProductValidityInDays());
			product.setProductCategoryId(request.getProductCategoryId());
			product.setProductName(request.getProductName());
			product.setTagsList(request.getTagsList());
			product.setBrandId(request.getBrandId());
			product.setDescription(request.getDescription());
			product.setDetailedDescription(request.getDetailedDescription());
			product.setProductCode(request.getProductCode());
			product.setStatus(request.getStatus());

		}
		for (ProductDtl productDtl : productDtlList) {
			productDtl.setProduct(product);
		}
		product.setProductDtlList(productDtlList);

		if (null != productImageList && !productImageList.isEmpty()) {
			for (ProductImage productImage : productImageList) {
				productImage.setProductId(request.getId());
			}
			product.setProductImageList(productImageList);
		}

		result.setObject(product);
		return result;
	}

	public List<ProductDtl> validateProductDtlList(Product request, List<String> errors) {

		List<ProductDtl> productDtlList = new ArrayList<ProductDtl>();

		for (ProductDtl productDtlReqObj : request.getProductDtlList()) {

			ProductDtl productDtl = null;
			if (!ValidationUtil.isNull(productDtlReqObj.getId())) {
				Optional<ProductDtl> productOptional = productDltRepository.findById(productDtlReqObj.getId());
				productDtl = productOptional.get();
			}

			Optional<UnitOfMeasurement> unitOfMeasurement = null;
			if (ValidationUtil.isNull(productDtlReqObj.getUomId())) {
				errors.add(messageSource.getMessage("uom.not.found"));
			} else {
				unitOfMeasurement = uomService.findById(productDtlReqObj.getUomId());
				if (!unitOfMeasurement.isPresent()) {
					errors.add(messageSource.getMessage("uom.not.found"));
				}
			}

			if (ValidationUtil.isNull(productDtlReqObj.getQuantity())) {
				errors.add(messageSource.getMessage("product.quantity.required"));
			}

			if (ValidationUtil.isNull(productDtlReqObj.getThresholdQuantity())) {
				errors.add(messageSource.getMessage("product.threshold.quantity.required"));
			}

			if (ValidationUtil.isNull(productDtlReqObj.getPurchaseRate())) {
				errors.add(messageSource.getMessage("product.purchase.rate.required"));
			}

			if (ValidationUtil.isNull(productDtlReqObj.getSalesRate())) {
				errors.add(messageSource.getMessage("product.sales.rate.required"));
			}

			if (ValidationUtil.isNullObject(productDtlReqObj.getDiscountType())) {
				errors.add(messageSource.getMessage("product.disc.type.required"));
			} else {

				if (!productDtlReqObj.getDiscountType().equals(ProductDiscountType.NONE)) {

					if (ValidationUtil.isNull(productDtlReqObj.getDiscount())) {
						errors.add(messageSource.getMessage("product.disc.required"));
					}
				} else {
					productDtlReqObj.setDiscount(0.00);
				}

			}

			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}

			if (null == productDtl) {
				productDtl = ProductDtl.builder().unitOfMeasurement(unitOfMeasurement.get())
						.quantity(productDtlReqObj.getQuantity())
						.thresholdQuantity(productDtlReqObj.getThresholdQuantity())
						.salesRate(productDtlReqObj.getSalesRate()).purchaseRate(productDtlReqObj.getPurchaseRate())
						.discountType(productDtlReqObj.getDiscountType()).discount(productDtlReqObj.getDiscount())
						.build();
			} else {
				productDtl.setDiscountType(productDtlReqObj.getDiscountType());
				productDtl.setDiscount(productDtlReqObj.getDiscount());
				productDtl.setPurchaseRate(productDtlReqObj.getPurchaseRate());
				productDtl.setQuantity(productDtlReqObj.getQuantity());
				productDtl.setSalesRate(productDtlReqObj.getSalesRate());
				productDtl.setStatus(productDtlReqObj.getStatus());
				productDtl.setThresholdQuantity(productDtlReqObj.getThresholdQuantity());
				productDtl.setUnitOfMeasurement(unitOfMeasurement.get());
			}
			productDtlList.add(productDtl);
		}

		return productDtlList;
	}

	public List<ProductImage> validateProductImageList(Product request, List<String> errors) {

		List<ProductImage> productImageList = new ArrayList<ProductImage>();

		for (ProductImage productImageReqObj : request.getProductImageList()) {

			ProductImage productImage = null;
			if (!ValidationUtil.isNull(productImageReqObj.getId())) {
				Optional<ProductImage> productImageOptional = productImageRepository
						.findById(productImageReqObj.getId());
				productImage = productImageOptional.get();
			}

//		        if (ValidationUtil.isNullOrEmpty(productImageReqObj.getFileBase64())) {
//		            errors.add(messageSource.getMessage("product.image.required"));
//		        }  
			 if (ValidationUtil.isNullOrEmpty(productImageReqObj.getFileName())) {
		            errors.add(messageSource.getMessage("product.name.required"));
		        } 
		        else {
		        	if(!(productImageReqObj.getFileName().toLowerCase().endsWith(".jpg") || productImageReqObj.getFileName().toLowerCase().endsWith(".jpeg")
		        			 || productImageReqObj.getFileName().toLowerCase().endsWith("png")))
		        	{
		        		errors.add(messageSource.getMessage("product.filename.invalid"));
		        	}	        	
		            
		        }
			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}
		
			if (null == productImage) {
				productImage = ProductImage.builder()
						.fileUrl(AWSS3DirectoryUtil.getProductImageUrl(productImageReqObj.getId(), productImageReqObj.getFileName()))
						.fileBase64(productImageReqObj.getFileBase64())
						.fileName(productImageReqObj.getFileName())
				.productImageType(productImageReqObj.getProductImageType()).build();
			} else {
				productImage.setFileBase64(productImageReqObj.getFileBase64());
				productImage.setFileName(productImageReqObj.getFileName());
				productImage.setFileUrl(AWSS3DirectoryUtil.getProductImageUrl(productImageReqObj.getId(),
				 productImageReqObj.getFileName()));
			}
			productImageList.add(productImage);
		}

		return productImageList;
	}

}
