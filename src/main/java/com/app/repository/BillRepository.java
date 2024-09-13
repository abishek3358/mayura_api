package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.Bill;

public interface BillRepository extends WriteableRepository<Bill, UUID> {
  
	@Query(value = "SELECT tb.*,ta.name AS assoName,c.name AS currName FROM trading_bill tb INNER JOIN trading_associate ta ON tb.associate_id=ta.id INNER JOIN currency c ON tb.currency_id=c.id where tb.id= :id",nativeQuery = true)
	Optional<Bill> getById(@Param("id") String id);
	
	Optional<Bill> findByTransactionId(@Param("transactionId") UUID transactionId);
}
