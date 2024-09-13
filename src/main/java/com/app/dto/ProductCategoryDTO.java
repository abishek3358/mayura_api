package com.app.dto;

import java.util.UUID;

import javax.persistence.Transient;

import com.app.enumeration.Status;

import groovy.transform.builder.Builder;
import lombok.Data;
@Data
@Builder
public class ProductCategoryDTO {
	
	
	
	
	private UUID id;
	
	private String name;
	
	private String shortName;
	
	private String description;
	
	private String status;
	
	private UUID productTypeId;
	
	private String productTypeName;
	
	private String productCount;
	
	private String fileName;
	
	private String fileUrl;	
	
	private String FileBase64;
	
	

	
	
}
