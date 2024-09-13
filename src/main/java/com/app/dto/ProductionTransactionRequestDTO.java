package com.app.dto;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductionTransactionRequestDTO {
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	private UUID productId;

}
