package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.ManageRewards;
import com.app.enumeration.ProductDiscountType;
import com.app.enumeration.RequestType;
import com.app.enumeration.RewardEarnType;
import com.app.enumeration.RewardType;
import com.app.service.ManageRewardsService;
import com.app.service.MessagePropertyService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class ManageRewardsValidator {
	@Autowired
	MessagePropertyService messageSource;

	private @NonNull ManageRewardsService manageRewardsService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for UnitOfMeasurement validation Added by Ulaganathan
	 * 
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */
	public ValidationResult validate(RequestType requestType, ManageRewards request) {

		errors = new ArrayList<>();
		ManageRewards manageRewards = null;

		manageRewards = manageRewardsService.findBYRewardType(request.getRewardType());

		if (request.getRewardType().equals(RewardType.SIGNUP)) {
			request.setRewardearntype(RewardEarnType.FLAT);
		} else {
			request.setRewardearntype(RewardEarnType.PERCENTAGE);
		}

		if (ValidationUtil.isNullObject(request.getRewardType())) {
			errors.add(messageSource.getMessage("manageRewards.reward.type.required"));
		}

		if (ValidationUtil.isNull(request.getReward())) {
			errors.add(messageSource.getMessage("manage.rewards.required"));
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		if (null == manageRewards) {
			manageRewards = ManageRewards.builder().rewardType(request.getRewardType()).reward(request.getReward())
					.rewardearntype(request.getRewardearntype()).build();
		} else {

			manageRewards.setRewardType(request.getRewardType());
			manageRewards.setReward(request.getReward());
			manageRewards.setRewardearntype(request.getRewardearntype());

		}
		result.setObject(manageRewards);
		return result;
	}

}
