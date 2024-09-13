package com.app.repository;

import java.util.List;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.VendorAccount;

public interface VendorAccountRepository extends WriteableRepository<VendorAccount, UUID> {

	List<VendorAccount> findByVendorId(UUID id);
	
}
