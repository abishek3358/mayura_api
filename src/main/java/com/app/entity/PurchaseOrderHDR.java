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

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trading_purchase_order_hdr")
@Getter
@Setter
public class PurchaseOrderHDR extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "PoNumber ID cannot be blank")
	@ApiModelProperty(value = "poNumber", required = false, allowableValues = "String")
	@Column(name = "po_number")
	private String poNumber;

	@NotNull(message = "PoRefNumber ID cannot be blank")
	@ApiModelProperty(value = "PoRefNumber", required = false, allowableValues = "String")
	@Column(name = "po_ref_number")
	private String poRefNumber;

	@NotNull(message = "Associate ID cannot be blank")
	@ApiModelProperty(value = "associateId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "associate_id")
	private UUID associateId;
	
	@NotNull(message = "Currency ID cannot be blank")
	@ApiModelProperty(value = "currencyId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "currency_id")
	private UUID currencyId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@NotNull(message = "OrderPlacedDate cannot be blank")
	@Column(name = "order_placed_date")
	private Date orderPlacedDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "expected_delivery_date")
	private Date expectedDeliveryDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "actual_delivery_date")
	private Date actualDeliveryDate;
	

	@Column(name = "closing_date")
	private Date closingDate;

	@Column(name = "cancellation_date")
	private Date cancellationDate;

	@NotNull(message = "StatusMaster cannot be null")
	@ApiModelProperty(value = "StatusMaster", required = true, dataType = "object", allowableValues = "A valid country object")
	@OneToOne()
	@JoinColumn(name = "status_master_id")
	private StatusMaster statusMasterObj;

	@ApiModelProperty(value = "Cancellation Reason", required = false, allowableValues = "String")
	@Column(name = "cancellation_reason")
	private String cancellationReason;

	@ApiModelProperty(value = "remarks", required = false, allowableValues = "String")
	@Column(name = "remarks")
	private String remarks;

	@ApiModelProperty(value = "totalCost", required = true, allowableValues = "Double")
	@NotNull(message = "TotalCost cannot be blank")
	@Column(name = "total_cost")
	private Double totalCost=0.00;

	@ApiModelProperty(value = "discount", required = true, allowableValues = "Double")
	@NotNull(message = "discount cannot be blank")
	@Column(name = "discount")
	private Double discount=0.00;

	@ApiModelProperty(value = "totalDiscountAmount", required = true, allowableValues = "Double")
	@NotNull(message = "TotalDiscountAmount cannot be blank")
	@Column(name = "total_discount_amount")
	private Double totalDiscountAmount=0.00;

	@ApiModelProperty(value = "totalServiceCharge", required = true, allowableValues = "Double")
	@NotNull(message = "TotalServiceCharge cannot be blank")
	@Column(name = "total_service_charge")
	private Double totalServiceCharge=0.00;

	@ApiModelProperty(value = "grandTotal", required = true, allowableValues = "Double")
	@NotNull(message = "TrandTotal cannot be blank")
	@Column(name = "grand_total")
	private Double grandTotal=0.00;

	@ApiModelProperty(value = "totalGSTAmount", required = true, allowableValues = "Double")
	@NotNull(message = "TotalGSTAmount cannot be blank")
	@Column(name = "total_gst_amount")
	private Double totalGSTAmount=0.00;

	@OneToMany(mappedBy = "tradingPurchaseOrderHDR", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<PurchaseOrderDTL> tradingPurchaseOrderDTLList;
	
	@Transient
	private List<ProductTransaction> productTransactionList;
	
	@Transient
	private Boolean isBillCreated=false;
	
	@Transient
	private String assoName;
	

}