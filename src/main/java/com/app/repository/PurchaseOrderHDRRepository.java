package com.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.dto.PurchaseOrderHdrDTO;
import com.app.entity.PurchaseOrderHDR;

public interface PurchaseOrderHDRRepository extends WriteableRepository<PurchaseOrderHDR, UUID> {
	
	
	@Query(value="SELECT t.id as purchaseId,t.po_number as poNumber,t.associate_id as associateId, t.status_master_id as poStatusId,t.po_ref_number as poRefNumber,a.name as vendorName,t.order_placed_date as orderPlacedDate,m.name as status ,t.expected_delivery_date as expectedDeliveryDate, t.actual_delivery_date as actualDeliveryDate, t.total_cost as totalCost FROM trading_purchase_order_hdr t INNER JOIN trading_associate a on t.associate_id=a.id INNER JOIN trading_status_master m on m.id=t.status_master_id", nativeQuery=true)
	public List<PurchaseOrderHdrDTO> findAllPurchase();
	
	@Query(value="SELECT t.id as purchaseId,t.po_number as poNumber,t.associate_id as associateId, t.status_master_id as poStatusId,t.po_ref_number as poRefNumber,a.name as vendorName,DATE_FORMAT(t.order_placed_date,'%d/%m/%Y') as orderPlacedDate,DATE_FORMAT(t.cancellation_date,'%d/%m/%Y') as cancelledDate,m.name as status ,DATE_FORMAT(t.expected_delivery_date,'%d/%m/%Y') as expectedDeliveryDate,DATE_FORMAT(t.expected_delivery_date ,'%d/%m/%Y')  as actualDeliveryDate, t.total_cost AS totalCost, t.grand_total AS grandTotal FROM trading_purchase_order_hdr t INNER JOIN trading_associate a on t.associate_id=a.id INNER JOIN trading_status_master m on m.id=t.status_master_id where t.status_master_id= :statusMasterId order by t.order_placed_date ", nativeQuery=true)
	public List<PurchaseOrderHdrDTO> findAllPurchaseByStatusMasterId(@Param("statusMasterId") String statusMasterId);

}
