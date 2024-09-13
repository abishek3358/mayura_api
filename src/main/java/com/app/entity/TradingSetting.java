package com.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.entity.RecordModifier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trading_settings")
@Getter
@Setter
public class TradingSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ApiModelProperty(value = "poPrefix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "po_prefix")
	private String poPrefix;

	@NotNull(message="poSuffix cannot be null")
	@ApiModelProperty(value = "poSuffix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "po_suffix")
	private Integer poSuffix;
	
	@ApiModelProperty(value = "poBillPrefix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "po_bill_prefix")
	private String poBillPrefix;

	@NotNull(message="poBillSuffix cannot be null")
	@ApiModelProperty(value = "poBillSuffix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "po_bill_suffix")
	private Integer poBillSuffix;
	
	@ApiModelProperty(value = "poBillReceiptPrefix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "po_bill_receipt_prefix")
	private String poBillReceiptPrefix;

	@NotNull(message="poBillReceiptSuffix cannot be null")
	@ApiModelProperty(value = "poBillReceiptSuffix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "po_bill_receipt_suffix")
	private Integer poBillReceiptSuffix;
	
	@ApiModelProperty(value = "soPrefix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "so_prefix")
	private String soPrefix;

	@NotNull(message="soSuffix cannot be null")
	@ApiModelProperty(value = "soSuffix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "so_suffix")
	private Integer soSuffix;
	
	@ApiModelProperty(value = "soInvoicePrefix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "so_invoice_prefix")
	private String soInvoicePrefix;

	@NotNull(message="soInvoiceSuffix cannot be null")
	@ApiModelProperty(value = "soSuffix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "so_invoice_suffix")
	private Integer soInvoiceSuffix;
	
	@ApiModelProperty(value = "soInvoiceReceiptPrefix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "so_invoice_receipt_prefix")
	private String soInvoiceReceiptPrefix;

	@NotNull(message="soInvoiceReceiptSuffix cannot be null")
	@ApiModelProperty(value = "soInvoiceReceiptSuffix", required = true, allowableValues = "NonEmpty String")
	@Column(name = "so_invoice_receipt_suffix")
	private Integer soInvoiceReceiptSuffix;
	

}
