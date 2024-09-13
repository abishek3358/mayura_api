package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.BrandDAO;
import com.app.dao.ProductBrandDAO;
import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.BrandDTO;
import com.app.dto.ProductDTO;
import com.app.dto.WebProductListingDTO;
import com.app.entity.Brand;
import com.app.enumeration.ImageType;
import com.app.enumeration.ProductDiscountType;
import com.app.jpa.interfaces.ProductBrandInterface;
import com.app.repository.BrandRepository;
import com.app.util.ValidationUtil;
import com.app.util.file.AWSS3DirectoryUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class BrandService {

	private @NonNull BrandRepository brandRepository;

	private @NonNull FileService fileUploadService;
	
	private @NonNull ProductBrandDAO brandDAO;
	private @NonNull BrandDAO DAOBrand ;

	@Transactional
	public void saveOrUpdate(Brand obj) throws Exception {
		brandRepository.saveAndFlush(obj);
		if (!ValidationUtil.isNullOrEmpty(obj.getFileBase64())) {
			obj.setFileUrl(AWSS3DirectoryUtil.getBrandImageUrl(obj.getId(), obj.getFileName()));
			fileUploadService.uploadImage(ImageType.BRAND, obj.getId(), obj.getFileName(), obj.getFileBase64());
		}
	}

	public BrandDTO getById(UUID id) {
		Optional<Brand> brandOptional = brandRepository.findById(id);
		Brand brandObj = brandOptional.get();
		BrandDTO response = new BrandDTO();
		response.setId(brandObj.getId());
		response.setBrandName(brandObj.getBrandName());
		response.setFileName(brandObj.getFileName());
		response.setProductCount(brandObj.getProductCount());
		response.setFileUrl(AWSS3DirectoryUtil.getBrandImageUrl(brandObj.getId(), brandObj.getFileName()));
		response.setDescription(brandObj.getDescription());
		return response;
	}

	public void deleteById(UUID id) {
		brandRepository.deleteById(id);
	}

	public Optional<Brand> findByBrandName(String brandName) {

		return brandRepository.findByBrandName(brandName);
	}

	public Optional<Brand> findById(UUID brandId) {
		return brandRepository.findById(brandId);
	}


	public List<BrandDTO> getAllBrands() {
		List<BrandDTO> list = new ArrayList<BrandDTO>();
		BrandDTO responseObj = null;
		for (ProductBrandInterface dtoBrandObj : brandRepository.findAllBrands()) {
			responseObj = new BrandDTO();
			responseObj.setId(dtoBrandObj.getId());
			responseObj.setBrandName(dtoBrandObj.getBrandName());
			responseObj.setProductCount(dtoBrandObj.getProductCount());
			responseObj.setFileUrl(AWSS3DirectoryUtil.getBrandImageUrl(dtoBrandObj.getId(), dtoBrandObj.getFileName()));
			responseObj.setFileName(dtoBrandObj.getFileName());
			responseObj.setProductCount(dtoBrandObj.getProductCount());
			list.add(responseObj);
		}
		return list;
	}
	
	
	public List<Brand> getAllBrand(String searchKey){
		return brandDAO.getAllBrand(searchKey);
	}

	public List<BrandDTO> getBrandProdcuctsById(UUID brandId) {
		return brandDAO.getBrandProdcuctsById(brandId.toString());
//		List<BrandDTO> productListing = new ArrayList<BrandDTO>();
//		BrandDTO responseObj = null;
//		for (ProductBrandInterface dtoObj : brandRepository.getBrandProductsById(brandId.toString())) {			
//			responseObj = BrandDTO.builder()
//					.id(dtoObj.getId()).productName(dtoObj.getProductName())
//					.brandName(dtoObj.getBrandName())
//					.productId(dtoObj.getProductId())
//					.fileName(dtoObj.getFileName())	
//					.noOfStars(dtoObj.getNoOfStars())
//					.fileUrl(AWSS3DirectoryUtil.getProductImageUrl(dtoObj.getProductId(), dtoObj.getFileName()))
//					.salesRate(dtoObj.getSalesRate())
//					.priceWithUom("₹" + dtoObj.getSalesRate() + " / " + dtoObj.getUomName())
//					.build();				
//					
//			productListing.add(responseObj);
//		}
//		return productListing;
	}

public void updateBrand(Brand brand) {
		
		brandRepository.saveAndFlush(brand);
	}

	public Brand findByNameId(String brandName, UUID id) {
		
		return DAOBrand.findByNameId(brandName,id);
	}
	
	public List<BrandDTO> getAll() {
		List<BrandDTO> list = new ArrayList<BrandDTO>();
		BrandDTO responseObj = null;
		for (ProductBrandInterface dtoObj : brandRepository.findAllBrand()) {
			responseObj = new BrandDTO();
			responseObj.setId(dtoObj.getId());
			responseObj.setBrandName(dtoObj.getBrandName());
			responseObj.setProductCount(dtoObj.getProductCount());
			responseObj.setDescription(dtoObj.getDescription());
			responseObj.setFileUrl(AWSS3DirectoryUtil.getBrandImageUrl(dtoObj.getId(), dtoObj.getFileName()));
			responseObj.setFileName(dtoObj.getFileName());
			list.add(responseObj);
		}
		return list;
	}
}
