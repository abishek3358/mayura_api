
package com.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.AllProductDTO;
import com.app.dto.FilterDTO;
import com.app.dto.FilterProductDTO;
import com.app.dto.FilterProductDTO.FilterProduct;
import com.app.dto.ProductRequestDTO;
import com.app.enumeration.ProductDiscountType;
import com.app.enumeration.SortingType;
import com.app.util.TextUtil;
import com.app.util.file.AWSS3DirectoryUtil;

@Repository
public class ProductDAO {

	@Autowired
	private EntityManager entityManager;

	public List<AllProductDTO> findByProductSearch(ProductRequestDTO request) {
		String sqlQuery = "SELECT DISTINCT a.productId,a.productName,a.uomName,a.discountType,a.product_price,"
				+ "a.product_rating,a.product_image,a.productDtlId,a.product_uom_id,a.favorite,a.brandName,a.productdiscount,a.description,a.tags,a.productCategoryId,a.brandId,a.product_code FROM (select p.id as productId,"
				+ "p.product_name as productName,p.product_code,p.description as description,p.tags as tags,pdtl.disc_type as discountType,pdtl.product_uom_id,"
				+ "uom.short_name as uomName,pc.id as productCategoryId,b.id as brandId, b.name as brandName,"
				+ "if(cfp.status ='ACTIVE','true','false') as favorite,(select if(max(dtl.discount) is null,0,max(dtl.discount)) from product_dtl dtl "
				+ "inner join product p on p.id=dtl.product_id) as productdiscount,"
				+ "pc.name as categoryName,(select if(round(max(co.no_of_stars)) is null ,0,round(max(co.no_of_stars))) "
				+ "from customer_order_items coi inner join customer_order co on coi.customer_order_id =co.id where "
				+ "product_id=p.id) as product_rating ,pdtl.sales_rate as product_price, (select file_name from  "
				+ "product_images  where product_id=p.id and product_image_type='LISTING' limit 1) as product_image,"
				+ "pdtl.id as productDtlId,pdtl.product_uom_id as uomId,p.product_code as productCode from product p inner join  product_category pc "
				+ "on pc.id=p.product_category_id inner join product_dtl pdtl on pdtl.product_id=p.id "
				+ "left join customer_favorite_product cfp on cfp.product_id=pdtl.id "
				+ "inner join unit_of_measurement uom on uom.id=pdtl.product_uom_id inner join brand b on b.id=p.brand_id ";

		if (!TextUtil.isEmptyOrNull(request.getSearchKey())) {
			sqlQuery = sqlQuery + " where pc.name like :search or p.product_name like :search or b.name like :search)a";
		} else if (request.getCategoryId() != null) {
			sqlQuery = sqlQuery + " where pc.id like :search)a";
		} else if (request.getBrandId() != null) {
			sqlQuery = sqlQuery + " where b.id like :search)a";
		}
		sqlQuery = sqlQuery + " where 1=1 ";

		if (request.getBrandList() != null && !request.getBrandList().isEmpty()) {
			sqlQuery = sqlQuery + " and a.brandId in(:brandId)";
		}
		if (request.getCategoryList() != null && !request.getCategoryList().isEmpty()) {
			sqlQuery = sqlQuery + " and a.productCategoryId in(:categoryId)";
		}
		if (request.getRatingList() != null && !request.getRatingList().isEmpty()) {
			sqlQuery = sqlQuery + " and a.product_rating in (:ratings)";
		}
		if (!TextUtil.isEmptyOrNull(request.getMinPrice())) {
			sqlQuery = sqlQuery + " and a.product_price >= :minPrice";
		}
		if (!TextUtil.isEmptyOrNull(request.getMaxPrice())) {
			sqlQuery = sqlQuery + " and a.product_price <= :maxPrice";
		}
		if (request.getShowItemsCount() != null) {
			sqlQuery = sqlQuery + " order by a.productName limit :showItem ";
		} else {
			sqlQuery = sqlQuery + " order by a.productName";
		}

		Query query = entityManager.createNativeQuery(sqlQuery);

		if (!TextUtil.isEmptyOrNull(request.getSearchKey())) {
			query.setParameter("search", "%" + request.getSearchKey() + "%");
		} else if (!TextUtil.isEmptyOrNull(request.getCategoryId())) {
			query.setParameter("search", request.getCategoryId().toString());
		} else if (!TextUtil.isEmptyOrNull(request.getBrandId())) {
			query.setParameter("search", request.getBrandId().toString());
		}
		if (request.getCategoryList() != null && !request.getCategoryList().isEmpty()) {
			List<String> categorylst = new ArrayList<>();
			for (UUID obj : request.getCategoryList()) {
				categorylst.add(obj.toString());
			}
			query.setParameter("categoryId", categorylst);
		}
		if (request.getBrandList() != null && !request.getBrandList().isEmpty()) {
			List<String> brandlst = new ArrayList<>();
			for (UUID obj : request.getBrandList()) {
				brandlst.add(obj.toString());
			}
			query.setParameter("brandId", brandlst);
		}
		if (request.getRatingList() != null && !request.getRatingList().isEmpty()) {
			List<String> gradeList = new ArrayList<>();
			for (Double obj : request.getRatingList()) {
				gradeList.add(obj.toString());
			}
			query.setParameter("ratings", gradeList);
		}
		if (!TextUtil.isEmptyOrNull(request.getMinPrice())) {
			query.setParameter("minPrice", request.getMinPrice().toString());
		}
		if (!TextUtil.isEmptyOrNull(request.getMaxPrice())) {
			query.setParameter("maxPrice", request.getMaxPrice().toString());
		}
		if (!TextUtil.isEmptyOrNull(request.getShowItemsCount())) {
			query.setParameter("showItem", request.getShowItemsCount());
		}
		List<Object[]> objectList = query.getResultList();
		AllProductDTO obj = null;
		List<AllProductDTO> allProductList = new ArrayList<AllProductDTO>();
		for (Object[] columns : objectList) {
			obj = new AllProductDTO();
			obj.setId(UUID.fromString(columns[0].toString()));
			obj.setProductName(columns[1].toString());
			obj.setUomName(columns[2].toString());
			String discType = null;
			boolean discOption = true;
			if (ProductDiscountType.valueOf(columns[3].toString()).equals(ProductDiscountType.FLAT)) {
				discType = "₹";
			} else if (ProductDiscountType.valueOf(columns[3].toString()).equals(ProductDiscountType.PERCENTAGE)) {
				discType = "%";
			} else {
				discType = "";
				discOption = false;
			}
			obj.setPrice(Double.valueOf(columns[4].toString()));
			obj.setPriceWithUom("₹ " + columns[4].toString() + "/" + columns[2].toString());
			obj.setNoOfStars(Double.valueOf(columns[5].toString()));
			String img = "default.jpg";
			if (columns[6] != null) {
				img = columns[6].toString();
			}
			obj.setFileName(img);
			obj.setFileUrl(AWSS3DirectoryUtil.getProductImageUrl(UUID.fromString(columns[0].toString()), img));
			obj.setProductDtlId(UUID.fromString(columns[7].toString()));
			obj.setUomId(UUID.fromString(columns[8].toString()));
			obj.setFavorite(Boolean.parseBoolean(columns[9].toString()));
			obj.setBrandName(columns[10].toString());
			if (columns[11].toString() == null) {
				obj.setDiscAmount(Double.valueOf(0.00));
			}
			obj.setDiscAmount(Double.valueOf(columns[11].toString()));
			obj.setDiscValue(discOption ? columns[11].toString() + " " + discType : "");
			obj.setDiscOption(discOption);		
			obj.setDescription(columns[12] != null ?  columns[12].toString():null);						
		    obj.setTag(columns[13].toString() != null ? columns[13].toString():null);
		    obj.setProductCode(columns[16].toString());
			String tagName = obj.getTag();
			String[] tags = null;
			tags = tagName.split(",");
			List<String> tagslist = new ArrayList<>();
			if (tags != null) {
				for (String tagname : tags) {
					tagslist.add(tagname);
				}
			}
			obj.setTagsList(tagslist);
			allProductList.add(obj);
		}
		return allProductList;
	}

	public FilterProductDTO findByFilterProduct(ProductRequestDTO request) {

		String sqlQuery = "select pc.id as productCategoryId,b.id as brandId, b.name as brandName,"
				+ "	pc.name as categoryName,(select if(round(max(co.no_of_stars)) is null ,0,round(max(co.no_of_stars))) "
				+ "	from customer_order_items coi inner join customer_order co on coi.customer_order_id =co.id where "
				+ "	coi.product_id=p.id) as product_rating from product p inner join  product_category pc"
				+ " on pc.id=p.product_category_id inner join brand b on b.id=p.brand_id "
				+ "inner join product_dtl pdtl on pdtl.product_id=p.id "
				+ "inner join unit_of_measurement uom on pdtl.product_uom_id=uom.id";

		if (!TextUtil.isEmptyOrNull(request.getSearchKey())) {
			sqlQuery = sqlQuery
					+ " where pc.name like :search or p.product_name  like :search or b.name   like :search ";
		} else if (!TextUtil.isEmptyOrNull(request.getCategoryId())) {
			sqlQuery = sqlQuery + " where pc.id=:search";
		} else if (!TextUtil.isEmptyOrNull(request.getBrandId())) {
			sqlQuery = sqlQuery + " where b.id=:search";
		}
		Query query = entityManager.createNativeQuery(sqlQuery);
		if (!TextUtil.isEmptyOrNull(request.getSearchKey())) {
			query.setParameter("search", "%" + request.getSearchKey() + "%");
		} else if (!TextUtil.isEmptyOrNull(request.getCategoryId())) {
			query.setParameter("search", request.getCategoryId().toString());
		} else if (!TextUtil.isEmptyOrNull(request.getBrandId())) {
			query.setParameter("search", request.getBrandId().toString());
		}
		else {
			query.setParameter("search",  "''");
		}
		List<Object[]> objectList = query.getResultList();
		List<FilterDTO> filterList = new ArrayList<FilterDTO>();
		FilterDTO filterobj = null;
		for (Object[] columns : objectList) {
			filterobj = new FilterDTO();
			filterobj.setCategoryId(UUID.fromString(columns[0].toString()));
			filterobj.setBrandId(UUID.fromString(columns[1].toString()));
			filterobj.setBrandName(columns[2].toString());
			filterobj.setCategoryName(columns[3].toString());
			filterobj.setNoOfStars(Double.valueOf(columns[4].toString()));
			filterList.add(filterobj);
		}
		FilterProductDTO filterProductDtoObj = new FilterProductDTO();

		List<FilterProduct> filterProductCategoryList = new ArrayList<FilterProduct>();
		List<FilterProduct> filterProductBrandList = new ArrayList<FilterProduct>();
//		List<FilterProduct> filterProductTagList = new ArrayList<FilterProduct>();
		List<FilterProduct> filterProductNoofStarList = new ArrayList<FilterProduct>();

		Map<UUID, List<FilterDTO>> categoryMap = filterList.stream().collect(Collectors.groupingBy(i -> i.getCategoryId()));
		for (UUID categoryObj : categoryMap.keySet()) {
			
			FilterProductDTO.FilterProduct categoryProductObj = new FilterProductDTO.FilterProduct();
			Integer categoryCount = categoryMap.get(categoryObj).size();
			String categoryName = categoryMap.get(categoryObj).get(0).getCategoryName();
			categoryProductObj.setId(categoryObj);
			categoryProductObj.setName(categoryName);
			categoryProductObj.setItemCount(categoryCount);
			filterProductCategoryList.add(categoryProductObj);
		}

		Map<UUID, List<FilterDTO>> brandMap = filterList.stream().collect(Collectors.groupingBy(i -> i.getBrandId()));
		for (UUID brandObj : brandMap.keySet()) {

			FilterProductDTO.FilterProduct brandProductObj = new FilterProductDTO.FilterProduct();
			String brandName = brandMap.get(brandObj).get(0).getBrandName();
			Integer brandCount = brandMap.get(brandObj).size();
			brandProductObj.setId(brandObj);
			brandProductObj.setName(brandName);
			brandProductObj.setItemCount(brandCount);
			filterProductBrandList.add(brandProductObj);
		}

		Map<Double, List<FilterDTO>> ratingMap = filterList.stream()
				.collect(Collectors.groupingBy(i -> i.getNoOfStars()));
		for (Double ratingObj : ratingMap.keySet()) {

			FilterProductDTO.FilterProduct ratingProductObj = new FilterProductDTO.FilterProduct();
			Integer starCount = ratingMap.get(ratingObj).size();
			ratingProductObj.setName(ratingObj.toString());
			ratingProductObj.setItemCount(starCount);
			filterProductNoofStarList.add(ratingProductObj);
		}
		filterProductDtoObj.setRatingList(filterProductNoofStarList);
		filterProductDtoObj.setProductCategoryList(filterProductCategoryList);
		filterProductDtoObj.setBrandList(filterProductBrandList);

		if (request.getCategoryId() != null) {
			filterProductDtoObj.setCategoryFlag(false);
		}
		if (request.getBrandId() != null) {
			filterProductDtoObj.setBrandFlag(false);
		}
		return filterProductDtoObj;
	}

}
