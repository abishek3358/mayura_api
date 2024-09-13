package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.VendorHandledOrderInterface;
import com.app.entity.VendorOrderMapping;

public interface VendorOrderMappingRepository extends WriteableRepository<VendorOrderMapping, UUID> {

	@Query("SELECT WH FROM VendorOrderMapping WH WHERE WH.customerOrderId = :customerOrderId ")
	Optional<VendorOrderMapping> findByCustomerOrderId(UUID customerOrderId);

	@Query(value = "SELECT COUNT(DISTINCT vom.customer_order_id) AS handledOrders, AVG(fs.overall_ratings) AS ratings \r\n"
			+ " FROM vendor_order_mapping vom\r\n"
			+ "LEFT JOIN  feedback_setting fs ON vom.vendor_id = fs.vendor_id\r\n"
			+ "WHERE  vom.vendor_id =:id  AND vom.vendor_order_status = 'DELIVERED'\r\n"
			+ "  AND fs.overall_ratings IS NOT NULL;", nativeQuery = true)
	List<VendorHandledOrderInterface> findByVendorHandledOrder(String id);

//	@Query(value="select v.id as vendorId, v.name as vendorName, c.full_name as customerName,c.id as customerId from vendor_order_mapping vom inner join customer c on  vom.customer_id=c.id inner join vendor v on  vom.vendor_id=v.id  where vom.id =:orderMappingId",nativeQuery = true)
//	Optional<VendorOrderMappingDTO> getById(UUID orderMappingId);
//	
}
