package com.app.repository;

import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.TradingSetting;

public interface TradingSettingsRepository extends WriteableRepository<TradingSetting, UUID> {
  
    
}
