package com.app.dto;

import java.util.UUID;

import javax.persistence.Transient;

import com.app.enumeration.Status;

import groovy.transform.builder.Builder;
import lombok.Data;

public interface ProductCategoryListDTO {
	
    UUID getId();
	
	String getProductCount();
	
	UUID getCategoryId();
	
	String getFileName();
	
	String getImageFileUrl();
	
	String getCategoryName();
	
	Status getStatus();
	
	
	
	
}
