package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.GstDao;
import com.app.dto.GstResponse;
import com.app.entity.GST;
import com.app.repository.GSTRepository;
import com.twilio.rest.api.v2010.Account.Status;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class GSTService {
	
	private @NonNull GSTRepository gstRepository;
	
	private @NonNull GstDao gstDao;
	
	public void saveOrUpdate(GST obj) {
		gstRepository.saveAndFlush(obj);
	}
	
	public Optional<GST> findById(UUID id) {
		return gstRepository.findById(id);
	}
	
	public List<GST> findAll() {
		return gstRepository.findAll();
	}

	public Optional<GST> findByName(String name) {
		return gstRepository.findByName(name);
	}
	
	public List<GstResponse> findByGet() {
		// TODO Auto-generated method stub
		return gstDao.findByGet();
	}

	
	public Optional<GST> findByNameExcludeId(String name,UUID excludeId) {
		return gstRepository.findByNameExcludeId(name,excludeId);
	}

	
	
	
}
