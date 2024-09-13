package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.SalesOrderDTO;
import com.app.dto.SalesOrderHdrDTO;
import com.app.entity.SalesOrderHDR;
import com.app.repository.SalesOrderHDRRepository;
import com.app.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class SalesOrderHDRService {
	
	private @NotNull AssociateInfoService associateInfoService;
	private @NonNull SalesOrderHDRRepository salesOrderHDRRepository;

	public void saveOrUpdate(SalesOrderHDR obj) {
		salesOrderHDRRepository.saveAndFlush(obj);
	}

	public Optional<SalesOrderHDR> findById(UUID id) {

		return salesOrderHDRRepository.findById(id);
	}

	public void save(SalesOrderHDR obj) {
		salesOrderHDRRepository.save(obj);
	}

	public List<SalesOrderDTO> findAll() {
		
		List<SalesOrderDTO> list=new ArrayList<>();
		
		SalesOrderDTO dto=null;
		for(SalesOrderHDR salesOrder:salesOrderHDRRepository.findAll()) {
			dto=new SalesOrderDTO();
			dto.setId(salesOrder.getId());
			dto.setAssociateName(associateInfoService.findById(salesOrder.getAssociateId()).get().getName());
			dto.setCommitedDateOfDelivery(salesOrder.getCommittedDateOfDelivery()!=null?DateUtil.getFormatedDateFromDbDateFormat(salesOrder.getCommittedDateOfDelivery().toString()):"");
			dto.setGrandTotal(salesOrder.getGrandTotal());
			dto.setOrderDate(DateUtil.getFormatedDateFromDbDateFormat(salesOrder.getOrderDate().toString()));
			dto.setSalesOrderNumber(salesOrder.getSalesOrderNumber());
			dto.setSalesRefNumber(salesOrder.getSalesRefNumber());
			list.add(dto);
		}
		
		return list.stream().sorted((s1,s2)->s2.getSalesOrderNumber().compareTo(s1.getSalesOrderNumber())).collect(Collectors.toList());
	}
	
	public List<SalesOrderHdrDTO> getAllSalesByStatus(String id){
		return salesOrderHDRRepository.findAllSalesByStatus(id);
		
	}
	
	

}
