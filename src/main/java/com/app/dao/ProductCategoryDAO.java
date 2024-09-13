package com.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.ProductCategoryDTO;
import com.app.util.file.AWSS3DirectoryUtil;

@Repository
public class ProductCategoryDAO {
	@Autowired
	private EntityManager entityManager;

	public List<ProductCategoryDTO> getAllCategories() {

		String sqlQuery = "select pc.id as id,pc.name as name,pc.short_name as shortName,pc.description as description,"
				+ "pc.status as status,pc.product_type_id as productTypeId,pt.name as productTypeName,"
				+ "pc.filename as fileName from product_category pc "
				
				//+ "inner join product_type pt on pc.product_type_id=pt.id where pc.status='ACTIVE'";
				+ "inner join product_type pt on pc.product_type_id=pt.id "
		        + "ORDER BY pc.status";
		
		Query query = entityManager.createNativeQuery(sqlQuery);
		List<Object[]> objectList = query.getResultList();
     	ProductCategoryDTO Obj = null;
		List<ProductCategoryDTO> productCategoryDtoList = new ArrayList<ProductCategoryDTO>();
		for (Object[] columns : objectList) {
			Obj = new ProductCategoryDTO();
			Obj.setId(UUID.fromString(columns[0].toString()));
			Obj.setName(columns[1].toString());
			Obj.setShortName(columns[2].toString());
			Obj.setDescription(columns[3].toString());
			Obj.setStatus(columns[4].toString());
			Obj.setProductTypeId(UUID.fromString(columns[5].toString()));
			Obj.setProductTypeName(columns[6].toString());	
			String img = "default.jpg";
			if (columns[7] != null) {
				img = columns[7].toString();
			}
			Obj.setFileName(img);			
			Obj.setFileUrl(AWSS3DirectoryUtil.getCategoryImageUrl(Obj.getId(),img));
			productCategoryDtoList.add(Obj);
		}
		return productCategoryDtoList;
	}

}
