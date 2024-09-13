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
@Table(name = "trading_sales_order_dtl")
@Getter
@Setter
public class SalesOrderDTL extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@JsonIgnore
	@NotNull(message = "salesOrderHDRObj cannot be blank")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sales_order_id")
	private SalesOrderHDR salesOrderHDRObj;

	@NotNull(message = "ProductId cannot be blank")
	@ApiModelProperty(value = "productId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "product_id")
	private UUID productId;

	@NotNull(message = "uom Id cannot be blank")
	@ApiModelProperty(value = "uomId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "uom_id")
	private UUID uomId;

	@ApiModelProperty(value = "orderQuantity", required = true, allowableValues = "Double")
	@NotNull(message = "OrderQuantity cannot be blank")
	@Column(name = "order_quantity")
	private Double orderQuantity;

	@ApiModelProperty(value = "actualQuantityDispatched", required = true, allowableValues = "Double")
	@NotNull(message = "Actual Quantity Dispatched cannot be blank")
	@Column(name = "actual_quantity_dispacted ")
	private Double actualQuantityDispatched;

	@ApiModelProperty(value = "returnQuantity", required = true, allowableValues = "Double")
	@NotNull(message = "returnQuantity cannot be blank")
	@Column(name = "return_qty ")
	private Double returnQuantity;

	@ApiModelProperty(value = "ratePerUnit", required = true, allowableValues = "Double")
	@NotNull(message = "RatePerUnit cannot be blank")
	@Column(name = "rate_per_unit")
	private Double ratePerUnit;

	@ApiModelProperty(value = "discount", required = true, allowableValues = "Double")
	@NotNull(message = "discount cannot be blank")
	@Column(name = "discount")
	private Double discount;

	@ApiModelProperty(value = "actualSalesRatePerUnit", required = true, allowableValues = "Double")
	@NotNull(message = "actualSalesRatePerUnit cannot be blank")
	@Column(name = "acutal_sale_rate_per_unit")
	private Double actualSalesRatePerUnit;

	@ApiModelProperty(value = "gstAmount", required = true, allowableValues = "Double")
	@NotNull(message = "gstAmount cannot be blank")
	@Column(name = "gst_amount")
	private Double gstAmount;
	
	@ApiModelProperty(value = "gstPercentage", required = true, allowableValues = "Double")
	//@NotNull(message = "gstAmount cannot be blank")
	@Column(name = "gst_perc")
	private Double gstPercentage;

	@ApiModelProperty(value = "amount", required = true, allowableValues = "Double")
	@NotNull(message = "Amount cannot be blank")
	@Column(name = "amount")
	private Double amount;
	
	@ApiModelProperty(value = "total", required = true, allowableValues = "Double")
	@NotNull(message = "total cannot be blank")
	@Column(name = "total")
	private Double total;
	
	@Transient
	private String productName;
	
	@Transient
	private String uomName;

}