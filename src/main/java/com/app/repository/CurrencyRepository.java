package com.app.repository;

import java.util.Optional;

import com.app.config.WriteableRepository;
import com.app.entity.Currency;

public interface CurrencyRepository extends WriteableRepository<Currency, Long> {

	public Optional<Currency> findByCode(String code);
}
