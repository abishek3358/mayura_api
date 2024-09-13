package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.PurchaseOrderDTO;
import com.app.dto.PurchaseOrderHdrDTO;
import com.app.entity.ProductTransaction;
import com.app.entity.PurchaseOrderHDR;
import com.app.repository.PurchaseOrderHDRRepository;
import com.app.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class PurchaseOrderHDRService {

	private @NonNull PurchaseOrderHDRRepository tradingPurchaseOrderHDRRepository;

	private @NonNull AssociateInfoService associateInfoService;
	
	private @NonNull ProductTransactionService productTransactionService;
	
	@Transactional
	public void saveOrUpdate(PurchaseOrderHDR obj) {
		if(obj.getProductTransactionList()!=null && !obj.getProductTransactionList().isEmpty()) {
			for(ProductTransaction productTrasactionObj:obj.getProductTransactionList()) {
				productTransactionService.saveOrUpdate(productTrasactionObj);
			}
		}
		tradingPurchaseOrderHDRRepository.saveAndFlush(obj);
	}

	public Optional<PurchaseOrderHDR> findById(UUID id) {
		return tradingPurchaseOrderHDRRepository.findById(id);
	}

	public List<PurchaseOrderDTO> findAll() {

		PurchaseOrderDTO dto = null;
		List<PurchaseOrderDTO> list = new ArrayList<>();

		for (PurchaseOrderHDR o : tradingPurchaseOrderHDRRepository.findAll()) {
			dto = new PurchaseOrderDTO();
			if (o.getActualDeliveryDate() != null)
				dto.setActualDeliveryDate(
						DateUtil.getFormatedDateFromDbDateFormat(o.getActualDeliveryDate().toString()));
			dto.setAssociateId(o.getAssociateId());
			if (o.getExpectedDeliveryDate() != null)
				dto.setExpectedDeliveryDate(
						DateUtil.getFormatedDateFromDbDateFormat(o.getExpectedDeliveryDate().toString()));
			if (o.getOrderPlacedDate() != null)
				dto.setOrderPlacedDate(DateUtil.getFormatedDateFromDbDateFormat(o.getOrderPlacedDate().toString()));
			dto.setPoNumber(o.getPoNumber());
			dto.setPoRefNumber(o.getPoRefNumber());
			dto.setGrandTotal(o.getGrandTotal());
			dto.setStatusMaster(o.getStatusMasterObj());
			dto.setPurchaseId(o.getId());
			dto.setTotalCost(o.getTotalCost());
			dto.setVendorName(associateInfoService.findById(o.getAssociateId()).get().getName());
			list.add(dto);

		}
		
		

		return list.stream().sorted((s1,s2)->s2.getPoNumber().compareTo(s1.getPoNumber())).collect(Collectors.toList());
	}

	public void save(PurchaseOrderHDR obj) {
		tradingPurchaseOrderHDRRepository.save(obj);
	}

	public List<PurchaseOrderHdrDTO> findAllPurchase() {
		return tradingPurchaseOrderHDRRepository.findAllPurchase();
	}

	public List<PurchaseOrderHdrDTO> findAllPurchaseByStatusMasterId(String statusMasterId) {
		return tradingPurchaseOrderHDRRepository.findAllPurchaseByStatusMasterId(statusMasterId);
	}

	
}
