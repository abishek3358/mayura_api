package com.app.dto;

import java.util.UUID;

import com.app.entity.State;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CityDTO {
	
	private UUID id;
	
	private String name;
	
	private String shortName;
	
	private String zipCode;
	
	private Double latitude;
	
	private Double longitude;
	
	private UUID stateId;
	
	private UUID countryId;	
}
