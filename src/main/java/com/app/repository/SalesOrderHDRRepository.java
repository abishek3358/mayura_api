package com.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.SalesOrderHdrDTO;
import com.app.entity.SalesOrderHDR;

public interface SalesOrderHDRRepository extends WriteableRepository<SalesOrderHDR, UUID> {

	@Query(value = "SELECT t.id AS salesId,t.sales_order_number AS soNumber,t.sales_ref_number AS soRefNumber,ta.name AS associateName, "
			+ " DATE_FORMAT(t.order_date,'%d/%m/%Y') AS orderPlacedDate,DATE_FORMAT(t.date_of_cancellation,'%d/%m/%Y') AS cancelledDate, DATE_FORMAT(t.committed_date_of_delivery,'%d/%m/%Y') AS commitedDateOfDelivery,t.so_total_cost AS totalCost,t.grand_total AS grandTotal,tsm.name AS status "
			+ " FROM trading_sales_order_hdr t INNER JOIN trading_status_master tsm ON t.order_status=tsm.id INNER JOIN trading_associate ta ON t.associate_id=ta.id WHERE t.order_status= :id order by t.order_date ", nativeQuery = true)
	public List<SalesOrderHdrDTO> findAllSalesByStatus(String id);

}
