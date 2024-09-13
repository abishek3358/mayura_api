package com.app.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.ProductCategoryDAO;
import com.app.dto.ProductCategoryDTO;
import com.app.dto.ProductCategoryInterface;
import com.app.dto.ProductCategoryListDTO;
import com.app.dto.WebProductCategoryListDto;
import com.app.entity.ProductCategory;
import com.app.enumeration.ImageType;

import com.app.repository.ProductCategoryRepository;
import com.app.util.ValidationUtil;
import com.app.util.file.AWSS3DirectoryUtil;
import com.twilio.rest.api.v2010.Account.Status;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ProductCategoryService {
	
	private @NonNull ProductCategoryDAO dao;
	
	private @NonNull ProductCategoryRepository productCategoryRepository;
	
	private @NonNull ProductTypeService productTypeService;
	
	private @NonNull FileService fileUploadService;
	
	@Transactional
	public void saveOrUpdate(ProductCategory obj) throws Exception { 
		productCategoryRepository.saveAndFlush(obj);
		
		if(!ValidationUtil.isNullOrEmpty(obj.getFileBase64())) {
			obj.setFileUrl(AWSS3DirectoryUtil.getCategoryImageUrl(obj.getId(),obj.getFileName()));
			fileUploadService.uploadImage(ImageType.CATEGORY,obj.getId(),obj.getFileName(),obj.getFileBase64());
			
			

		}
	}
	public Optional<ProductCategory> findById(UUID id) {
		return productCategoryRepository.findById(id);
	}
	
	public List<ProductCategory> findAll() {
		return productCategoryRepository.findAll();
	}
	
	public Optional<ProductCategory> findByName(String name) {
		return productCategoryRepository.findByName(name);
	}
	
	public Optional<ProductCategory> findByNameExcludeId(String name,UUID excludeId) {
		return productCategoryRepository.findByNameExcludeId(name,excludeId);
	}
	
	public List<ProductCategoryDTO> getAllCategorielst() {
		return dao.getAllCategories();
	}
	
	
	
	public List<WebProductCategoryListDto> findAllCategories() {		
		return productCategoryRepository.findAllCategories();
	}
	
	  

//	public List<ProductCategoryInterface> getAllCategories(){
//		//List<ProductCategoryListDTO> productCategoryList=productCategoryRepository.findAllCategory();
//		List<ProductCategoryInterface> productListing = new ArrayList<ProductCategoryInterface>();
//		ProductCategoryInterface responseObj = null;
//		for (ProductCategoryListDTO dtoObj : productCategoryRepository.findAllCategory()) {
//			responseObj=new ProductCategoryInterface();
//			responseObj.setId(dtoObj.getCategoryId());
//			responseObj.setCategoryName(dtoObj.getCategoryName());
//			responseObj.setImageFileUrl(AWSS3DirectoryUtil.getCategoryImageUrl(dtoObj.getCategoryId(),dtoObj.getFileName()));
//			responseObj.setFileName(dtoObj.getFileName());
//			responseObj.setProductCount(dtoObj.getProductCount());					
//			productListing.add(responseObj);
//		}
//		return productListing;
//	}
	
	public List<ProductCategoryInterface> getAllCategories(){	
		List<ProductCategoryInterface> productListing = new ArrayList<ProductCategoryInterface>();
		ProductCategoryInterface responseObj = null;
		for (ProductCategoryListDTO dtoObj : productCategoryRepository.findAllCategoriesList()) {
			responseObj=new ProductCategoryInterface();
			responseObj.setId(dtoObj.getCategoryId());
			responseObj.setCategoryName(dtoObj.getCategoryName());
			responseObj.setCategoryId(dtoObj.getCategoryId());
			responseObj.setImageFileUrl(AWSS3DirectoryUtil.getCategoryImageUrl(dtoObj.getCategoryId(),dtoObj.getFileName()));
			responseObj.setFileName(dtoObj.getFileName());
			responseObj.setProductCount(dtoObj.getProductCount());
			responseObj.setStatus(dtoObj.getStatus());
			productListing.add(responseObj);
		}
		return productListing;
	}
	public List<ProductCategoryListDTO> findAllCategoriesList() {		
		return productCategoryRepository.findAllCategoriesList();
	}
	public List<ProductCategory> findByStatus(Status active) {
		
		return productCategoryRepository.findByStatus(active);
	}
	
}
