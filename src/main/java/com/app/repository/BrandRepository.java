package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.Brand;
import com.app.jpa.interfaces.ProductBrandInterface;

public interface BrandRepository extends WriteableRepository<Brand, UUID> {

	public Optional<Brand> findByBrandName(String brandName);
	
	@Query(value = "select count(p.id) as productCount,p.brand_id as brandId,b.id as id,b.description,b.name as brandName,"
			+ "b.file_name as fileName from brand b left join product p on b.id=p.brand_id left join product_dtl pdtl on pdtl.product_id=p.id "
			+ "left join unit_of_measurement uom on pdtl.product_uom_id=uom.id group by b.id", nativeQuery = true)
	List<ProductBrandInterface> findAllBrand();

	@Query(value = "select count(p.brand_id) as productCount,p.brand_id as brandId,"
			+ "b.id as id,b.description,b.name as brandName,"
			+ "b.file_name as fileName from brand b inner join "
			+ "product p on b.id=p.brand_id group by b.id", nativeQuery = true)
	List<ProductBrandInterface> findAllBrands();

	@Query(value = "select * from brand where id:=brandId", nativeQuery = true)
	public Optional<Brand> findByBrandId(UUID brandId);

	@Query(value="SELECT distinct(p.product_name) as productName,p.id as productId,dtl.sales_rate as salesRate,"
			+ "img.file_name as fileName,uom.name as uomName,co.no_of_stars as noOfStars "
			+ "from brand b inner join product p on p.brand_id=b.id inner join "
			+ "product_dtl dtl on p.id=dtl.product_id inner join product_images img on "
			+ "img.product_id=p.id inner join unit_of_measurement uom on "
			+ "dtl.product_uom_id=uom.id inner join customer_order_items coi "
			+ "on p.id=coi.product_id inner join customer_order co on "
			+ "co.id=coi.customer_order_id where b.id=:brandId",nativeQuery=true)
	List<ProductBrandInterface> getBrandProductsById(String brandId);


}
