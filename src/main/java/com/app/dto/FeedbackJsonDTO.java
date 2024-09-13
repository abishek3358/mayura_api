package com.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FeedbackJsonDTO {

	private Integer timing;
    private Integer quality;
    private Integer rate;
    private Integer quantity;
    private Integer service;
    
	
	
}
