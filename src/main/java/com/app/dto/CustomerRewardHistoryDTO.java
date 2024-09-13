package com.app.dto;

import java.util.Date;
import java.util.UUID;

import com.app.enumeration.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomerRewardHistoryDTO {
	private UUID id;
	private UUID rewardId;
	private double rewards;
	private String notes;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date transactionDate;
	
	private TransactionType transactionType;
	
	//private List<CustomerRewardHistoryDTO> customerRewardHistoryList;
	
	
	
	

}


