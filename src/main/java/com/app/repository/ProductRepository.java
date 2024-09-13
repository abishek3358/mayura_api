package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.dto.OverAllProductDTO;
import com.app.dto.ProductDTO;
import com.app.dto.ProductResponseDTO;
import com.app.dto.ProductViewDTO;
import com.app.entity.Product;
import com.app.entity.ProductCategory;

public interface ProductRepository extends WriteableRepository<Product, UUID> {

	Optional<Product> findByProductName(String name);

	Optional<Product> findByProductCode(String code);

	@Query("SELECT PD FROM Product PD WHERE PD.productName = :productName and PD.id <> :excludeId")
	Optional<Product> findByProductNameExcludeId(@Param("productName") String productName,@Param("excludeId") UUID excludeId);

	@Query("SELECT PD FROM Product PD WHERE PD.productCode = :productCode and PD.id <> :excludeId")
	Optional<Product> findByProductCodeExcludeId(@Param("productCode") String productCode,
			@Param("excludeId") UUID excludeId);


	@Query(value = " SELECT distinct(dtl.id) as id,p.product_name as productName,uom.short_name as uom,dtl.sales_rate as price,"
			+ "img.file_name as imageName,dtl.sales_rate as salesRate,dtl.disc_type as discType,dtl.discount as productDiscount,"
			+ "co.no_of_stars,if(cfp.status ='ACTIVE','true','false') as favorite FROM product p "
			+ "inner join product_dtl dtl on p.id=dtl.product_id "
			+ "left join customer_favorite_product cfp on cfp.product_id=dtl.id "
			+ "inner join unit_of_measurement uom on uom.id=dtl.product_uom_id "
			+ "inner join product_images img on p.id=img.product_id "
			+ "inner join customer_order_items coi on p.id=coi.product_id "
			+ "inner join customer_order co on co.id=coi.customer_order_id",nativeQuery = true)	
	List<ProductDTO> findAllProducts();

	@Query(value = "select distinct(p.product_name) as productName,p.product_code as productCode,"
			+ "dtl.id as productDtlId,img.product_id,uom.id as uomId,"
			+ "img.file_name as fileName,uom.short_name as uomName,b.name as brandName,"
			+ "dtl.disc_type as productDiscType,dtl.discount as productDiscount,"
			+ "img.product_id as id,img.product_image_type as imageType,"
			+ "sales_rate as salesRate,if(cfp.status ='ACTIVE','true','false') as favorite,"
			+ "(select if(round(max(co.no_of_stars)) is null,0,round(max(co.no_of_stars))) from "
			+ "customer_order_items coi inner join customer_order co on coi.customer_order_id =co.id where "
			+ "coi.product_id=p.id) as noOfStars from product_dtl dtl "
			+ "inner join unit_of_measurement uom on uom.id = dtl.product_uom_id "
			+ "inner join product_images img on dtl.product_id=img.product_id and img.product_image_type='LISTING' "
			+ "inner join product p on p.id = dtl.product_id "
			+ "left join customer_favorite_product cfp on cfp.product_id=dtl.id "
			+ "inner join brand b on p.brand_id=b.id where p.product_category_id =:categoryId", nativeQuery = true)
	List<ProductDTO> findAllProductsByCategoryId(@Param("categoryId") String categoryId);
	
	/*
	 * @Query(value =
	 * "select img.product_id,p.product_name as productName,img.file_name as imageName,concat(dtl.sales_rate,\"/\",uom.short_name) as priceWithUom,img.file_url as imageFileUrl,uom.short_name as uomName,dtl.disc_type as discType,dtl.discount as productDiscount,co.no_of_stars as noOfStars,img.product_id as productId,sales_rate as salesRate from product_dtl dtl inner join unit_of_measurement uom on uom.id = dtl.product_uom_id inner join product_images img on dtl.product_id=img.product_id and img.product_image_type='LISTING' inner join product p on p.id = dtl.product_id inner join customer_order_items coi on p.id=coi.product_id inner join customer_order co on co.id=coi.customer_order_id inner join product_category pc on pc.id=p.product_category_id where p.product_name like %:customerInput% or pc.name like %:customerInput%"
	 * ,nativeQuery = true) List<Product>
	 * findByUserReq(@Param("customerInput")String customerInput);
	 */
/*
	 * 
	 * @Query(value =
	 * "SELECT tp.id AS id, tp.product_uom_id AS productUomId, tu.name AS uomName, tp.purchase_rate AS purchaseRatePerUnit ,tp.sales_rate AS salesRatePerUnit, tg.gst_percentage AS gstPercentage, tp.initial_quantity AS availableQuantity "
	 * +
	 * "FROM trading_product tp INNER JOIN trading_product_category tpc ON tp.product_category_id=tpc.id "
	 * +
	 * "INNER JOIN trading_gst tg ON tpc.gst_id=tg.id INNER JOIN trading_uom tu ON tp.product_uom_id=tu.id"
	 * ,nativeQuery = true) List<ProductDetailsDTO> getProductDetails(UUID
	 * productId);
	 */

	@Query(value = "select p.* from product p inner join product_category pc on "
			+ "p.product_category_id=pc.id where p.product_name like %:customerInput% "
			+ "or pc.name like %:customerInput%", nativeQuery = true)
	List<Product> findByUserReq(@Param("customerInput") String customerInput);
	

	@Query(value = "select distinct(p.product_name) as productName,dtl.id as productDtlId,p.id as id,"
			+ "dtl.disc_type as discType,(select if(max(dtl.discount) is null,0,max(dtl.discount)) from product_dtl dtl "
			+ "inner join product p on p.id=dtl.product_id) as productdiscount,b.name as brandName,"
			+ "uom.id as uomId,uom.short_name as uomName,dtl.sales_rate as price,"
			+ "if(cfp.status ='ACTIVE','true','false') as favorite,(select file_name from product_images "
			+ "where product_id=p.id and product_image_type='LISTING' limit 1) as fileName,"
			+ "(select if(round(max(co.no_of_stars)) is null,0,round(max(co.no_of_stars))) from "
			+ "customer_order_items coi inner join customer_order co on coi.customer_order_id =co.id where "
			+ "coi.product_id=p.id) as noOfStars from product p left join product_dtl dtl on p.id=dtl.product_id "
			+ "left join brand b on b.id=p.brand_id left join customer_favorite_product cfp "
			+ "on cfp.product_id=dtl.id left join unit_of_measurement uom on uom.id=dtl.product_uom_id "
			+ "left join product_images img on p.id=img.product_id left join customer_order_items coi "
			+ "on p.id=coi.product_id left join "
			+ "customer_order co on co.id=coi.customer_order_id where co.no_of_stars>=3 ",nativeQuery = true)
	List<ProductViewDTO> findTopRatedProducts();

	@Query(value = " select dtl.id as id,p.product_name as productName,uom.short_name as uom,"
			+ "dtl.sales_rate as price,"
			+ "img.file_name as fileName,dtl.sales_rate as salesRate,dtl.disc_type as discType,"
			+ "dtl.discount as productDiscount,"
			+ "co.no_of_stars from product p  inner join product_dtl dtl on p.id=dtl.product_id "
			+ "inner join unit_of_measurement uom on uom.id=dtl.product_uom_id inner join product_images img "
			+ "on p.id=img.product_id inner join customer_order_items coi on p.id=coi.product_id inner join "
			+ "customer_order co on co.id=coi.customer_order_id where p.id=:id",nativeQuery = true)	
	Optional<ProductResponseDTO> getById(UUID id);

	/*
	 * @Query(value =
	 * "select p.id as id, p.product_name as productName from product p  where "
	 * +"p.product_name like %:searchKey%",nativeQuery = true)
	 */
	@Query(value = "SELECT p.id as productId,p.product_name as productName,p.brand_id as productBrancdId,"
			+ "p.product_category_id as productCategoryId FROM "
			+ "product_category pc join product p where pc.name like %:searchKey%  or p.product_name "
			+ "like %:searchKey% ",nativeQuery = true)
	List<OverAllProductDTO> findByProductSearch(String searchKey);

	@Query(value = "SELECT p.id as productId,p.product_name as productName,p.brand_id as productBrancdId,"
			+ "p.product_category_id as productCategoryId FROM  "
			+ "product p where p.product_category_id=:productCategoryObj",nativeQuery = true)
	List<OverAllProductDTO> findByProductCategoryList(ProductCategory productCategoryObj);

	@Query(value = "SELECT p.id as productId,p.product_name as productName,p.brand_id as productBrancdId,"
			+ "p.product_category_id as productCategoryId FROM product p ",nativeQuery = true)
	List<OverAllProductDTO> findByProduct();
	
	@Query(value = "SELECT p.id as productId,p.product_name as productName,p.brand_id as productBrancdId,"
			+ "p.product_category_id as productCategoryId FROM product p ",nativeQuery = true)
	List<OverAllProductDTO> findByMaxPriceProductSearch(Double maxPrice);


	@Query(value="select distinct(p.product_name) as productName,dtl.product_id as productId,p.product_code as productCode,"
			+ "p.tags,uom.short_name as uomName,b.name as brandName,p.description,p.detailed_description as detailedDescription,dtl.id as productDtlId,"
			+ "dtl.product_uom_id as uomId,dtl.sales_rate as salesRate,(select if(round(max(co.no_of_stars)) is null,0,round(max(co.no_of_stars))) from product p "
			+ "inner join customer_order_items coi on p.id=coi.product_id inner join customer_order co on co.id=coi.customer_order_id )as noOfStars "
			+ "from product_dtl dtl inner join unit_of_measurement uom on uom.id = dtl.product_uom_id "
			+ "inner join product p on p.id = dtl.product_id "
			+ "inner join brand b on p.brand_id=b.id where dtl.id=:productDtlId",nativeQuery=true)
	Optional<ProductDTO> findProductsByProductDtlId(String productDtlId);
	
	/* List<ProductDTO> findProductsByProductDtlId(String productDtlId); */

}
