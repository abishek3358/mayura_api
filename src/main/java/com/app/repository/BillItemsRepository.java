package com.app.repository;

import java.util.List;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.BillItems;

public interface BillItemsRepository extends WriteableRepository<BillItems, UUID> {
	
	public List<BillItems> findByBillId(UUID billId);
}
