package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.VendorOrderStatus;

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
@Table(name="vendor_order_mapping")
public class VendorOrderMapping extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull(message = "Vendor id cannot be blank")
	@Type(type = "uuid-char")
	@Column(name = "vendor_id")
	private UUID vendorId;
	
	@NotNull(message = "Customer id cannot be blank")
	@Type(type = "uuid-char")
	@Column(name = "customer_id")
	private UUID customerId;
	
	@NotNull(message = "Customer order id cannot be blank")
	@Type(type = "uuid-char")
	@Column(name = "customer_order_id")
	private UUID customerOrderId;
	
	@Column(name = "rejection_reason")
	private String rejectionReason;

	@NotNull(message = "Vendor status cannot be blank")
	@ApiModelProperty(value = "Valid vendorOrder status", required = true,
			allowableValues = "Pending,Allocated,Shipped,Delivered,Closed,Rejected")
	@Enumerated(EnumType.STRING)
	@Column(name = "vendor_order_status")
	private VendorOrderStatus vendorOrderStatus;
	
	@ApiModelProperty(value = "valid ratings", required = true, allowableValues = "String")
	@Column(name="ratings")
	private Double ratings;
	
}
