package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="customer_order_items")
public class CustomerOrderItems extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull(message = "Discount amount option cannot be blank")
	@ApiModelProperty(value = "discAmount", required = true, allowableValues = "Double")
	@Column(name = "disc_amount")
	private Double discAmount;
	
	@NotNull(message = "Order quantity cannot be blank")
	@ApiModelProperty(value = "orderQty", required = true, allowableValues = "Double")
	@Column(name = "order_quantity")
	private Double orderQty;
	
	@NotNull(message = "Product amount cannot be blank")
	@ApiModelProperty(value = "productAmount", required = true, allowableValues = "Double")
	@Column(name = "product_amount")
	private Double price;
	
	@NotNull(message = "Order quantity amount cannot be blank")
	@ApiModelProperty(value = "orderQuantityAmount", required = true, allowableValues = "Double")
	@Column(name = "order_quantity_amount")
	private Double orderQuantityAmount;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_order_id")
	private CustomerOrder customerOrderObj;
	
	@JsonIgnore
	@OneToOne()
	@JoinColumn(name = "product_uom_id")
	private UnitOfMeasurement productUom;
	
	@JsonIgnore
	@OneToOne()
	@JoinColumn(name = "product_dtl_id")
	private ProductDtl productDtl;
	
	
	@JsonIgnore
	@OneToOne()
	@JoinColumn(name = "product_id")
	private Product productObj;
	
	@NotNull(message = "product id cannot be null")
	@ApiModelProperty(value = "productId", required = true, allowableValues = "UUID")
	@Transient
	private UUID productId;
	
	public UUID getProductId() {
		if(null != productObj) {
			return productObj.getId();
		}
		return productId;
	
}
}
