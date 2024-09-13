package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.dto.ProductCategoryDTO;
import com.app.dto.ProductCategoryListDTO;
import com.app.dto.WebProductCategoryListDto;
import com.app.entity.ProductCategory;
import com.twilio.rest.api.v2010.Account.Status;

public interface ProductCategoryRepository extends WriteableRepository<ProductCategory, UUID> {
  
    Optional<ProductCategory> findByName(String name);
    
    @Query("SELECT PC FROM ProductCategory PC WHERE PC.name = :name and PC.id <> :excludeId")
    Optional<ProductCategory> findByNameExcludeId(@Param("name") String name, @Param("excludeId") UUID excludeId);

    @Query(value="SELECT id as id,name as name,short_name as shortName,description as description,filename as fileName FROM product_category",nativeQuery=true)
    List<WebProductCategoryListDto> findAllCategories();	

	@Query(value ="select count(p.product_category_id) as productCount,pc.id as productCategoryId,"
			+ "pc.name as categoryName,pc.filename as fileName"
			+ " from product_category pc left join product p on p.product_category_id=pc.id group by pc.id",nativeQuery=true)
	List<ProductCategoryListDTO> findAllCategory();
	

	
	@Query(value ="select count(p.product_category_id) as productCount,pc.id as CategoryId,"
			+ "pc.name as categoryName,pc.filename as fileName,pc.status as status"
			+ " from product_category pc "
			+ "left join product p on p.product_category_id=pc.id group by pc.id",nativeQuery=true)
	List<ProductCategoryListDTO> findAllCategoriesList();

	
	List<ProductCategory> findByStatus(Status active);	
	
	
}
