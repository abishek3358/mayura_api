package com.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.FeedbackJsonDTO;
import com.app.entity.FeedbackSetting;
import com.app.repository.FeedbackSettingRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;


@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class FeedbackSettingService {
	
	@Autowired
	MessagePropertyService messageSource;
	
	private @NonNull FeedbackSettingRepository feedbackSettingRepository ;
	

	public void saveFeedbackSetting(FeedbackSetting feedbackSetting) {

				int timing = feedbackSetting.getRawJson().getTiming();
				int quality = feedbackSetting.getRawJson().getQuality();
				int rate = feedbackSetting.getRawJson().getRate();
				int quantity = feedbackSetting.getRawJson().getQuantity();
				int service = feedbackSetting.getRawJson().getService();

				Double average = (double) ((timing + quality + rate + quantity + service)/ 5.0);

				feedbackSetting.setOverallRatings(average);
	
		feedbackSettingRepository.saveAndFlush(feedbackSetting);
		
	}

}
