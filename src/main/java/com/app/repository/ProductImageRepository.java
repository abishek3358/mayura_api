package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.ProductDTO;
import com.app.dto.ProductImageDTO;
import com.app.entity.Product;
import com.app.entity.ProductImage;
import com.app.enumeration.ProductImageType;
import com.app.service.ProductImageInterface;

public interface ProductImageRepository extends WriteableRepository<ProductImage, UUID> {

	List<ProductImage> findByProductImageTypeAndProductObj(ProductImageType productImageType,UUID uuid);
	
	List<ProductImage> findByProductObj(Product productObj);

	List<ProductImage> findByProductObj(Optional<Product> product);

	@Query(value="select file_name as fileName,product_id as productId,product_image_type as ProductImageType from product_images where product_id=:id",nativeQuery=true)
	List<ProductImageInterface> findByProductObj(String id);














	

	
	
	
}
