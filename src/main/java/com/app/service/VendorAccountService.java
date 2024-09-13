package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.VendorAccountDTO;
import com.app.dto.VendorBranchDTO;
import com.app.entity.VendorAccount;
import com.app.entity.VendorBranch;
import com.app.repository.VendorAccountRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class VendorAccountService {

	private @NonNull VendorAccountRepository vendorAccountRepository;

	public void saveOrUpdate(VendorAccount obj) {
		vendorAccountRepository.saveAndFlush(obj);
	}

	public Optional<VendorAccount> findById(UUID id) {
		return vendorAccountRepository.findById(id);
	}

	public List<VendorAccount> findAll() {
		return vendorAccountRepository.findAll();
	}

	public void deleteById(UUID id) {
		vendorAccountRepository.deleteById(id);
	}

	public VendorAccountDTO getById(UUID id) {
		Optional<VendorAccount> vendorBranchOptional = vendorAccountRepository.findById(id);
		VendorAccount vendorAccountObj = vendorBranchOptional.get();
		VendorAccountDTO vendorAccountDtoObj = new VendorAccountDTO();
		vendorAccountDtoObj.setId(vendorAccountObj.getId());
		vendorAccountDtoObj.setVendorId(vendorAccountObj.getVendorId());
		vendorAccountDtoObj.setCommissionPercentage(vendorAccountObj.getCommissionPercentage());
		vendorAccountDtoObj.setPayoutPeriod(vendorAccountObj.getPayoutPeriod());
		return vendorAccountDtoObj;
	}

	public List<VendorAccountDTO> getAll() {
		List<VendorAccount> vendorAccountList = vendorAccountRepository.findAll();
		List<VendorAccountDTO> vendorAccountDtoList = new ArrayList<VendorAccountDTO>();
		VendorAccountDTO vendorAccountDtoObj = null;
		for (VendorAccount vendorAccountObj : vendorAccountList) {
			vendorAccountDtoObj = new VendorAccountDTO();
			vendorAccountDtoObj.setId(vendorAccountObj.getId());
			vendorAccountDtoObj.setVendorId(vendorAccountObj.getVendorId());
			vendorAccountDtoObj.setCommissionPercentage(vendorAccountObj.getCommissionPercentage());
			vendorAccountDtoObj.setPayoutPeriod(vendorAccountObj.getPayoutPeriod());
			vendorAccountDtoList.add(vendorAccountDtoObj);
		}
		return vendorAccountDtoList;
	}
}
