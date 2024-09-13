package com.app.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GstResponse {
	
	private UUID id;
	
	private String name;
	
	
	private Double gst;
	
	
	private Double cgst;
	
	
	private Double sgst;
	
	
	private Double igst;
	
	private String description;
	
	private String status;
	 

}
