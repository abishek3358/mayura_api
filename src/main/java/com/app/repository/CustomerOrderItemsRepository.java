package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.CustomerOrder;
import com.app.entity.CustomerOrderItems;
import com.app.entity.Product;

public interface CustomerOrderItemsRepository extends WriteableRepository<CustomerOrderItems, UUID> {

	Optional<CustomerOrderItems> findById(UUID id);

	
	@Query(value="SELECT max(co.no_of_stars) FROM customer_order_items  coi inner join customer_order co on coi.customer_order_id =co.id where product_id=:product ",nativeQuery=true)
	Double findByNoofStars(Product product);

	@Query("SELECT COI FROM CustomerOrderItems COI WHERE COI.customerOrderObj = :customerOrder")
	List<CustomerOrderItems> findByOrderItemList(CustomerOrder customerOrder);
	
}
