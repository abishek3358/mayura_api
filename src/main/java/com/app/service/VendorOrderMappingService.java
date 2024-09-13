package com.app.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.VendorMappingDAO;
import com.app.dto.VendorHandledOrderInterface;
import com.app.dto.VendorOrderMappingDTO;
import com.app.dto.VendorOrdersDTO;
import com.app.entity.CustomerOrder;
import com.app.entity.FeedbackSetting;
import com.app.entity.VendorOrderMapping;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.VendorOrderStatus;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.CustomerRepository;
import com.app.repository.FeedbackSettingRepository;
import com.app.repository.VendorOrderMappingRepository;
import com.app.repository.VendorRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorOrderMappingService {

	private @NonNull VendorOrderMappingRepository vendorOrderMappingRepository;

	private @NonNull VendorRepository vendorRepository;

	private @NonNull CustomerRepository customerRepository;

	private @NonNull CustomerOrderRepository customerOrderRepository;
	
	private @NonNull VendorMappingDAO vendorMappingDao;
	
	private @NonNull FeedbackSettingRepository feedbackSettingRepository;
	
	public void saveOrUpdate(VendorOrderMapping obj) throws Exception {

		vendorOrderMappingRepository.saveAndFlush(obj);
		 
		LocalDate localDate = LocalDate.now();
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		CustomerOrder customerOrderObj = customerOrderRepository.findById(obj.getCustomerOrderId()).get();
		customerOrderObj.setOrderAllocatedDate(date);
		customerOrderObj.setOrderStatus(OrderStatus.PROGRESS);
		
		customerOrderRepository.saveAndFlush(customerOrderObj);

	}
	public void saveOrUpdates(VendorOrderMapping object) {
		vendorOrderMappingRepository.saveAndFlush(object);		
	}	
	public Optional<VendorOrderMapping> findById(UUID id) {
		return vendorOrderMappingRepository.findById(id);
	}

	public List<VendorOrderMapping> findAll() {
		return vendorOrderMappingRepository.findAll();
	}

	public List<VendorOrderMappingDTO> getAll() {
		List<VendorOrderMapping> vendorMappingList = vendorOrderMappingRepository.findAll();
		List<VendorOrderMappingDTO> vendorMappingDtoList = new ArrayList<VendorOrderMappingDTO>();
		VendorOrderMappingDTO vendorMappingDtoObj = null;
		for (VendorOrderMapping vendorMappingObj : vendorMappingList) {
			vendorMappingDtoObj = new VendorOrderMappingDTO();
			vendorMappingDtoObj.setId(vendorMappingObj.getId());
			vendorMappingDtoObj.setVendorId(vendorMappingObj.getVendorId());
			vendorMappingDtoObj
					.setVendorName(vendorRepository.findById(vendorMappingObj.getVendorId()).get().getName());
			vendorMappingDtoObj.setCustomerId(vendorMappingObj.getCustomerId());
			vendorMappingDtoObj
					.setCustomerName(customerRepository.findById(vendorMappingObj.getCustomerId()).get().getFullName());
			vendorMappingDtoObj.setCustomerOrderId(vendorMappingObj.getCustomerOrderId());
			vendorMappingDtoObj.setVendorOrderStatus(vendorMappingObj.getVendorOrderStatus());
			vendorMappingDtoList.add(vendorMappingDtoObj);
		}
		return vendorMappingDtoList;
	}

	public VendorOrderMappingDTO getById(UUID orderMappingId) {
		Optional<VendorOrderMapping> vendorOrderMappingOptional = vendorOrderMappingRepository.findById(orderMappingId);
		VendorOrderMapping vendorOrderMappingObj = vendorOrderMappingOptional.get();
		VendorOrderMappingDTO responseObj = new VendorOrderMappingDTO();
		responseObj.setId(orderMappingId);
		responseObj.setVendorId(vendorOrderMappingObj.getVendorId());
		responseObj.setCustomerId(vendorOrderMappingObj.getCustomerId());
		responseObj.setCustomerName(
				customerRepository.findById(vendorOrderMappingObj.getCustomerId()).get().getFullName());
		responseObj.setVendorName(vendorRepository.findById(vendorOrderMappingObj.getVendorId()).get().getName());
		responseObj.setCustomerOrderId(vendorOrderMappingObj.getCustomerOrderId());
		responseObj.setVendorOrderStatus(vendorOrderMappingObj.getVendorOrderStatus());
		return responseObj;

	}
	public void deleteById(UUID id) {
		vendorOrderMappingRepository.deleteById(id);
	}
	public List<VendorOrdersDTO> findByVendorOrders(VendorOrderStatus orderStatus) {
		
		return vendorMappingDao.findByVendorOrders(orderStatus);
	}
	public List<VendorHandledOrderInterface> findByVendorHandledOrder(UUID id) {
		return vendorOrderMappingRepository.findByVendorHandledOrder(id.toString());
	}
	
	public void updateOrderAsReject(VendorOrderMapping obj) throws Exception {
		vendorOrderMappingRepository.saveAndFlush(obj);
		//orderNotificationService.orderCancelled(obj);
	}
	

}