package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.Discount;
import com.app.entity.Product;

public interface DiscountRepository extends WriteableRepository<Discount, UUID> {
	
	Optional<Discount> findByDiscountCode(String code);

}
