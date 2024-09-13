package com.app.repository;

import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.SalesOrderDTL;

public interface SalesOrderDTLRepository extends WriteableRepository<SalesOrderDTL, UUID> {

//	@Query(value = "SELECT t.id as purchaseId,t.po_number as poNumber,t.associate_id as associateId, t.po_status_id as poStatusId,t.po_ref_number as poRefNumber,a.name as vendorName,t.order_placed_date as orderPlacedDate,m.name as status ,t.expected_delivery_date as expectedDeliveryDate, t.actual_delivery_date as actualDeliveryDate, t.total_cost as totalCost FROM trading_purchase_order_hdr t INNER JOIN trading_associate a on t.associate_id=a.id INNER JOIN trading_purchase_status_master m on m.id=t.po_status_id", nativeQuery = true)
//	public List<SalesOrderHDRDto> findAllPurchase();

}
