package com.app.entity;

import com.app.entity.RecordModifier;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "trading_purchase_order_dtl")
@Getter
@Setter
public class PurchaseOrderDTL extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@JsonIgnore
	@NotNull(message = "TradingPurchaseOrderHDR cannot be blank")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrderHDR tradingPurchaseOrderHDR;

	@NotNull(message = "ProductId cannot be blank")
	@ApiModelProperty(value = "productId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "product_id")
	private UUID productId;

	@ApiModelProperty(value = "orderQuantity", required = true, allowableValues = "Double")
	@NotNull(message = "OrderQuantity cannot be blank")
	@Column(name = "order_quantity")
	private Double orderQuantity;

	@NotNull(message = "uom Id cannot be blank")
	@ApiModelProperty(value = "uomId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "uom_id")
	private UUID uomId;

	@ApiModelProperty(value = "ratePerUnit", required = true, allowableValues = "Double")
	@NotNull(message = "RatePerUnit cannot be blank")
	@Column(name = "rate_per_unit")
	private Double ratePerUnit;

	@ApiModelProperty(value = "amount", required = true, allowableValues = "Double")
	@NotNull(message = "Amount cannot be blank")
	@Column(name = "amount")
	private Double amount;

	@ApiModelProperty(value = "qcPass", required = true, allowableValues = "Double")
	@NotNull(message = "qcPass cannot be blank")
	@Column(name = "qc_pass")
	private Double qcPass;

	@ApiModelProperty(value = "qcFail", required = true, allowableValues = "Double")
	@NotNull(message = "qcFail cannot be blank")
	@Column(name = "qc_fail")
	private Double qcFail;

	@ApiModelProperty(value = "returnQty", required = true, allowableValues = "Double")
	@NotNull(message = "ReturnQty cannot be blank")
	@Column(name = "return_qty")
	private Double returnQty;

	@ApiModelProperty(value = "discount", required = true, allowableValues = "Double")
	@NotNull(message = "discount cannot be blank")
	@Column(name = "discount")
	private Double discount;

	@ApiModelProperty(value = "actualQuantity", required = true, allowableValues = "Double")
	@NotNull(message = "ActualQuantity cannot be blank")
	@Column(name = "actual_quantity")
	private Double actualQuantity;

	@ApiModelProperty(value = "purchaseRatePerUnit", required = true, allowableValues = "Double")
	@NotNull(message = "purchaseRatePerUnit cannot be blank")
	@Column(name = "purchase_rate_per_unit")
	private Double purchaseRatePerUnit;

	@ApiModelProperty(value = "gstAmount", required = true, allowableValues = "Double")
	@NotNull(message = "gstAmount cannot be blank")
	@Column(name = "gst_amount")
	private Double gstAmount;
	
	@ApiModelProperty(value = "gstPercentage", required = true, allowableValues = "Double")
	//@NotNull(message = "gstAmount cannot be blank")
	@Column(name = "gst_perc")
	private Double gstPercentage;
	
	@ApiModelProperty(value = "total", required = true, allowableValues = "Double")
	@NotNull(message = "total cannot be blank")
	@Column(name = "total")
	private Double total;
	
	@Transient
	private String productName;
	
	@Transient
	private String uomName;

}