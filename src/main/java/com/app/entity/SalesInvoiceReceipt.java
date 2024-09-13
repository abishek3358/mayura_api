package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.entity.RecordModifier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trading_inv_receipt")
@Getter
@Setter
@ToString
public class SalesInvoiceReceipt extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "invId cannot be blank")
	@ApiModelProperty(value = "invId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "inv_id")
	private UUID invId;

	@NotNull(message = "paidBy cannot be blank")
	@ApiModelProperty(value = "paidBy", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "paid_by")
	private UUID paidBy;

	@NotNull(message = "paidAmount cannot be blank")
	@ApiModelProperty(value = "paidAmount", required = true, allowableValues = "Double")
	@Column(name = "paid_amount")
	private Double paidAmount;

	@NotNull(message = "currencyId cannot be blank")
	@ApiModelProperty(value = "currencyId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "currency_id")
	private UUID currencyId;

	@ApiModelProperty(value = "usdConversion", required = true, allowableValues = "Double")
	@Column(name = "usd_conversion")
	private Double usdConversion;

	@NotNull(message = "acctGroupId cannot be blank")
	@ApiModelProperty(value = "acctGroupId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "account_group_id")
	private UUID acctGroupId;

	@ApiModelProperty(value = "notes Type name", required = true, allowableValues = "String")
	@Column(name = "notes")
	private String notes;

}
