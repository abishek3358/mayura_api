package com.app.util.file;

import java.util.UUID;


import com.app.util.ValidationUtil;

public class AWSS3DirectoryUtil {

	public static final String DIRECTORY_PRODUCT	= "products";
	
	public static final String DIRECTORY_CATEGORY	= "category";
	
	public static final String DIRECTORY_BRAND	= "brands";
	
	public static final String DIRECTORY_PROFILE	= "profile";
	
	public static final String S3_BASE_URL	= "https://equeue-staging.s3.ap-south-1.amazonaws.com/"; 
	
	public static final String BASE_URL = "https://ebraintechnologies.app/ecommerce/api/";
	

	
	public static String getProductImageUrl(UUID productId, String fileName) {
		if(ValidationUtil.isNullOrEmpty(fileName)) {
			
			return S3_BASE_URL+DIRECTORY_PRODUCT+"/default.jpg";
		}
		return S3_BASE_URL+DIRECTORY_PRODUCT+"/"+productId+"/"+fileName;
	}
	
	public static String getCategoryImageUrl(UUID categoryId, String fileName) {
		if(ValidationUtil.isNullOrEmpty(fileName)) {
			
			return S3_BASE_URL+DIRECTORY_CATEGORY+"/default.jpg";
		}
		return S3_BASE_URL+DIRECTORY_CATEGORY+"/"+categoryId+"/"+fileName;
	}
	
	
	public static String getBrandImageUrl(UUID brandId, String fileName) {
		if(ValidationUtil.isNullOrEmpty(fileName)) {
			
			return S3_BASE_URL+DIRECTORY_BRAND+"/default.jpg";
		}
		return S3_BASE_URL+DIRECTORY_BRAND+"/"+brandId+"/"+fileName;
	}	
	
	public static String getProfileImageUrl(UUID userId, String fileName) {
		if(ValidationUtil.isNullOrEmpty(fileName)) {
			
			return S3_BASE_URL+DIRECTORY_PROFILE+"/default.png";
		}
		return S3_BASE_URL+DIRECTORY_PROFILE+"/"+userId+"/"+fileName;
	}
	public static String getBaseUrl() {
		return BASE_URL;
	}
}
