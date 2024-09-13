package com.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.OrderStatus;
import com.app.enumeration.PaymentMode;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name="customer_order")

public class CustomerOrder extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "order_no")
	private String orderNo;

	@ApiModelProperty(value = "Valid Payment Mode", required = true, allowableValues = "CASH_ON_DELIVERY")
	@Enumerated(EnumType.STRING)
	@Column(name="payment_mode")
	private PaymentMode paymentMode ;
	
	@Enumerated(EnumType.STRING)
	@Column(name="order_status")
	private OrderStatus orderStatus ;

	@NotNull(message = "Delivery fee cannot be blank")
	@ApiModelProperty(value = "deliveryFee", required = true, allowableValues = "Double")
	@Column(name = "delivery_fee")
	private Double deliveryFee;

	@NotNull(message = "Number of products cannot be blank")
	@ApiModelProperty(value = "noOfProducts", required = true, allowableValues = "Integer")
	@Column(name = "no_of_items")
	private Integer noOfProducts;
	
	@NotNull(message = "Subtotal cannot be blank")
	@ApiModelProperty(value = "subTotal", required = true, allowableValues = "Double")
	@Column(name = "sub_total")
	private Double subTotal;
	
	@NotNull(message = "Grand total cannot be blank")
	@ApiModelProperty(value = "grandTotal", required = true, allowableValues = "Double")
	@Column(name = "grand_total")
	private Double grandTotal;
	
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	@ApiModelProperty(value = "actualDeliveryDate", required = true, allowableValues = "Date")
	@Column(name = "actual_delivery_date")
	private Date actualDeliveryDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	@ApiModelProperty(value = "estimatedDeliveryDate", required = true, allowableValues = "Date")
	@Column(name = "estimated_delivery_date")
	private Date estimatedDeliveryDate;
	
	@ApiModelProperty(value = "notes", required = true, allowableValues = "String")
	@Column(name = "notes")
	private String notes;
	
	@ApiModelProperty(value = "noOfStars", required = true, allowableValues = "Double")
	@Column(name = "no_of_stars")
	private Double noOfStars;
	
	@ApiModelProperty(value = "reviews", required = true, allowableValues = "String")
	@Column(name = "reviews")
	private String reviews;
	
	@Column(name = "reason")
	private String reason;
	
	@ApiModelProperty(value = "discAmount", required = true, allowableValues = "Double")
	@Column(name = "disc_amount")
	private Double discAmount;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	@ApiModelProperty(value = "orderAllocateDate", required = true, allowableValues = "Date")
	@Column(name = "order_allocated_on")
	private Date orderAllocatedDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	@ApiModelProperty(value = "orderShippedDate", required = true, allowableValues = "Date")
	@Column(name = "order_shipped_on")
	private Date orderShippedDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	@ApiModelProperty(value = "orderDeliveredDate", required = true, allowableValues = "Date")
	@Column(name = "order_delivered_on")
	private Date orderDeliveredDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	@ApiModelProperty(value = "ordercompletedDate", required = true, allowableValues = "Date")
	@Column(name = "order_completed_on")
	private Date ordercompletedDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm aa")
	@ApiModelProperty(value = "orderCancelledDate", required = true, allowableValues = "Date")
	@Column(name = "order_cancelled_on")
	private Date orderCancelledDate;
	
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "delivery_address_id")
	private CustomerDeliveryAddress deliveryAddressObj;
	
	@Column(name = "location")
	private String location;
	
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="longitude")
	private Double longitude;	
	
	@Column(name="offer_status")
	private Boolean offerStatus=false;	
	

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customerObj;
	
	@NotNull(message = "Deliivery address id cannot be null")
	@ApiModelProperty(value = "deliveryAddressId", required = true, allowableValues = "UUID")
	@Transient
	private UUID deliveryAddressId;
	
	@NotNull(message = "Customer id cannot be null")
	@ApiModelProperty(value = "customerId", required = true, allowableValues = "UUID")
	@Transient
	private UUID customerId;
	
	@Transient
	private Double orderQty;
	
	@OneToMany(mappedBy = "customerOrderObj", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CustomerOrderItems> customerOrderItemsList;
	
	public UUID getDeliveryAddressId() {
		if(null != deliveryAddressObj) {
			return deliveryAddressObj.getId();
		}
		return deliveryAddressId;
		
	}

	public UUID getCustomerId() {
		if(null != customerObj) {
			return customerObj.getId();
		}
		return customerId;
		
	}

	
	
	
}
