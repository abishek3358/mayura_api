package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.app.enumeration.ProductDiscountType;
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
@Table(name="product_dtl")
public class ProductDtl extends RecordModifier implements Serializable, Comparable<ProductDtl> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Transient
	private UUID productId;
	
	@JsonIgnore
	@OneToOne()
	@JoinColumn(name = "product_uom_id")
	private UnitOfMeasurement unitOfMeasurement;
	
	@NotNull(message = "Unit of measurment cannot be blank")
	@ApiModelProperty(value = "Unit of measurment", required = true, allowableValues = "UUID")
	@Transient
	private UUID uomId;

	@NotNull(message = "Product Initial quantity cannot be blank")
	@ApiModelProperty(value = "Product Initial quantity", required = true, allowableValues = "Double")
	@Column(name = "quantity")
	private Double quantity;

	@NotNull(message = "Product Thershold quantity cannot be blank")
	@ApiModelProperty(value = "Product Thershold quantity", required = true, allowableValues = "Double")
	@Column(name = "threshold_quantity")
	private Double thresholdQuantity;

	@NotNull(message = "Product Purchase rate  cannot be blank")
	@ApiModelProperty(value = "Product Purchase rate", required = true, allowableValues = "Double")
	@Column(name = "purchase_rate")
	private Double purchaseRate;

	@NotNull(message = "Product sales rate  cannot be blank")
	@ApiModelProperty(value = "Product sales rate", required = true, allowableValues = "Double")
	@Column(name = "sales_rate")
	private Double salesRate;
	
	@ApiModelProperty(value = "Valid status", required = true, allowableValues = "NONE,FLAT,PERCENTAGE")
	@Enumerated(EnumType.STRING)
	@Column(name="disc_type")
	private ProductDiscountType discountType ;
	
	@ApiModelProperty(value = "Product discount", required = true, allowableValues = "Double")
	@Column(name = "discount")
	private Double discount;

	public UUID getUomId() {
		if(null != unitOfMeasurement) {
			return unitOfMeasurement.getId();
		}
		return uomId;
	}
	
	@Override
	public int compareTo(ProductDtl o) {
		return getSalesRate().compareTo(o.getSalesRate());
	}
 
 
}
