package com.app.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.dto.ProductTransactionResponseDTO;
import com.app.entity.ProductTransaction;

public interface ProductTransactionRepository extends WriteableRepository<ProductTransaction, UUID> {

	@Query(value = "select tpt.id AS id,tpt.trx_quantity AS transQty, tpt.trx_source AS transSource,DATE_FORMAT(tpt.trx_date,'%d/%m/%Y') AS transDate , tpo.po_number AS poNo, tso.sales_order_number AS soNo,tp.product_name AS productName "
			+ " from trading_product_transaction tpt INNER JOIN trading_product tp ON tpt.product_id=tp.id "
			+ " LEFT JOIN trading_purchase_order_hdr tpo ON tpt.trx_id=tpo.id LEFT JOIN trading_sales_order_hdr tso ON tpt.trx_id=tso.id where tpt.trx_date >= :fromDate and tpt.trx_date <= :toDate",nativeQuery = true)
	List<ProductTransactionResponseDTO> getproductTransByFromAndToDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query(value = "select tpt.id AS id,tpt.trx_quantity AS transQty, tpt.trx_source AS transSource,DATE_FORMAT(tpt.trx_date,'%d/%m/%Y') AS transDate , tpo.po_number AS poNo, tso.sales_order_number AS soNo,tp.product_name AS productName "
			+ " from trading_product_transaction tpt INNER JOIN trading_product tp ON tpt.product_id=tp.id "
			+ " LEFT JOIN trading_purchase_order_hdr tpo ON tpt.trx_id=tpo.id LEFT JOIN trading_sales_order_hdr tso ON tpt.trx_id=tso.id where product_id = :productId AND (tpt.trx_date >= :fromDate AND tpt.trx_date<= :toDate)",nativeQuery = true)
	List<ProductTransactionResponseDTO> getproductTransByFromToDateAndProductId(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,@Param("productId") String productId);
}
