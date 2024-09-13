package com.app.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesDispatchDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUID Id;
	private UUID createdBy;
	
}
