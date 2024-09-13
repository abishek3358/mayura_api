package com.app.dto;

import java.util.UUID;

public interface ProductTransactionResponseDTO {
	UUID getId();

	String getPoNo();

	String getSoNo();

	String getProductName();

	String getTransSource();

	String getTransDate();

	Double getTransQty();

}
