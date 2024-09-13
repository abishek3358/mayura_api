package com.app.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTypeResponse {
	
	

	private UUID id;

	private String name;

	private String description;
  
	private String status;
	
}
