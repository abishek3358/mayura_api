package com.app.repository;

import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.AppSetting;

public interface AppSettingRepository extends WriteableRepository<AppSetting, UUID> {

   
}
