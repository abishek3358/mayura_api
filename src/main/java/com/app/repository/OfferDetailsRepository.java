package com.app.repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.OfferDetails;


public interface OfferDetailsRepository extends WriteableRepository<OfferDetails, UUID> {
	

	Optional<OfferDetails> findByOfferCode(String offerCode);
	
}
