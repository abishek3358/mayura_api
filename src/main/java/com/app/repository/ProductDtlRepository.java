package com.app.repository;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.MyOrdersDTO;
import com.app.dto.OverAllProductDtlDTO;
import com.app.entity.Product;
import com.app.entity.ProductDtl;
import com.app.entity.UnitOfMeasurement;
import com.app.enumeration.OrderStatus;

public interface ProductDtlRepository extends WriteableRepository<ProductDtl, UUID> {

	List<ProductDtl> findByProduct(Product productObj);
	
	Optional<Product> findByProductAndUnitOfMeasurement(Product product, UnitOfMeasurement unitOfMeasurement);

	@Query(value="select * from product_dtl dtl where dtl.product_id=:productOptionalObj",nativeQuery=true)
	List<ProductDtl> findByProductId(Product productOptionalObj);


	@Query(value = "select dtl.id as productDtlId ,dtl.product_uom_id as UomId, uom.name as UomName ,dtl.disc_type as productDiscountType, dtl.sales_rate as saleRate, dtl.discount as discountAmount from product_dtl dtl inner join unit_of_measurement uom on uom.id = dtl.product_uom_id where dtl.product_id=:product",nativeQuery = true)
	List<OverAllProductDtlDTO> findByProductDtlList(Product product);

	

	

	

}
