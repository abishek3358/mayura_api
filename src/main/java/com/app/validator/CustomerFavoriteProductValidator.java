package com.app.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.advice.ObjectInvalidException;
import com.app.entity.CustomerFavoriteProduct;
import com.app.enumeration.RequestType;
import com.app.service.CustomerFavoriteProductService;
import com.app.service.CustomerService;
import com.app.service.MessagePropertyService;
import com.app.service.UserService;
import com.app.util.ValidationUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @author Nisha Kailasam
 */
@Service
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerFavoriteProductValidator {

	@Autowired
	MessagePropertyService messageSource;

	private @NonNull CustomerFavoriteProductService customerFavoriteProductService;
	private @NonNull UserService usertService;
	private @NonNull CustomerService customerService;

	public ValidationResult validate(RequestType requestType, CustomerFavoriteProduct request, UUID customerId) {
		List<String> errors = null;
		
		errors = new ArrayList<>();
		CustomerFavoriteProduct customerFavoriteProduct = null;
		if (requestType.equals(RequestType.POST)) {
			if (!ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("invalid.request.payload"));
			}
		} else {
			if (ValidationUtil.isNull(request.getId())) {
				throw new ObjectInvalidException(messageSource.getMessage("request.payload.invalid"));
			}

			Optional<CustomerFavoriteProduct> CustomerFavoriteProductOptional = customerFavoriteProductService
					.findById(request.getId());
			if (!CustomerFavoriteProductOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.getMessage("customer.favorite.product.not.found"));
			}

			customerFavoriteProduct = CustomerFavoriteProductOptional.get();
		}

		ValidationResult result = new ValidationResult();
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}

		if (null == customerFavoriteProduct) {
			customerFavoriteProduct = CustomerFavoriteProduct.builder().customerId(customerId)
					.productId(request.getProductId()).build();
		}
		result.setObject(customerFavoriteProduct);
		return result;
	}

}
