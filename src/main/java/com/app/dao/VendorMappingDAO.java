package com.app.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.CustomerOrderItemsDTO;
import com.app.dto.VendorOrdersDTO;
import com.app.enumeration.VendorOrderStatus;
import com.app.util.TextUtil;

@Repository
public class VendorMappingDAO {

	@Autowired
	private EntityManager entityManager;

	public List<VendorOrdersDTO> findByVendorOrders(VendorOrderStatus orderStatus) {

		String sqlQuery = "SELECT distinct co.id,co.order_no,co.grand_total,co.payment_mode,co.no_of_items,co.order_allocated_on ,vom.vendor_id ,u.user_id ,"
				+ "	c.full_name, vom.rejection_reason ,vom.vendor_order_status ,concat_ws(cda.address_line1,',',cda.address_line2,',',cda.location) as deliveryAddress FROM "
				+ "	vendor_order_mapping vom inner join customer c on c.id=vom.customer_id "
				+ "	inner join vendor v ON v.id = vom.vendor_id "
				+ "	inner join users u ON u.reference_id = v.id "
				+ "	inner join customer_order co on co.id=vom.customer_order_id "
				+ "	inner join customer_delivery_address cda on cda.id=co.delivery_address_id where vom.vendor_order_status='"+orderStatus+"'";


		Query query = entityManager.createNativeQuery(sqlQuery);

		List<Object[]> objectList = query.getResultList();
		VendorOrdersDTO obj = null;
		List<VendorOrdersDTO> responceList = new ArrayList<VendorOrdersDTO>();
		for (Object[] columns : objectList) {
			obj = new VendorOrdersDTO();
			obj.setCustomerOrderId(UUID.fromString(columns[0].toString()));
			obj.setCustomerOrderNo(columns[1].toString());
			obj.setGrantTotal(Double.valueOf(columns[2].toString()));
			obj.setPaymentStatus(columns[3].toString());
			obj.setNoOfItems(Integer.parseInt(columns[4].toString()));
			obj.setAllocatedDate((Date) columns[5]);
			obj.setVendorId(UUID.fromString(columns[6].toString()));
			obj.setUserId(UUID.fromString(columns[7].toString()));
			obj.setCustomerName(columns[8].toString());
			obj.setRejectionReason(columns[9] !=null? columns[9].toString():null);
			obj.setVendorOrderStatus(columns[10].toString());
			obj.setDeliveryAddress(columns[11].toString());
			List<CustomerOrderItemsDTO> customerOrderItemsDTOList = findByOrderItems(
					UUID.fromString(columns[0].toString()));
			obj.setCustomerOrderItemsDTOList(customerOrderItemsDTOList);
			responceList.add(obj);
		}
		return responceList;
	}

	private List<CustomerOrderItemsDTO> findByOrderItems(UUID customerOrderId) {

		String sqlQuery = "SELECT p.product_name as productName,b.name as brandName,pc.name as productCategoryName,uom.short_name as uomName,"
				+ "coi.order_quantity as qty ,coi.product_amount as price,coi.order_quantity_amount as orderQuantityAmmount "
				+ "FROM customer_order co inner join customer_order_items coi on co.id=coi.customer_order_id "
				+ "inner join product p on p.id=coi.product_id inner join brand b on b.id=p.brand_id "
				+ "inner join product_category pc on pc.id=p.product_category_id inner join product_dtl dtl on dtl.id=coi.product_dtl_id "
				+ "inner join unit_of_measurement uom on uom.id=dtl.product_uom_id";

		if (!TextUtil.isEmptyOrNull(customerOrderId)) {
			sqlQuery = sqlQuery + " where co.id=:customerOrderId";
		}
		Query query = entityManager.createNativeQuery(sqlQuery);

		if (!TextUtil.isEmptyOrNull(customerOrderId)) {
			query.setParameter("customerOrderId", customerOrderId.toString());

		}
		List<Object[]> objectList = query.getResultList();
		CustomerOrderItemsDTO obj = null;
		List<CustomerOrderItemsDTO> responceList = new ArrayList<CustomerOrderItemsDTO>();
		for (Object[] columns : objectList) {
			obj = new CustomerOrderItemsDTO();
			obj.setProductName(columns[0].toString());
			obj.setBrandName(columns[1].toString());
			obj.setProductCategoryName(columns[2].toString());
			obj.setOrderQty(Double.valueOf(columns[4].toString()));
			obj.setPrice(Double.valueOf(columns[5].toString()));
			obj.setPriceWithUom(Double.valueOf(columns[5].toString()) + "/" + columns[3].toString());
			obj.setOrderQtyAmount(Double.valueOf(columns[6].toString()));
			responceList.add(obj);
		}
		return responceList;
	}

}
