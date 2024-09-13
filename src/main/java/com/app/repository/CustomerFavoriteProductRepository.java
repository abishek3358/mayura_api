package com.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.CustomerFavoriteProductDTO;

import com.app.entity.CustomerFavoriteProduct;

public interface CustomerFavoriteProductRepository extends WriteableRepository<CustomerFavoriteProduct, UUID> {

	@Query(value = "SELECT distinct (p.id) as id,cfp.id as customerFavoriteProductId,dtl.id as productDtlId,"
			+ "p.product_name as productName, dtl.sales_rate as price, b.name as brandName,"
			+ "p.tags as  tagList,concat(dtl.sales_rate,'/',uom.name) as priceWithUom, "
			+ "(select if(round(max(co.no_of_stars)) is null ,0,round(max(co.no_of_stars))) "
			+ "from customer_order_items coi inner join customer_order co on coi.customer_order_id =co.id where "
			+ "product_id=p.id) as productRating ,if(cfp.status ='ACTIVE','true','false') as favoriteItem, "
			+ "p.detailed_description as description ,(select "
			+ "concat('https://agrimart.s3-ap-southeast-1.amazonaws.com/products/',p.id,'/',file_name) "
			+ "from  product_images  where product_id=p.id and product_image_type='LISTING' limit 1) as fileUrl, "
			+ "if( dtl.quantity>0,'STOCK IN','STOCK OUT') as productStack FROM ecommerce.customer_favorite_product  cfp "
			+ "inner join product_dtl dtl on dtl.id=cfp.product_id "
			+ "inner join product p on p.id=dtl.product_id "
			+ "inner join brand b on b.id=p.brand_id "
			+ "inner join unit_of_measurement uom on uom.id=dtl.product_uom_id "
			+ "inner join  product_images  img on img.product_id=dtl.product_id  "
			+ "where customer_id=:customerId and cfp.status ='ACTIVE'", nativeQuery = true)
	List<CustomerFavoriteProductDTO> getAll(String customerId);

	@Modifying
	@Query(value = "update customer_favorite_product set status='DEACTIVE' "
			+ "where customer_id=:customerId and product_id=:productId ", nativeQuery = true)
	void deleteByFavoriteupdate(String customerId, String productId);

}
