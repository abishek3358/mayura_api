package com.app.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.app.config.WriteableRepository;
import com.app.entity.FeedbackSetting;


@Repository
public interface FeedbackSettingRepository extends WriteableRepository<FeedbackSetting, UUID>{

	//Optional<FeedbackSetting> findByOrderId(@NotNull(message = "Customer order id cannot be blank") UUID customerOrderId);

}
