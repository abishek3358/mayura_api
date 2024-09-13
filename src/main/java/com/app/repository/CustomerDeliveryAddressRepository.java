package com.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.CustomerDeliveryAddressDTO;
import com.app.entity.Customer;
import com.app.entity.CustomerDeliveryAddress;
import com.app.enumeration.Status;

public interface CustomerDeliveryAddressRepository extends WriteableRepository<CustomerDeliveryAddress, UUID> {
	
	@Query("SELECT CDA FROM CustomerDeliveryAddress CDA where customer=:customer")
	List<CustomerDeliveryAddress> findByCustomer(Customer customer);

	@Query("SELECT CDA FROM CustomerDeliveryAddress CDA where customer=:customer and status=:active")
	List<CustomerDeliveryAddress> findByCustomerAndStatus(Customer customer, Status active);
	
}
