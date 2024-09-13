package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.CustomerReward;
import com.app.entity.CustomerRewardHistory;
import com.app.entity.User;
import com.app.enumeration.RequestType;
import com.app.repository.CustomerRewardHistoryRepository;
import com.app.repository.CustomerRewardRepository;
import com.app.service.CountryStateService;
import com.app.service.CustomerRewardHistoryService;
import com.app.service.CustomerRewardService;
import com.app.service.MessagePropertyService;
import com.app.service.WarehouseService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Ulaganathan
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerRewardValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull CustomerRewardService customerRewardService;

	private @NonNull CustomerRewardRepository customerRewardRepository;

	@Autowired(required = true)
	CountryStateService countryStateService;

	private @NonNull WarehouseService warehouseService;

	private @NonNull CustomerRewardHistoryService customerRewardHistoryService;
	
	private @NonNull CustomerRewardHistoryRepository customerRewardHistoryRepository;
	
	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	/**
	 * method for product type validation Added by Ulaganathan
	 *
	 * @param httpHeader
	 * @return ValidationResult
	 * @throws Exception
	 */

	public ValidationResult validate(RequestType requestType, CustomerReward request, User user) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();

		CustomerReward customerReward = null;

		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
			Optional<CustomerReward> customerRewardOptional=customerRewardService.findByCustomerId(user.getReferenceId());
			if(customerRewardOptional.isPresent()) {
				customerReward=customerRewardOptional.get();
			}

		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}

			Optional<CustomerReward> customerRewardOptional = customerRewardService.findById(request.getId());
			if (!customerRewardOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("vendor.not.found"));
			}
			customerReward = customerRewardOptional.get();
		}
		
		
		List<CustomerRewardHistory> customerList = validateCustomerRewardHistoryList(requestType, request, errors);


		
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		if (null == customerReward) {
			customerReward = CustomerReward.builder().customerId(user.getReferenceId())
					.activeRewards(request.getActiveRewards()).usedRewards(request.getUsedRewards())
					.lockedRewards(request.getLockedRewards())
					.customerRewardHistoryList(customerList)
					.build();
		} else {
			customerReward.setCustomerId(user.getReferenceId());
			customerReward.setActiveRewards(request.getActiveRewards());
			customerReward.setUsedRewards(request.getUsedRewards());
			customerReward.setLockedRewards(request.getLockedRewards());
			customerReward.setCustomerRewardHistoryList(customerList);
		}
		
		result.setObject(customerReward);
		return result;
	}

	public List<CustomerRewardHistory> validateCustomerRewardHistoryList(RequestType reqType, CustomerReward obj,
			List<String> errors) {

		errors = new ArrayList<>();

		List<CustomerRewardHistory> customerRewardHistoryList = new ArrayList<CustomerRewardHistory>();
		
		Map<UUID, CustomerRewardHistory> customerRewardHistoryMap = null;
		
		if (reqType.equals(RequestType.PUT)) {
			List<CustomerRewardHistory> customerRewardHistorylist = customerRewardHistoryService.findByCustomerRewardId(obj.getId());	
			customerRewardHistoryMap = customerRewardHistorylist.stream().collect(Collectors.toMap(CustomerRewardHistory::getId, s -> s));
		}

		for (CustomerRewardHistory request : obj.getCustomerRewardHistoryList()) {
			CustomerRewardHistory customerRewardHistory = null;

			if (!ValidationUtil.isNull(request.getId())) {
				Optional<CustomerRewardHistory> customerRewardHistoryOptional = customerRewardHistoryService
						.findById(request.getId());
				customerRewardHistory = customerRewardHistoryOptional.get();
			}
			
			if (ValidationUtil.isNull(request.getRewards())) {
				errors.add(messageSource.getMessage("rewards.required"));
			}
			if (ValidationUtil.isNullObject(request.getTransactionType())) {
				errors.add(messageSource.getMessage("transaction.type.required"));

			}
			
			if (ValidationUtil.isNullObject(request.getTransactionDate())) {
				errors.add(messageSource.getMessage("transaction.date.required"));

			}

			ValidationResult result = new ValidationResult();
			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}

			if (null == customerRewardHistory) {
				customerRewardHistory = CustomerRewardHistory.builder()
						.rewards(request.getRewards())
						.transactionDate(request.getTransactionDate())
						.transactionType(request.getTransactionType())
						.notes(request.getNotes())
						.build();
			} else {
				customerRewardHistoryMap.remove(request.getId());
				
				customerRewardHistory.setRewards(request.getRewards());
				customerRewardHistory.setNotes(request.getNotes());
				customerRewardHistory.setTransactionDate(request.getTransactionDate());
				customerRewardHistory.setTransactionType(request.getTransactionType());
				customerRewardHistory.setNotes(request.getNotes());

			}
			result.setObject(customerRewardHistory);
			customerRewardHistoryList.add(customerRewardHistory);

		}
		
		if (reqType.equals(RequestType.PUT)) {
			for (UUID id : customerRewardHistoryMap.keySet()) {
				customerRewardHistoryRepository.deleteById(id)
;
			}
		}
		return customerRewardHistoryList;

	}
}