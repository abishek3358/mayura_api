package com.app.dto;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCancelDTO {
	
	    private UUID customerOrderId;
	    private String reason;
	    
	    public static void main(String[] args) throws Exception {

	    	OrderCancelDTO hdr = new OrderCancelDTO();	
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(hdr);
			System.out.print(jsonString);
		}
	}
	


