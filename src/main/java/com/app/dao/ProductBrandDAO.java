package com.app.dao;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.BrandDTO;
import com.app.entity.Brand;
import com.app.entity.CustomerOrder;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.Status;
import com.app.util.TextUtil;
import com.app.util.file.AWSS3DirectoryUtil;

@Repository
public class ProductBrandDAO {

	@Autowired
	private EntityManager entityManager;

	public List<Brand> getAllBrand(String searchKey) {

		String sqlQuery = "SELECT id, name, description, file_name from brand where status = '" + Status.ACTIVE + "' ";

		if (!TextUtil.isEmptyOrNull(searchKey)) {
			sqlQuery = sqlQuery + " and name like  ?1 ";
		}

		sqlQuery = sqlQuery + " order by name asc ";

		Query query = entityManager.createNativeQuery(sqlQuery);

		if (!TextUtil.isEmptyOrNull(searchKey)) {
			query.setParameter(1, searchKey + "%");
		}

		List<Object[]> objectList = query.getResultList();

		Brand obj = null;
		List<Brand> brands = new ArrayList<Brand>();
		for (Object[] columns : objectList) {
			obj = new Brand();
			obj.setId(UUID.fromString(columns[0].toString()));
			obj.setBrandName(columns[1].toString());
			obj.setDescription(columns[2].toString());
			obj.setFileName(columns[3].toString());
			obj.setFileUrl(AWSS3DirectoryUtil.getBrandImageUrl(obj.getId(), obj.getFileName()));
			brands.add(obj);
		}
		return brands;
	}

	public List<BrandDTO> getBrandProdcuctsById(String brandId) {

		String sqlQuery = "SELECT count(p.id)as count,b.id as brandId,b.name as brandName,b.file_name FROM "
				+ "brand b inner join product p on b.id=p.brand_id ";
		if (!TextUtil.isEmptyOrNull(brandId)) {
			sqlQuery = sqlQuery + "where b.id= ?1 ";
		}
		sqlQuery = sqlQuery + " group by p.brand_id ";

		Query query = entityManager.createNativeQuery(sqlQuery);

		if (!TextUtil.isEmptyOrNull(brandId)) {
			query.setParameter(1, brandId);
		}
		List<Object[]> objectList = query.getResultList();
		BrandDTO obj = null;
		List<BrandDTO> brands = new ArrayList<BrandDTO>();
		for (Object[] columns : objectList) {
			obj = new BrandDTO();
			obj.setProductCount(columns[0].toString());
			obj.setId(UUID.fromString(columns[1].toString()));
			obj.setBrandName(columns[2].toString());
			obj.setFileName(columns[3].toString());
			obj.setFileUrl(AWSS3DirectoryUtil.getBrandImageUrl(obj.getId(), obj.getFileName()));
			brands.add(obj);

			String sqlQuery1 = "SELECT distinct(p.product_name) as productName,p.id as productId,dtl.sales_rate as salesRate,"
					+ "img.file_name as fileName,uom.name as uomName,co.no_of_stars as noOfStars,if(cfp.status ='ACTIVE','true','false') as favorite from brand b "
					+ "inner join product p on p.brand_id=b.id inner join product_dtl dtl on p.id=dtl.product_id "
					+ "left join customer_favorite_product cfp on cfp.product_id=p.id inner join product_images img on img.product_id=p.id "
					+ "inner join unit_of_measurement uom on dtl.product_uom_id=uom.id left join customer_order_items coi on p.id=coi.product_id "
					+ "left join customer_order co on co.id=coi.customer_order_id ";

			if (!TextUtil.isEmptyOrNull(brandId)) {
				sqlQuery1 = sqlQuery1 + "where b.id= ?1 ";
			}
			Query query1 = entityManager.createNativeQuery(sqlQuery1);

			if (!TextUtil.isEmptyOrNull(brandId)) {
				query1.setParameter(1, brandId);
			}
			List<Object[]> objectLists = query1.getResultList();
			AdminViewOrdersDTO object = null;
			List<AdminViewOrdersDTO> productList = new ArrayList<AdminViewOrdersDTO>();
			for (Object[] rows : objectLists) {
				object = new AdminViewOrdersDTO();
				object.setProductName(rows[0].toString());
				object.setProductId(UUID.fromString(rows[1].toString()));
				object.setSalesRate(Double.valueOf(rows[2].toString()));
				object.setFileName(rows[3].toString());
				object.setUomName(rows[4].toString());
				if (rows[5] != null) {
					object.setNoOfStars(Double.valueOf(rows[5].toString()));
				}
				object.setFileUrl(AWSS3DirectoryUtil.getProductImageUrl(object.getProductId(), object.getFileName()));
				object.setFavorite(Boolean.parseBoolean(rows[6].toString()));
				productList.add(object);
			}
			obj.setProductDtoList(productList);
		}

		return brands;

	}
}
