package com.app.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.MyOrdersDTO;
import com.app.entity.Customer;
import com.app.entity.CustomerOrder;
import com.app.enumeration.OrderStatus;

public interface CustomerOrderRepository extends WriteableRepository<CustomerOrder, UUID> {

	List<CustomerOrder> findByCustomerObj(Customer customer);
	
	List<CustomerOrder> findByOrderStatus(OrderStatus orderStatus);
	
	
	
	@Query(value="select * from customer_order where id=:customerOrderId",nativeQuery=true)
	Optional<CustomerOrder> getById(UUID customerOrderId);
	
	//@Query("SELECT a FROM PurchaseOrderFileupload a WHERE a.purchaseOrderId = :purchaseOrderId")
	
	@Query("select co from CustomerOrder co where co.grandTotal>=:minimumOrderAmount AND co.customerObj=:customerObj")
	List<CustomerOrder> findByOrder(Double minimumOrderAmount, Customer customerObj);

	@Query(value="select * from customer_order where customer_id=:referenceId",nativeQuery=true)
	List<CustomerOrder> findByOrders(UUID referenceId);

	@Query("select co from CustomerOrder co where co.grandTotal>=:minimumOrderAmount AND co.customerObj=:customerObj and co.orderStatus=:confirmed and offerStatus=:offerStatus ")
	List<CustomerOrder> findByComfirmOrder(Double minimumOrderAmount, Customer customerObj, OrderStatus confirmed, Boolean offerStatus);

	@Query(value="select * from customer_order where id=:customerOrderId",nativeQuery=true)
	Optional<CustomerOrder> getByCustomerOrderId(String customerOrderId);

//	@Query(value="SELECT co.id as orderId,co.no_of_items as noOfItems,co.created_on as orderDate,"
//			+ "co.actual_delivery_date as allocatedDate,co.sub_total as subTotal,co.disc_amount as discount,"
//			+ "co.delivery_fee as deliveryFee,co.grand_total as grandtotal,co.location as location,"
//			+ "coi.order_quantity as quantity FROM "
//			+ "customer_order co inner join customer c on c.id=co.customer_id inner join "
//			+ "customer_order_items coi on coi.customer_order_id=co.id where co.order_status =:orderStatus and "
//			+ "co.customer_id =:referenceId",nativeQuery=true)
//	List<AdminViewOrdersDTO> findByMyOrderStatus(String orderStatus, String referenceId);

	
//	Optional<MyOrdersDTO> findByOrderStatus(UUID orderStatus, UUID orderStatus2);
	
	

}