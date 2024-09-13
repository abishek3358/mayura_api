package com.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.app.entity.RecordModifier;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trading_sales_order_hdr")
@Getter
@Setter
public class SalesOrderHDR extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "PoNumber ID cannot be blank")
	@ApiModelProperty(value = "poNumber", required = false, allowableValues = "String")
	@Column(name = "sales_order_number")
	private String salesOrderNumber;

	@NotNull(message = "PoRefNumber ID cannot be blank")
	@ApiModelProperty(value = "PoRefNumber", required = false, allowableValues = "String")
	@Column(name = "sales_ref_number")
	private String salesRefNumber;

	@NotNull(message = "StatusMaster cannot be blank")
	@ApiModelProperty(value = "orderStatusID", required = true, allowableValues = "UUID")
	@OneToOne
	@JoinColumn(name = "order_status")
	private StatusMaster statusMasterObj;

	@NotNull(message = "Associate ID cannot be blank")
	@ApiModelProperty(value = "associateId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "associate_id")
	private UUID associateId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@NotNull(message = "OrderDate cannot be blank")
	@Column(name = "order_date ")
	private Date orderDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "committed_date_of_delivery")
	private Date committedDateOfDelivery;

	@JsonIgnore
	@Column(name = "date_product_ready_to_dispatch")
	private Date dateProductReadyToDispatch;

	@JsonIgnore
	@Column(name = "date_of_dispach")
	private Date dateOfDispatch;

	@JsonIgnore
	@Column(name = "delivery_mode ")
	private String deliveryMode;

	@JsonIgnore
	@Column(name = "date_of_closuer ")
	private Date dateOfClosuer;

	@JsonIgnore
	@Column(name = "date_of_cancellation ")
	private Date dateOfCancellation;

	@JsonIgnore
	@Column(name = "date_of_rejected_n_recevied ")
	private Date dateOfRejectedNReceived;

	@ApiModelProperty(value = "salesOrderTotalCost", required = true, allowableValues = "Double")
	@Column(name = "so_total_cost")
	private Double salesOrderTotalCost=0.00;

	@ApiModelProperty(value = "totalDiscount", required = true, allowableValues = "Double")
	@Column(name = " total_discount")
	private Double totalDiscount=0.00;

	@ApiModelProperty(value = "discount", required = true, allowableValues = "Double")
	@Column(name = "discount_parcentage")
	private Double discountPercentage=0.00;

	@ApiModelProperty(value = "totalGSTAmount", required = true, allowableValues = "Double")
	@NotNull(message = "TotalGSTAmount cannot be blank")
	@Column(name = "total_gst_amount")
	private Double totalGSTAmount=0.00;

	@ApiModelProperty(value = "grandTotal", required = true, allowableValues = "Double")
	@NotNull(message = "TrandTotal cannot be blank")
	@Column(name = "grand_total")
	private Double grandTotal=0.00;

	@NotNull(message = "CurrencyId cannot be blank")
	@ApiModelProperty(value = "currencyId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "currency_id")
	private UUID currencyId;

	@ApiModelProperty(value = "Reason", required = false, allowableValues = "String")
	@Column(name = "reason")
	private String reason;

	@OneToMany(mappedBy = "salesOrderHDRObj", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SalesOrderDTL> salesOrderDTLList;
	
	@Transient
	private String orderingDate;
	
	@Transient
	private String assoName;
	

}
