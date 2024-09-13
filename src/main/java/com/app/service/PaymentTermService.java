package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.PaymentTerm;
import com.app.repository.PaymentTermRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class PaymentTermService {

	private @NonNull PaymentTermRepository paymentTermRepository;

	public void saveOrUpdate(PaymentTerm obj) {
		paymentTermRepository.saveAndFlush(obj);
	}

	public Optional<PaymentTerm> findById(UUID id) {
		return paymentTermRepository.findById(id);
	}

	public List<PaymentTerm> findAll() {
		return paymentTermRepository.findAll();
	}

}
