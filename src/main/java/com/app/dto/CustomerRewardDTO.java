package com.app.dto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomerRewardDTO {
	private UUID id;
	private UUID customerId;
	private double activeRewards;
	private double usedRewards;
	private double lockedRewards;
	private List<CustomerRewardHistoryDTO> customerRewardHistoryList;
	
	
	public static void main(String[] args) throws Exception {

		CustomerRewardDTO hdr = new CustomerRewardDTO();	
		hdr.setCustomerRewardHistoryList(Arrays.asList(new CustomerRewardHistoryDTO()));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(hdr);
		System.out.print(jsonString);
	}

}
	



