package com.app.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.ContactUs;
import com.app.repository.ContactUsRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ContactUsService {
	
	private @NonNull ContactUsRepository contactUsRepository;
	
	@Transactional
	public void saveOrUpdate(ContactUs obj) throws Exception {
		contactUsRepository.saveAndFlush(obj);
	}

	public Optional<ContactUs> findById(UUID id) {
		return contactUsRepository.findById(id);
	}

	public List<ContactUs> findAll() {
		return contactUsRepository.findAll();
	}

	public void deleteById(UUID id) {
		contactUsRepository.deleteById(id);
	}

}