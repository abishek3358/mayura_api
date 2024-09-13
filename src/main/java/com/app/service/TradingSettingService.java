package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.TradingSetting;
import com.app.repository.TradingSettingsRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class TradingSettingService {
	
	private @NonNull TradingSettingsRepository billNoRepository;
	
	public void saveOrUpdate(TradingSetting obj) {
		billNoRepository.saveAndFlush(obj);
	}
	
	public Optional<TradingSetting> findById(UUID id) {
		return billNoRepository.findById(id);
	}
	
	public List<TradingSetting> findAll() {
		return billNoRepository.findAll();
	}

}
