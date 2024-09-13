package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.DiscountType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trading_bill_items")
@Getter
@Setter
public class BillItems extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "billId cannot be blank")
	@ApiModelProperty(value = "billId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "bill_id")
	private UUID billId;

	@JsonIgnore
	@NotNull(message = "productObj cannot be null")
	@ApiModelProperty(value = "productObj", required = true, dataType = "object", allowableValues = "A valid country object")
	@OneToOne()
	@JoinColumn(name = "product_id")
	private Product productObj;

//	@NotNull(message = "productId cannot be blank")
//	@ApiModelProperty(value = "productId", required = true, allowableValues = "NonEmpty String")
//	@Type(type = "uuid-char")
//	@Column(name = "product_id")
//	private UUID productId;

	@NotNull(message = "accountHeadId cannot be blank")
	@ApiModelProperty(value = "accountHeadId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "account_head_id")
	private UUID accountHeadId;

	@NotNull(message = "quantity cannot be blank")
	@ApiModelProperty(value = "quantity", required = true, allowableValues = "Double")
	@Column(name = "quantity")
	private Double quantity;

	@NotNull(message = "ratePerUnit cannot be blank")
	@ApiModelProperty(value = "ratePerUnit", required = true, allowableValues = "Double")
	@Column(name = "rate_per_unit")
	private Double ratePerUnit;

	@NotNull(message = "amount cannot be blank")
	@ApiModelProperty(value = "amount", required = true, allowableValues = "Double")
	@Column(name = "amount")
	private Double amount;

	// @NotNull(message = "discountType cannot be blank")
	@ApiModelProperty(value = "discountType", required = true, allowableValues = "Percentage,Amount")
	@Enumerated(EnumType.STRING)
	@Column(name = "discount_type")
	private DiscountType discountType;

	@NotNull(message = "discountValue cannot be blank")
	@ApiModelProperty(value = "discountValue", required = true, allowableValues = "Double")
	@Column(name = "discount_value")
	private Double discountValue;

	@NotNull(message = "discountAmount cannot be blank")
	@ApiModelProperty(value = "discountAmount", required = true, allowableValues = "Double")
	@Column(name = "discount_amount")
	private Double discountAmount;

	@ApiModelProperty(value = "gstId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "gst_id")
	private UUID gstId;

	@NotNull(message = "gstAmount cannot be blank")
	@ApiModelProperty(value = "gstAmount", required = true, allowableValues = "Double")
	@Column(name = "gst_amount")
	private Double gstAmount;

	@ApiModelProperty(value = "gstPerc", required = true, allowableValues = "Double")
	@Column(name = "gst_perc")
	private Double gstPerc;

	@NotNull(message = "total cannot be blank")
	@ApiModelProperty(value = "total", required = true, allowableValues = "Double")
	@Column(name = "total")
	private Double total;

	@Transient
	private String productName;

	@Transient
	private UUID productId;

}
