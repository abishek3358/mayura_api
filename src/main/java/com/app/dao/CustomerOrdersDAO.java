package com.app.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.AllocatedOrderSearchDTO;
import com.app.dto.CustomerOrderItemsDTO;
import com.app.dto.MyOrdersDTO;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.VendorOrderStatus;
import com.app.util.TextUtil;
import com.app.util.file.AWSS3DirectoryUtil;
import com.google.inject.internal.Nullable;

@Repository
public class CustomerOrdersDAO {

	@Autowired
	private EntityManager entityManager;

	public List<AdminViewOrdersDTO> findByConfirmedOrders(OrderStatus orderStatus) {

		String sqlQuery = "select   co.id  as orderId, co.order_no,c.full_name as customerName,"
				+ "date(co.created_on)as orderdate,co.no_of_items as itemsCount,"
				+ "concat(cda.address_line1,',',cda.address_line2,',',cda.location) as deliveryAddress,"
				+ "date(co.order_allocated_on) as orderAllocatedDate,v.name as vendorName,vom.vendor_order_status from customer_order co "
				+ "inner join customer c on c.id=co.customer_id "
				+ "inner join customer_delivery_address cda on cda.id=co.delivery_address_id "
				+ "inner join city cty on cty.id=cda.city_id " + "inner join state st on st.id= cty.state_id "
				+ "inner join country cry on cry.id=cty.country_id "
				+ "inner join vendor_order_mapping vom on vom.customer_id=c.id "
				+ "inner join vendor v on v.id=vom.vendor_id";

		if (OrderStatus.CONFIRMED.equals(orderStatus) || OrderStatus.PROGRESS.equals(orderStatus)
				|| OrderStatus.SHIPPED.equals(orderStatus) || OrderStatus.DELIVERED.equals(orderStatus)
				|| OrderStatus.COMPLETED.equals(orderStatus)) {

			if (!TextUtil.isNull(orderStatus)) {
				sqlQuery = sqlQuery + " where vom.vendor_order_status =?1";
			}
		}

		sqlQuery = sqlQuery + " order by co.created_on asc ";

		Query query = entityManager.createNativeQuery(sqlQuery);

		if (!TextUtil.isNull(orderStatus)) {
			query.setParameter(1, orderStatus.toString());
		}
		List<Object[]> objectList = query.getResultList();
		AdminViewOrdersDTO obj = null;
		List<AdminViewOrdersDTO> orders = new ArrayList<AdminViewOrdersDTO>();
		for (Object[] columns : objectList) {
			obj = new AdminViewOrdersDTO();
			// customerDeliveryAddressDTO=new CustomerDeliveryAddressDTO();
			obj.setCustomerOrderId(UUID.fromString(columns[0].toString()));
			obj.setOrderNo(columns[1].toString());
			obj.setCustomerName(columns[2].toString());
			obj.setOrderDate((Date) columns[3]);
			obj.setNoOfItem(Integer.valueOf(columns[4].toString()));
			if (columns[5] != null) {
				obj.setCustomerDeliveryAddress(columns[5].toString());
			}
			obj.setCustomerName(columns[2].toString());
			obj.setOrderDate((Date) columns[3]);
			obj.setNoOfItem(Integer.valueOf(columns[4].toString()));
			if (columns[5] != null) {
				obj.setCustomerDeliveryAddress(columns[5].toString());
			}
			obj.setAllocatedDate((java.util.Date) columns[6]);
			obj.setVendorName(columns[7].toString());
			obj.setVendorOrderStatus(columns[8].toString());

			orders.add(obj);
		}
		return orders;

	}

	public List<AdminViewOrdersDTO> findByCustomerConfirmedOrders(OrderStatus orderStatus) {

		String sqlQuery = "SELECT distinct(co.id) as orderId,co.order_no as orderNo,c.full_name as customerName,co.no_of_items as itemsCount,"
				+ "concat_ws(' ',cda.address_line1,cda.address_line2,cda.location) as deliveryAddress,co.created_on as orderDate,"
				+ "co.order_status as orderStatus,co.grand_total as grandTotal,co.payment_mode as paymentStatus FROM customer_order co "
				+ "inner join customer c on c.id=co.customer_id "
				+ "inner join customer_delivery_address cda on cda.id=co.delivery_address_id "
				+ "inner join customer_order_items coi on coi.customer_order_id=co.id where co.status='ACTIVE'";

		if (OrderStatus.CONFIRMED.equals(orderStatus)) {
			sqlQuery = sqlQuery + " and co.order_status=?1";
		}
		Query query = entityManager.createNativeQuery(sqlQuery);
		if (OrderStatus.CONFIRMED.equals(orderStatus)) {
			query.setParameter(1, orderStatus.toString());
		}
		List<Object[]> objectList = query.getResultList();
		AdminViewOrdersDTO obj = null;
		List<AdminViewOrdersDTO> myOrders = new ArrayList<AdminViewOrdersDTO>();
		for (Object[] columns : objectList) {
			obj = new AdminViewOrdersDTO();
			obj.setOrderId(UUID.fromString(columns[0].toString()));
			obj.setOrderNo(columns[1].toString());
			obj.setCustomerName(columns[2].toString());
			obj.setNoOfItem(Integer.valueOf(columns[3].toString()));
			if (columns[4] != null) {
				obj.setCustomerDeliveryAddress(columns[4].toString());
			}
			obj.setOrderDate((java.util.Date) columns[5]);
			obj.setOrderStatus(columns[6].toString());
			List<CustomerOrderItemsDTO> customerOrderItemsDTOList = findByOrderItems(
					UUID.fromString(columns[0].toString()));
			obj.setCustomerOrderItemsDTOList(customerOrderItemsDTOList);
			obj.setGrandTotal(Double.valueOf(columns[7].toString()));
			obj.setPaymentStatus(columns[8].toString());
			myOrders.add(obj);
		}
		return myOrders;

	}
	
	public List<AdminViewOrdersDTO> findByVendorRejectedOrders(VendorOrderStatus vendorOrderStatus) {

		String sqlQuery = "SELECT distinct(co.id) as orderId,co.order_no as orderNo, c.full_name as customerName,co.no_of_items as itemsCount,"
				+ "	concat_ws(' ',cda.address_line1,cda.address_line2,cda.location) as deliveryAddress,co.created_on as orderDate,"
				+ "	vom.vendor_order_status as orderStatus,co.grand_total as grandTotal,co.payment_mode as paymentStatus,vom.rejection_reason,"
				+ " v.id AS vendorId,v.name as vendorName "
				+ "	FROM customer_order co inner join customer c on c.id=co.customer_id "
				+ "	inner join customer_delivery_address cda on cda.id=co.delivery_address_id "
				+ "	inner join customer_order_items coi on coi.customer_order_id=co.id "
				+ "	inner join vendor_order_mapping vom on vom.customer_id=c.id "
				+ "	inner join vendor v on vom.vendor_id=v.id where vom.vendor_order_status='REJECTED'";

		if (VendorOrderStatus.REJECTED.equals(vendorOrderStatus)) {
			sqlQuery = sqlQuery + " and vom.vendor_order_status=?1";
		}
		Query query = entityManager.createNativeQuery(sqlQuery);
		if (VendorOrderStatus.REJECTED.equals(vendorOrderStatus)) {
			query.setParameter(1, vendorOrderStatus.toString());
		}
		List<Object[]> objectList = query.getResultList();
		AdminViewOrdersDTO obj = null;
		List<AdminViewOrdersDTO> myOrders = new ArrayList<AdminViewOrdersDTO>();
		for (Object[] columns : objectList) {
			obj = new AdminViewOrdersDTO();
			obj.setOrderId(UUID.fromString(columns[0].toString()));
			obj.setOrderNo(columns[1].toString());
			obj.setCustomerName(columns[2].toString());
			obj.setNoOfItem(Integer.valueOf(columns[3].toString()));
			if (columns[4] != null) {
				obj.setCustomerDeliveryAddress(columns[4].toString());
			}
			obj.setOrderDate((java.util.Date) columns[5]);
			obj.setOrderStatus(columns[6].toString());
			List<CustomerOrderItemsDTO> customerOrderItemsDTOList = findByOrderItems(
					UUID.fromString(columns[0].toString()));
			obj.setCustomerOrderItemsDTOList(customerOrderItemsDTOList);
			obj.setGrandTotal(Double.valueOf(columns[7].toString()));
			obj.setPaymentStatus(columns[8].toString());
			obj.setRejectionReason(columns[9].toString());
			obj.setVendorId(UUID.fromString(columns[10].toString()));
			obj.setVendorName(columns[11].toString());
			
			myOrders.add(obj);
		}
		return myOrders;

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

	public List<AdminViewOrdersDTO> findByAllOrderStatus(OrderStatus orderStatus, AllocatedOrderSearchDTO request) {

		/*
		 * String sqlQuery =
		 * "SELECT co.id as orderId,co.no_of_items as noOfItems,co.created_on as orderDate,"
		 * + "co.actual_delivery_date as allocatedDate,co.sub_total as subTotal," +
		 * "co.disc_amount as discount,co.delivery_fee as deliveryFee,co.grand_total as grandtotal,"
		 * + "co.location as location,coi.order_quantity as quantity," +
		 * "p.product_name as productName,b.name as brandName,dtl.sales_rate as prize,"
		 * +
		 * "uom.name as uomName,img.file_name as fileName,co.order_status as orderStatus "
		 * + "FROM customer_order co " + "inner join customer c on c.id=co.customer_id "
		 * + "inner join customer_order_items coi on coi.customer_order_id=co.id " +
		 * "inner join product p on coi.product_id=p.id " +
		 * "inner join brand b on b.id=p.brand_id " +
		 * "inner join product_dtl dtl on coi.product_dtl_id=dtl.id " +
		 * "inner join unit_of_measurement uom on coi.product_uom_id=uom.id " +
		 * "inner join product_images img on coi.product_id=img.product_id where co.status='ACTIVE'"
		 * ;
		 */
		String sqlQuery = "	SELECT co.id as orderId,\r\n"
				+ "       co.no_of_items as noOfItems,\r\n"
				+ "       co.created_on as orderDate,\r\n"
				+ "       co.actual_delivery_date as allocatedDate,\r\n"
				+ "       co.sub_total as subTotal,\r\n"
				+ "       co.disc_amount as discount,\r\n"
				+ "       co.delivery_fee as deliveryFee,\r\n"
				+ "       co.grand_total as grandtotal,\r\n"
				+ "       co.location as location,\r\n"
				+ "       coi.order_quantity as quantity,\r\n"
				+ "       p.product_name as productName,\r\n"
				+ "       b.name as brandName,\r\n"
				+ "       dtl.sales_rate as prize,\r\n"
				+ "       uom.name as uomName,\r\n"
				+ "       img.file_name as fileName,\r\n"
				+ "       co.order_status as orderStatus,\r\n"
				+ "       CASE WHEN fs.latest_id IS NULL THEN 0 ELSE 1 END AS status\r\n"
				+ "FROM customer_order co \r\n"
				+ "LEFT JOIN (SELECT order_id, MAX(id) AS latest_id FROM feedback_setting GROUP BY order_id) fs ON co.id = fs.order_id\r\n"
				+ "INNER JOIN customer c ON c.id = co.customer_id \r\n"
				+ "INNER JOIN customer_order_items coi ON coi.customer_order_id = co.id \r\n"
				+ "INNER JOIN product p ON coi.product_id = p.id\r\n"
				+ "INNER JOIN brand b ON b.id = p.brand_id \r\n"
				+ "INNER JOIN product_dtl dtl ON coi.product_dtl_id = dtl.id \r\n"
				+ "INNER JOIN unit_of_measurement uom ON coi.product_uom_id = uom.id \r\n"
				+ "INNER JOIN product_images img ON coi.product_id = img.product_id \r\n"
				+ "WHERE co.status = 'ACTIVE'";

		if (OrderStatus.ALLORDERS.equals(orderStatus)) {
			sqlQuery = sqlQuery + "";
		} else {
			sqlQuery = sqlQuery + "and co.order_status=?1";
		}
		Query query = entityManager.createNativeQuery(sqlQuery);
		if (OrderStatus.ALLORDERS.equals(orderStatus)) {
			sqlQuery = sqlQuery + "";
		} else {
			query.setParameter(1, orderStatus.toString());
		}
		List<Object[]> objectList = query.getResultList();
		AdminViewOrdersDTO obj = null;
		List<AdminViewOrdersDTO> myOrders = new ArrayList<AdminViewOrdersDTO>();
		for (Object[] columns : objectList) {
			obj = new AdminViewOrdersDTO();
			obj.setOrderId(UUID.fromString(columns[0].toString()));
			obj.setNoOfItem(Integer.valueOf(columns[1].toString()));
			obj.setOrderDate((java.util.Date) columns[2]);
			obj.setAllocatedDate((java.util.Date) columns[3]);
			obj.setSubTotal(Double.valueOf(columns[4].toString()));
			obj.setDiscAmount(Double.valueOf(columns[5].toString()));
			obj.setDeliveryFee(Double.valueOf(columns[6].toString()));
			obj.setGrandTotal(Double.valueOf(columns[7].toString()));
			obj.setLocation(columns[8].toString());
			obj.setOrderStatus(columns[15].toString());
			 obj.setStatus(((BigInteger) columns[16]).intValue() == 1);

			myOrders.add(obj);
		}
		return myOrders;

	}

	public List<AdminViewOrdersDTO> findByMyOrderStatus(OrderStatus orderStatus, UUID referenceId) {
		String sqlQuery = "SELECT  distinct co.id as orderId,co.no_of_items as noOfItems,co.created_on as orderDate,"
				+ "co.actual_delivery_date as deliveryDate,co.sub_total as subTotal,"
				+ "co.disc_amount as discount,co.delivery_fee as deliveryFee,co.grand_total as grandtotal,"
				+ "co.location as location,co.order_status as orderStatus,co.order_no as orderNo,cda.mobile_no as mobileNumber,c.full_name as customerName,cda.name,vom.vendor_id "
				+ "FROM customer_order co inner join customer c on c.id=co.customer_id "
				+ "inner join customer_delivery_address cda on co.delivery_address_id=cda.id "
				+ " inner JOIN vendor_order_mapping vom ON vom.customer_id=co.customer_id "
				+ "inner join customer_order_items coi on coi.customer_order_id=co.id where co.status='ACTIVE'";

		if (OrderStatus.ALLORDERS.equals(orderStatus)) {
			sqlQuery = sqlQuery + "";
		} else {
			sqlQuery = sqlQuery + " and co.order_status=:ordStatus";
		}
		if (referenceId != null) {
			sqlQuery = sqlQuery + " and co.customer_id =:refId";
		}

		Query query = entityManager.createNativeQuery(sqlQuery);

		if (OrderStatus.ALLORDERS.equals(orderStatus)) {
			sqlQuery = sqlQuery + "";
		} else {
			query.setParameter("ordStatus", orderStatus.toString());
		}
		if (referenceId != null) {
			query.setParameter("refId", referenceId.toString());
		}
		List<Object[]> objectList = query.getResultList();

		AdminViewOrdersDTO obj = null;
		List<AdminViewOrdersDTO> myOrders = new ArrayList<AdminViewOrdersDTO>();
		for (Object[] columns : objectList) {
			obj = new AdminViewOrdersDTO();
			obj.setOrderId(UUID.fromString(columns[0].toString()));
			obj.setNoOfItem(Integer.valueOf(columns[1].toString()));
			obj.setOrderDate((java.util.Date) columns[2]);
			obj.setDeliveryDate((java.util.Date) columns[3]);
			obj.setSubTotal(Double.valueOf(columns[4].toString()));
			obj.setDiscAmount(Double.valueOf(columns[5].toString()));
			obj.setDeliveryFee(Double.valueOf(columns[6].toString()));
			obj.setGrandTotal(Double.valueOf(columns[7].toString()));
			obj.setLocation(columns[8].toString());
			obj.setOrderStatus(columns[9].toString());
			obj.setOrderNo(columns[10].toString());
			List<MyOrdersDTO> myOrdersDtoList = myOrdersDtoList(obj.getOrderNo());
			obj.setMobileNumber(columns[11].toString());
			obj.setCustomerName(columns[12].toString());
		    obj.setReceiverName(columns[13].toString());
		    obj.setVendorId(UUID.fromString(columns[14].toString()));
			obj.setMyOrdersDtoList(myOrdersDtoList);
			myOrders.add(obj);
		}
		return myOrders;
	}

	private List<MyOrdersDTO> myOrdersDtoList(String orderNo) {
		String sqlQuery1 = "select distinct(co.order_no) as orderNo,coi.order_quantity as orderQty,p.product_name as productName,b.name as brandName,"
				+ "dtl.sales_rate as price,p.id as productId,uom.name as uomName,img.file_name as fileName,"
				+ "co.order_status as orderStatus from customer_order co "
				+ "inner join customer_order_items coi on co.id=coi.customer_order_id "
				+ "inner join product p on coi.product_id=p.id " + "inner join brand b on b.id=p.brand_id "
				+ "inner join product_dtl dtl on coi.product_dtl_id=dtl.id "
				+ "inner join unit_of_measurement uom on coi.product_uom_id=uom.id "
				+ "inner join product_images img on coi.product_id=img.product_id where co.status='ACTIVE'";

		if (!TextUtil.isEmptyOrNull(orderNo)) {
			sqlQuery1 = sqlQuery1 + " and co.order_no =:orderNo";
		}
		Query query2 = entityManager.createNativeQuery(sqlQuery1);

		if (!TextUtil.isEmptyOrNull(orderNo)) {
			query2.setParameter("orderNo", orderNo.toString());
		}

		List<Object[]> objectLists = query2.getResultList();

		MyOrdersDTO objects = null;
		List<MyOrdersDTO> myOrdersList = new ArrayList<MyOrdersDTO>();
		for (Object[] columns : objectLists) {
			objects = new MyOrdersDTO();
			objects.setOrderNo(columns[0].toString());
			objects.setOrderQty(Double.valueOf(columns[1].toString()));
			objects.setProductName(columns[2].toString());
			objects.setBrandName(columns[3].toString());
			objects.setSalesRate(Double.valueOf(columns[4].toString()));
			objects.setProductId(UUID.fromString(columns[5].toString()));
			objects.setUomName(columns[6].toString());
			objects.setFileName(columns[7].toString());
			objects.setFileUrl(AWSS3DirectoryUtil.getProductImageUrl(objects.getProductId(), objects.getFileName()));
			objects.setOrderStatus(columns[8].toString());
			myOrdersList.add(objects);
		}
		return myOrdersList;

	}

	public List<AdminViewOrdersDTO> findByProgressingOrders(OrderStatus orderStatus, @Nullable String vendorName,
			@Nullable Date startDate, @Nullable Date endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDay = null, toDay = null;
		if (null != startDate && null != endDate) {
			fromDay = formatter.format(startDate);
			toDay = formatter.format(endDate);
		}

		String sqlQuery = "	SELECT a.orderNo,a.orderdate,a.allocatedDate,a.customerName,a.vendorId,a.userId,a.vendorname,a.delivery_address,"
				+ "	a.vendorOrderStatus,a.customerOrderId ,a.shippedDate FROM (select co.order_no as orderNo,"
				+ "	vom.vendor_order_status as vendorOrderStatus,co.created_on as orderdate,"
				+ "	co.order_allocated_on as allocatedDate,date(co.order_shipped_on) as shippedDate,"
				+ "	c.full_name as customerName,vom.vendor_id AS vendorId,u.user_id AS userId,v.name as vendorname,co.id as customerOrderId,"
				+ "	concat_ws(' ',cda.address_line1,cda.address_line2,cda.postal_code) as delivery_address "
				+ "	from customer_order co "
				+ "	inner join vendor_order_mapping vom on vom.customer_order_id =co.id "
				+ "	inner join customer c on co.customer_id = c.id "
				+ "	inner join vendor v on v.id = vom.vendor_id "
				+ "	inner join users u ON u.reference_id = v.id "
				+ "	left join customer_delivery_address cda on co.delivery_address_id = cda.id ";

		if (orderStatus.equals(OrderStatus.ALLOCATED) || orderStatus.equals(OrderStatus.PROGRESS)
				|| orderStatus.equals(OrderStatus.SHIPPED)) {
			if (orderStatus.toString() != null) {
				sqlQuery = sqlQuery + " where vom.vendor_order_status =:orderStatus)a ";
			}
		} else {
			sqlQuery = sqlQuery + " )a";
		}
		sqlQuery = sqlQuery + "where 1=1 ";

		if (vendorName != null) {
			sqlQuery = sqlQuery + "and a.vendorname like :vendor ";
		}
		if (fromDay != null) {
			sqlQuery = sqlQuery +"and a.orderdate>=:startDate ";
		}
		if (toDay != null) {
			sqlQuery = sqlQuery +"and a.orderdate<=:endDate ";
		}
		Query query = entityManager.createNativeQuery(sqlQuery);

		if (orderStatus.toString() != null) {
			query.setParameter("orderStatus", orderStatus.toString());
		}
		if (fromDay != null) {
			query.setParameter("startDate", fromDay);
		}
		if (toDay != null) {
			query.setParameter("endDate", toDay);
		}
		if (!TextUtil.isEmptyOrNull(vendorName)) {
			query.setParameter("vendor", "%" + vendorName + "%");
		}
		List<Object[]> objectList = query.getResultList();

		List<AdminViewOrdersDTO> orders = new ArrayList<AdminViewOrdersDTO>();
		for (Object[] columns : objectList) {
			AdminViewOrdersDTO obj = new AdminViewOrdersDTO();
			// customerDeliveryAddressDTO=new CustomerDeliveryAddressDTO();
			obj.setOrderNo(columns[0].toString());
			obj.setOrderDate((java.util.Date) columns[1]);
			obj.setAllocatedDate((java.util.Date) columns[2]);
			obj.setCustomerName((String) columns[3]);
			obj.setVendorId(UUID.fromString(columns[4].toString()));
			obj.setUserId(UUID.fromString(columns[5].toString()));
			obj.setVendorName((String) columns[6]);
			if (columns[7] != null) {
				obj.setCustomerDeliveryAddress(columns[7].toString());
			}
			obj.setVendorOrderStatus(columns[8].toString());
			obj.setCustomerOrderId(UUID.fromString(columns[9].toString()));
			if (columns[10] != null) {
				obj.setOrderShippedDate((java.util.Date) columns[10]);
			}
			orders.add(obj);
		}
		return orders;
	}

	public List<AdminViewOrdersDTO> findByDeliveredOrders(OrderStatus orderStatus, AllocatedOrderSearchDTO request) {
		String sqlQuery = "select a.orderNo,a.vendorOrderStatus,a.orderdate,a.deliveryDate,a.allocatedDate,a.customerName,a.vendorId,a.vendorname,"
				+ "	a.customerOrderId from (select co.order_no as orderNo,vom.vendor_order_status as vendorOrderStatus,"
				+ "	date(co.created_on) as orderdate,date(co.order_delivered_on) as deliveryDate,date(co.order_allocated_on) as allocatedDate,"
				+ "	c.full_name as customerName,v.name as vendorname,co.id as customerOrderId ,v.id AS vendorId\r\n"
				+ "	from customer_order co "
				+ "	inner join vendor_order_mapping vom on vom.customer_order_id =co.id "
				+ "	inner join customer c on co.customer_id = c.id "
				+ "	inner join vendor v on v.id = vom.vendor_id ";

		if (OrderStatus.CONFIRMED.equals(orderStatus) || OrderStatus.PROGRESS.equals(orderStatus)
				|| OrderStatus.SHIPPED.equals(orderStatus) || OrderStatus.DELIVERED.equals(orderStatus)
				|| OrderStatus.COMPLETED.equals(orderStatus)) {
			if (!TextUtil.isNull(orderStatus)) {
				sqlQuery = sqlQuery + " where vom.vendor_order_status =:orderStatus)a ";
			}
		} else {
			sqlQuery = sqlQuery + " ) a";
		}
		sqlQuery = sqlQuery + "where 1=1 ";
		if (!TextUtil.isEmptyOrNull(request.getVendorName())) {
			sqlQuery = sqlQuery + "and a.vendorname like :vendor ";
		}
		if (!TextUtil.isNull(request.getStartDate()) || !TextUtil.isNull(request.getEndDate())) {
			sqlQuery += "and a.orderdate between :startDate and :endDate ";
		}

		Query query = entityManager.createNativeQuery(sqlQuery);

		if (OrderStatus.CONFIRMED.equals(orderStatus) || OrderStatus.PROGRESS.equals(orderStatus)
				|| OrderStatus.DELIVERED.equals(orderStatus) || OrderStatus.COMPLETED.equals(orderStatus)) {

			if (!TextUtil.isNull(orderStatus)) {
				query.setParameter("orderStatus", orderStatus.toString());
			}
		}
		if (!TextUtil.isNull(request.getStartDate())) {
			query.setParameter("startDate", request.getStartDate(), TemporalType.DATE);
		}
		if (!TextUtil.isNull(request.getEndDate())) {
			query.setParameter("endDate", request.getEndDate(), TemporalType.DATE);

		}
		if (!TextUtil.isEmptyOrNull(request.getVendorName())) {
			query.setParameter("vendor", "%" + request.getVendorName() + "%");
		}

		List<Object[]> objectList = query.getResultList();

		List<AdminViewOrdersDTO> orders = new ArrayList<AdminViewOrdersDTO>();
		for (Object[] columns : objectList) {
			AdminViewOrdersDTO obj = new AdminViewOrdersDTO();
			obj.setOrderNo(columns[0].toString());
			obj.setVendorOrderStatus(columns[1].toString());
			obj.setOrderDate((java.util.Date) columns[2]);
			obj.setOutForDelivery((java.util.Date) columns[3]);
			obj.setAllocatedDate((java.util.Date) columns[4]);

			long time_difference = obj.getOutForDelivery().getTime() - obj.getOrderDate().getTime();

			long days_difference = (time_difference / (1000 * 60 * 60 * 24)) % 365;
			long seconds_difference = (time_difference / 1000) % 60;
			long minutes_difference = (time_difference / (1000 * 60)) % 60;
			long hours_difference = (time_difference / (1000 * 60 * 60)) % 24;

			obj.setDeliveryDuration(
					days_difference + "Days :" + hours_difference + " HH :" + minutes_difference + " MM");
			obj.setCustomerName((String) columns[5]);
			obj.setVendorId(UUID.fromString(columns[6].toString()));
			obj.setVendorName((String) columns[7]);
			obj.setCustomerOrderId(UUID.fromString(columns[8].toString()));
			orders.add(obj);
		}
		return orders;

	}

}
