package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Customer;
import com.app.entity.CustomerDeliveryAddress;
import com.app.enumeration.Status;
import com.app.repository.CustomerDeliveryAddressRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerDeliveryAddressService {

	private @NonNull CustomerDeliveryAddressRepository customerDeliveryAddressRepository;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull CountryService countryService;

	public void saveOrUpdate(CustomerDeliveryAddress obj) {
		customerDeliveryAddressRepository.saveAndFlush(obj);
	}

	public Optional<CustomerDeliveryAddress> findById(UUID id) {
		return customerDeliveryAddressRepository.findById(id);
	}

	public List<CustomerDeliveryAddress> findAll() {
		return customerDeliveryAddressRepository.findAll();
	}

	public List<CustomerDeliveryAddress> findByCustomer(Customer customer) {
		List<CustomerDeliveryAddress> addressList = customerDeliveryAddressRepository.findByCustomer(customer);
//		List<CustomerDeliveryAddressViewDTO> customerDeliveryAddressDtoList = new ArrayList<CustomerDeliveryAddressViewDTO>();
//		CustomerDeliveryAddressViewDTO customerDeliveryAddressDtoObj = null;
//		for (CustomerDeliveryAddress customerDeliveryAddressObj : addressList) {
//			customerDeliveryAddressDtoObj = new CustomerDeliveryAddressViewDTO();
//
//			customerDeliveryAddressDtoObj.setId(customerDeliveryAddressObj.getId());
//			customerDeliveryAddressDtoObj.setAddressLine1(customerDeliveryAddressObj.getAddressLine1());
//			customerDeliveryAddressDtoObj.setAddressLine2(customerDeliveryAddressObj.getAddressLine2());
//			customerDeliveryAddressDtoObj.setAddressType(customerDeliveryAddressObj.getAddressType());
//			customerDeliveryAddressDtoObj.setCityId(customerDeliveryAddressObj.getCityId());
//			customerDeliveryAddressDtoObj.setCityName(cityService.findNameById(customerDeliveryAddressObj.getCityId()));
//			customerDeliveryAddressDtoObj.setStateId(customerDeliveryAddressObj.getCityObj().getStateId());
//			customerDeliveryAddressDtoObj
//					.setStateName(stateService.findNameById(customerDeliveryAddressObj.getCityObj().getStateId()));
//			customerDeliveryAddressDtoObj.setCountryId(customerDeliveryAddressObj.getCityObj().getCountryId());
//			customerDeliveryAddressDtoObj.setCountryName(
//					countryService.findNameById(customerDeliveryAddressObj.getCityObj().getCountryId()));
//			customerDeliveryAddressDtoList.add(customerDeliveryAddressDtoObj);
//
//		}
		addressList.stream().forEach(value -> value.setCityRelatedInformation());
		return addressList;
	}

	public void deleteById(UUID deliveryAddressId) {
		customerDeliveryAddressRepository.deleteById(deliveryAddressId);
	}

	public List<CustomerDeliveryAddress> findByCustomerAndStatus(Customer customer, Status active) {
		return customerDeliveryAddressRepository.findByCustomerAndStatus(customer,active);
	}

}
