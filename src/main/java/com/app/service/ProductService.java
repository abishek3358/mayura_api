package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.ProductDAO;
import com.app.dto.AllProductDTO;
import com.app.dto.FilterProductDTO;
import com.app.dto.ProductDTO;
import com.app.dto.ProductDtlDTO;
import com.app.dto.ProductImageDTO;
import com.app.dto.ProductRequestDTO;
import com.app.dto.ProductResponseDTO;
import com.app.dto.ProductViewDTO;
import com.app.dto.WebProductListingDTO;
import com.app.entity.Product;
import com.app.entity.ProductDtl;
import com.app.entity.ProductImage;
import com.app.enumeration.ImageType;
import com.app.enumeration.ProductDiscountType;
import com.app.repository.BrandRepository;
import com.app.repository.CustomerOrderItemsRepository;
import com.app.repository.ProductCategoryRepository;
import com.app.repository.ProductDtlRepository;
import com.app.repository.ProductImageRepository;
import com.app.repository.ProductRepository;
import com.app.util.ValidationUtil;
import com.app.util.file.AWSS3DirectoryUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProductService {

	private @NonNull ProductDAO dao;

	private @NonNull ProductRepository productRepository;

	private @NonNull ProductCategoryRepository productCategoryRepository;

	private @NonNull ProductCategoryService productCategoryService;

	private @NonNull BrandService BrandService;

	private @NonNull BrandRepository brandRepository;

	private @NonNull UnitOfMeasurementService unitOfMeasurementService;

	private @NonNull ProductDtlRepository productDtlRepository;

	private @NonNull ProductImageRepository productImageRepository;

	private @NonNull CustomerOrderItemsRepository customerOrderItemsRepository;

	private @NonNull FileService fileUploadService;

	@Transactional
	public void saveOrUpdate(Product obj) throws Exception {

		productRepository.saveAndFlush(obj);

		if (null != obj.getProductDtlList() && !obj.getProductDtlList().isEmpty()) {
	        for (ProductDtl productDtl : obj.getProductDtlList()) {
	            if (productDtl.getId() != null) {
	                // If the ProductDtl has an ID, it already exists, so update it
	                ProductDtl existingProductDtl = productDtlRepository.findById(productDtl.getId()).orElse(null);
	                if (existingProductDtl != null) {
	                    existingProductDtl.setQuantity(productDtl.getQuantity());
	                    existingProductDtl.setThresholdQuantity(productDtl.getThresholdQuantity());
	                    // Update other fields as needed
	                    productDtlRepository.saveAndFlush(existingProductDtl);
	                }
	            }
	        }
	    }

		if (null != obj.getProductImageList() && !obj.getProductImageList().isEmpty()) {

			for (ProductImage productImage : obj.getProductImageList()) {

				productImage.setProductId(obj.getId());
				productImage.setProductObj(obj);
				if (!ValidationUtil.isNullOrEmpty(productImage.getFileBase64())) {
					productImage
							.setFileUrl(AWSS3DirectoryUtil.getProductImageUrl(obj.getId(), productImage.getFileName()));
					fileUploadService.uploadImage(ImageType.PRODUCT, obj.getId(), productImage.getFileName(),
							productImage.getFileBase64());

				}
				productImageRepository.saveAndFlush(productImage);
			}
		}

		if (obj.getId() != null) {
			String tagName = obj.getTag();
			String[] tags = null;
			tags = tagName.split(",");
			List<String> tagslist = new ArrayList<>();
			if (tags != null) {
				for (String tagname : tags) {
					tagslist.add(tagname);
				}
			}
		}
	}

	public Optional<Product> findById(UUID id) {
		return productRepository.findById(id);
	}

	public ProductResponseDTO getById(UUID id) {
		Optional<Product> productOptional = productRepository.findById(id);
		Product productObj = productOptional.get();
		ProductResponseDTO productDtoObj = new ProductResponseDTO();
		productDtoObj.setId(productObj.getId());
		productDtoObj.setProductCode(productObj.getProductCode());
		productDtoObj.setProductName(productObj.getProductName());
		productDtoObj.setProductCategoryId(productObj.getProductCategoryId());
		productDtoObj.setProductCategoryName(
				productCategoryService.findById(productObj.getProductCategoryId()).get().getName());
		productDtoObj.setProductValidityInDays(productObj.getProductValidityInDays());
		productDtoObj.setBrandId(productObj.getBrandId());
		productDtoObj.setHsCode(productObj.getHsCode());
		productDtoObj.setBrandName(BrandService.findById(productObj.getBrandId()).get().getBrandName());
		productDtoObj.setProductValidityInDays(productObj.getProductValidityInDays());
		String tagName = productObj.getTag();
		String[] tags = null;
		tags = tagName.split(",");
		List<String> tagslist = new ArrayList<>();
		if (tags != null) {
			for (String tagname : tags) {
				tagslist.add(tagname);
			}
		}

		productDtoObj.setTagsList(tagslist);
		productDtoObj.setTag(productObj.getTag());
		productDtoObj.setDescription(productObj.getDescription());
		productDtoObj.setDetailedDescription(productObj.getDetailedDescription());
		List<ProductDtl> productDtlDtoList = productDtlRepository.findByProduct(productObj);
		List<ProductDtlDTO> productDtlList = new ArrayList<ProductDtlDTO>();
		ProductDtlDTO productDtlDtoObj = null;
		for (ProductDtl productDtlObjList : productDtlDtoList) {
			productDtlDtoObj = new ProductDtlDTO();
			productDtlDtoObj.setId(productDtlObjList.getId());
			productDtlDtoObj.setProductId(productDtlObjList.getProductId());
			productDtlDtoObj.setUomId(productDtlObjList.getUomId());
			productDtlDtoObj.setQuantity(productDtlObjList.getQuantity());
			productDtlDtoObj
					.setUomName(unitOfMeasurementService.findById(productDtlObjList.getUomId()).get().getName());
			productDtlDtoObj.setThresholdQuantity(productDtlObjList.getThresholdQuantity());
			productDtlDtoObj.setDiscount(productDtlObjList.getDiscount());
			productDtlDtoObj.setDiscountType(productDtlObjList.getDiscountType());
			productDtlDtoObj.setPurchaseRate(productDtlObjList.getPurchaseRate());
			productDtlDtoObj.setSalesRate(productDtlObjList.getSalesRate());
			productDtlList.add(productDtlDtoObj);
			List<ProductImage> productImageList = productImageRepository.findByProductObj(productObj);
			List<ProductImageDTO> productImageDtoList = new ArrayList<ProductImageDTO>();
			ProductImageDTO productImageDtoObj = null;
			for (ProductImage productImageObj : productImageList) {
				productImageDtoObj = new ProductImageDTO();
				productImageDtoObj.setId(productImageObj.getId());
				productImageDtoObj.setProductId(productObj.getId());
				productImageDtoObj.setProductImageType(productImageObj.getProductImageType());
				productImageDtoObj.setFileName(productImageObj.getFileName());
				productImageDtoObj.setFileUrl(
						AWSS3DirectoryUtil.getProductImageUrl(productObj.getId(), productImageObj.getFileName()));
				productImageDtoList.add(productImageDtoObj);
			}
			productDtoObj.setProductImageList(productImageDtoList);
		}
		productDtoObj.setProductDtlList(productDtlList);

		return productDtoObj;
	}

	public List<Product> findAll() {
		return productRepository.findAll();

	}

	public List<ProductResponseDTO> getAll() {
		List<Product> productList = productRepository.findAll();
		List<ProductResponseDTO> productDtoList = new ArrayList<ProductResponseDTO>();
		ProductResponseDTO productDtoObj = null;
		for (Product productObj : productList) {
			productDtoObj = new ProductResponseDTO();
			productDtoObj.setId(productObj.getId());
			productDtoObj.setProductName(productObj.getProductName());
			productDtoObj.setProductCode(productObj.getProductCode());
			productDtoObj.setHsCode(productObj.getHsCode());
			productDtoObj.setProductCategoryId(productObj.getProductCategoryId());
			productDtoObj.setProductCategoryName(
					productCategoryService.findById(productObj.getProductCategoryId()).get().getName());
			productDtoObj.setBrandId(productObj.getBrandId());
			productDtoObj.setBrandName(BrandService.findById(productObj.getBrandId()).get().getBrandName());
			productDtoObj.setProductValidityInDays(productObj.getProductValidityInDays());

			String tagName = productObj.getTag();

			// productDtoObj.setBrandName(BrandService.findById(productObj.getBrandId()).get().getBrandName());
			// productDtoObj.setProductValidityInDays(productObj.getProductValidityInDays());

			String[] tags = null;
			tags = tagName.split(",");
			List<String> tagslist = new ArrayList<>();
			if (tags != null) {
				for (String tagname : tags) {
					tagslist.add(tagname);
				}
			}

			productDtoObj.setTagsList(tagslist);
			productDtoObj.setTag(productObj.getTag());
			productDtoObj.setDescription(productObj.getDescription());
			productDtoObj.setDetailedDescription(productObj.getDetailedDescription());
			productDtoList.add(productDtoObj);

			List<ProductDtl> productDtlList = productDtlRepository.findByProduct(productObj);
			List<ProductDtlDTO> productDtlDtoList = new ArrayList<ProductDtlDTO>();
			ProductDtlDTO productDtlDtoObj = null;
			for (ProductDtl productDtlObj : productDtlList) {
				productDtlDtoObj = new ProductDtlDTO();
				productDtlDtoObj.setId(productDtlObj.getId());
				productDtlDtoObj.setProductId(productObj.getId());
				productDtlDtoObj.setUomId(productDtlObj.getUomId());
				productDtlDtoObj.setQuantity(productDtlObj.getQuantity());
				productDtlDtoObj
						.setUomName(unitOfMeasurementService.findById(productDtlObj.getUomId()).get().getName());
				productDtlDtoObj.setThresholdQuantity(productDtlObj.getThresholdQuantity());
				productDtlDtoObj.setDiscount(productDtlObj.getDiscount());
				productDtlDtoObj.setDiscountType(productDtlObj.getDiscountType());
				productDtlDtoObj.setPurchaseRate(productDtlObj.getPurchaseRate());
				productDtlDtoObj.setSalesRate(productDtlObj.getSalesRate());
				productDtlDtoList.add(productDtlDtoObj);

				List<ProductImage> productImageList = productImageRepository.findByProductObj(productObj);
				List<ProductImageDTO> productImageDtoList = new ArrayList<ProductImageDTO>();
				ProductImageDTO productImageDtoObj = null;
				for (ProductImage productImageObj : productImageList) {
					productImageDtoObj = new ProductImageDTO();
					productImageDtoObj.setId(productImageObj.getId());
					productImageDtoObj.setProductId(productObj.getId());
					productImageDtoObj.setProductImageType(productImageObj.getProductImageType());
					productImageDtoObj.setFileUrl(
							AWSS3DirectoryUtil.getProductImageUrl(productObj.getId(), productImageObj.getFileName()));
					productImageDtoObj.setFileName(productImageObj.getFileName());
					productImageDtoList.add(productImageDtoObj);
				}
				productDtoObj.setProductImageList(productImageDtoList);
			}
			productDtoObj.setProductDtlList(productDtlDtoList);
		}
		return productDtoList;
	}

	public Optional<Product> findByProductName(String name) {
		return productRepository.findByProductName(name);
	}

	public Optional<Product> findByProductCode(String code) {
		return productRepository.findByProductCode(code);
	}

	public Optional<Product> findByProductNameExcludeId(String product_name, UUID excludeId) {
		return productRepository.findByProductNameExcludeId(product_name, excludeId);
	}

	public Optional<Product> findByProductCodeExcludeId(String productCode, UUID excludeId) {
		return productRepository.findByProductCodeExcludeId(productCode, excludeId);
	}

	public List<WebProductListingDTO> findAllProducts() {
		List<WebProductListingDTO> productListing = new ArrayList<WebProductListingDTO>();
		WebProductListingDTO responseObj = null;
		for (ProductDTO dtoObj : productRepository.findAllProducts()) {
			String discType = null;
			boolean discOption = true;
			if (dtoObj.getProductDiscType().equals(ProductDiscountType.FLAT)) {
				discType = "₹";
			} else if (dtoObj.getProductDiscType().equals(ProductDiscountType.PERCENTAGE)) {
				discType = "%";
			} else {
				discType = "";
				discOption = false;
			}
			responseObj = WebProductListingDTO.builder().productId(dtoObj.getId()).name(dtoObj.getProductName())
					.fileName(dtoObj.getFileName())
					.fileUrl(AWSS3DirectoryUtil.getProductImageUrl(dtoObj.getProductId(), dtoObj.getFileName()))
					.price(dtoObj.getSalesRate())
					.priceWithUom("₹ " + dtoObj.getSalesRate() + " / " + dtoObj.getUomName()).discOption(discOption)
					.discAmount(discOption ? dtoObj.getProductDiscount() : 0.00)
					.discValue(discOption ? dtoObj.getProductDiscount() + " " + discType : "")
					.favorite(dtoObj.getfavorite().toString()).build();

			productListing.add(responseObj);
		}
		return productListing;
	}

	public List<WebProductListingDTO> findAllProductsByCategory(UUID categoryId) {
		List<WebProductListingDTO> productListing = new ArrayList<WebProductListingDTO>();
		WebProductListingDTO responseObj = null;
		for (ProductDTO dtoObj : productRepository.findAllProductsByCategoryId(categoryId.toString())) {
			String discType = null;
			boolean discOption = true;
			if (dtoObj.getProductDiscType().equals(ProductDiscountType.FLAT)) {
				discType = "₹";
			} else if (dtoObj.getProductDiscType().equals(ProductDiscountType.PERCENTAGE)) {
				discType = "%";
			} else {
				discType = "";
				discOption = false;
			}
			responseObj = WebProductListingDTO.builder().id(dtoObj.getId()).productName(dtoObj.getProductName())
					.fileName(dtoObj.getFileName()).brandName(dtoObj.getBrandName())
					.productCode(dtoObj.getProductCode())
					.fileUrl(AWSS3DirectoryUtil.getProductImageUrl(dtoObj.getId(), dtoObj.getFileName()))
					.price(dtoObj.getSalesRate()).uomId(dtoObj.getUomId()).productDtlId(dtoObj.getProductDtlId())
					.noOfStars(dtoObj.getNoOfStars())
					.priceWithUom("₹" + dtoObj.getSalesRate() + " / " + dtoObj.getUomName()).discOption(discOption)
					.discAmount(discOption ? dtoObj.getProductDiscount() : 0.00)
					.discValue(discOption ? dtoObj.getProductDiscount() + " " + discType : "")
					.favorite(dtoObj.getfavorite().toString()).build();
			productListing.add(responseObj);
		}
		return productListing;
	}

	public List<Product> findByUserReq(String customerInput) {
		return productRepository.findByUserReq(customerInput.toString());
	}

	public List<WebProductListingDTO> findTopRatedProducts() {
		List<WebProductListingDTO> productListing = new ArrayList<WebProductListingDTO>();
		WebProductListingDTO responseObj = null;
		for (ProductViewDTO dtoObj : productRepository.findTopRatedProducts()) {
			String discType = null;
			boolean discOption = true;
			if (dtoObj.getDiscType().equals(ProductDiscountType.FLAT)) {
				discType = "₹";
			} else if (dtoObj.getDiscType().equals(ProductDiscountType.PERCENTAGE)) {
				discType = "%";
			} else {
				discType = "";
				discOption = false;
			}
			responseObj = WebProductListingDTO.builder().id(dtoObj.getId()).productName(dtoObj.getProductName())
					.productDtlId(dtoObj.getProductDtlId()).uomId(dtoObj.getUomId()).fileName(dtoObj.getFileName())
					.brandName(dtoObj.getBrandName()).noOfStars(dtoObj.getNoOfStars())
					.fileUrl(AWSS3DirectoryUtil.getProductImageUrl(dtoObj.getId(), dtoObj.getFileName()))
					.price(dtoObj.getPrice()).priceWithUom("₹ " + dtoObj.getPrice() + " / " + dtoObj.getUomName())
					.discOption(discOption).discAmount(discOption ? dtoObj.getProductDiscount() : 0.00)
					.discValue(discOption ? dtoObj.getProductDiscount() + " " + discType : "")
					.favorite(dtoObj.getfavorite()).build();
			productListing.add(responseObj);
		}
		return productListing;
	}

	public List<AllProductDTO> findByProductSearch(ProductRequestDTO request) {
		return dao.findByProductSearch(request);
	}

	public FilterProductDTO findByFilterProduct(ProductRequestDTO request) {
		return dao.findByFilterProduct(request);
	}
//	public List<AllProductDTO> findByProductSearch(String searchKey, UUID categoryId, UUID brandId,
//			List<UUID> brandList, List<Double> ratingList, Double maxPrice, Double minPrice, List<UUID> categoryList,
//			Integer showItemCount, SortingType sortingType) {
//		return dao.findByProductSearch(searchKey, categoryId, brandId, brandList, ratingList, maxPrice, minPrice,
//				categoryList, showItemCount, sortingType);
//	}
//
//	public FilterProductDTO findByFilterProduct(String searchKey, UUID categoryId, UUID brandId, List<UUID> brandList,
//			List<Double> ratingList, Double maxPrice, Double minPrice, List<UUID> categoryList, Integer showItemCount,
//			SortingType sortingType) {
//		return dao.findByFilterProduct(searchKey, categoryId, brandId, brandList, ratingList, maxPrice, minPrice,
//				categoryList, showItemCount, sortingType);
//	}

	public WebProductListingDTO getProductsByProductDtlId(UUID productDtlId) {
		Optional<ProductDTO> productOptional = productRepository.findProductsByProductDtlId(productDtlId.toString());
		ProductDTO productObj = productOptional.get();
		WebProductListingDTO response = new WebProductListingDTO();
		response.setProductDtlId(productObj.getProductDtlId());
		response.setUomId(productObj.getUomId());
		response.setId(productObj.getId());
		response.setProductId(productObj.getProductId());
		response.setProductName(productObj.getProductName());
		response.setBrandName(productObj.getBrandName());
		String tagName = productObj.getTags();
		String[] tags = null;
		tags = tagName.split(",");
		List<String> tagslist = new ArrayList<>();
		if (tags != null) {
			for (String tagname : tags) {
				tagslist.add(tagname);
			}
		}
		response.setTagsList(tagslist);
		response.setTag(productObj.getTags());
		response.setNoOfStars(productObj.getNoOfStars());
		response.setProductCode(productObj.getProductCode());
		response.setPrice(productObj.getSalesRate());
		response.setPriceWithUom(("₹ " + productObj.getSalesRate() + " / " + productObj.getUomName()));
		response.setFileName(productObj.getFileName());
		response.setFileUrl(AWSS3DirectoryUtil.getProductImageUrl(productObj.getId(), productObj.getFileName()));
		response.setDescription(productObj.getDescription());
		response.setDetailedDescription(productObj.getDetailedDescription());
		response.setDiscAmount(productObj.getProductDiscount());

		List<ProductImageInterface> productImageList = productImageRepository
				.findByProductObj((productOptional.get().getProductId()).toString());
		List<ProductImageDTO> productImageDtoList = new ArrayList<ProductImageDTO>();
		ProductImageDTO productImageDtoObj = null;
		for (ProductImageInterface productImageObj : productImageList) {
			productImageDtoObj = new ProductImageDTO();
			productImageDtoObj.setId(productImageObj.getId());
			productImageDtoObj.setProductId(productObj.getProductId());
			productImageDtoObj.setProductImageType(productImageObj.getProductImageType());
			productImageDtoObj.setFileName(productImageObj.getFileName());
			productImageDtoObj.setFileUrl(AWSS3DirectoryUtil.getProductImageUrl(productImageDtoObj.getProductId(),
					productImageObj.getFileName()));
			productImageDtoList.add(productImageDtoObj);

		}
		response.setProductImageList(productImageDtoList);

		return response;
	}

	public void deleteUomId(UUID id) {
		
		Optional<ProductDtl> productDtlOptional = productDtlRepository.findById(id);

        if (productDtlOptional.isPresent()) {
            ProductDtl productDtl = productDtlOptional.get();

            // Disassociate the child association from the parent Product
            Product product = productDtl.getProduct();
            if (product != null) {
                product.getProductDtlList().remove(productDtl);
                productDtl.setProduct(null); // Disassociate the child from the parent
            }

            // Delete the ProductDtl entity
            productDtlRepository.delete(productDtl);
	}

	}

	public void deleteImageId(UUID id) {
		Optional<ProductImage> productImageOptional = productImageRepository.findById(id);

        if (productImageOptional.isPresent()) {
        	ProductImage productImage= productImageOptional.get();

            // Disassociate the child association from the parent Product
            Product product = productImage.getProductObj();
            if (product != null) {
                product.getProductDtlList().remove(productImage);
                productImage.setProductObj(null); // Disassociate the child from the parent
            }

            // Delete the ProductDtl entity
            productImageRepository.delete(productImage);
	}
	}
}
