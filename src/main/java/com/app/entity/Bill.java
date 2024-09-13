package com.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.entity.Currency;
import com.app.entity.RecordModifier;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trading_bill")
@Getter
@Setter
@ToString
public class Bill extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "billNumber cannot be blank")
	@ApiModelProperty(value = "Valid billNumber", required = true, allowableValues = "NonEmpty String")
	@Column(name = "bill_number")
	private String billNumber;

	@NotNull(message = "transactionId cannot be blank")
	@ApiModelProperty(value = "transactionId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "transaction_id")
	private UUID transactionId;

	@NotNull(message = "billRefNumber cannot be blank")
	@ApiModelProperty(value = "Valid billRefNumber", required = true, allowableValues = "NonEmpty String")
	@Column(name = "bill_ref_number")
	private String billRefNumber;
	
	@JsonIgnore
	@NotNull(message = "AssociateInfoObj cannot be null")
	@ApiModelProperty(value = "AssociateInfoObj", required = true, dataType = "object", allowableValues = "A valid country object")
	@OneToOne()
	@JoinColumn(name = "associate_id")
	private AssociateInfo associateInfoObj;

//	@NotNull(message = "associateId cannot be blank")
//	@ApiModelProperty(value = "associateId", required = true, allowableValues = "NonEmpty String")
//	@Type(type = "uuid-char")
//	@Column(name = "associate_id")
//	private UUID associateId;
	
	@JsonIgnore
	@NotNull(message = "currencyObj cannot be null")
	@ApiModelProperty(value = "currencyObj", required = true, dataType = "object", allowableValues = "A valid country object")
	@OneToOne()
	@JoinColumn(name = "currency_id")
	private Currency currencyObj;

//	@NotNull(message = "currencyId cannot be blank")
//	@ApiModelProperty(value = "currencyId", required = true, allowableValues = "NonEmpty String")
//	@Type(type = "uuid-char")
//	@Column(name = "currency_id")
//	private UUID currencyId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@NotNull(message = "billDate cannot be blank")
	@ApiModelProperty(value = "billDate", required = true, allowableValues = "dd/mm/yyyy")
	@Column(name = "bill_date")
	private Date billDate;

	@NotNull(message = "paymentTerms cannot be blank")
	@ApiModelProperty(value = "paymentTerms", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "payment_terms")
	private UUID paymentTerms;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@NotNull(message = "dueDate cannot be blank")
	@ApiModelProperty(value = "dueDate", required = true, allowableValues = "dd/mm/yyyy")
	@Column(name = "due_date")
	private Date dueDate;

	@NotNull(message = "subTotal cannot be blank")
	@ApiModelProperty(value = "subTotal", required = true, allowableValues = "Double")
	@Column(name = "sub_total")
	private Double subTotal;

	@ApiModelProperty(value = "discountType", required = false, allowableValues = "String")
	@Column(name = "discount_type")
	private String discountType;

	@NotNull(message = "discountValue cannot be blank")
	@ApiModelProperty(value = "discountValue", required = true, allowableValues = "Double")
	@Column(name = "discount_value")
	private Double discountValue;

	@NotNull(message = "discountAcctHead cannot be blank")
	@ApiModelProperty(value = "discountAcctHead", required = true, allowableValues = "Double")
	@Type(type = "uuid-char")
	@Column(name = "discount_acct_head")
	private UUID discountAcctHead;

	@NotNull(message = "discountAmount cannot be blank")
	@ApiModelProperty(value = "discountAmount", required = true, allowableValues = "Double")
	@Column(name = "discount_amount")
	private Double discountAmount;

	@ApiModelProperty(value = "taxId", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "tax_id")
	private UUID taxId;

	@NotNull(message = "totalTax cannot be blank")
	@ApiModelProperty(value = "totalTax", required = true, allowableValues = "Double")
	@Column(name = "total_tax")
	private Double totalTax;

	@NotNull(message = "adjustment cannot be blank")
	@ApiModelProperty(value = "adjustment", required = true, allowableValues = "Double")
	@Column(name = "adjustment")
	private Double adjustment;

	@NotNull(message = "total cannot be blank")
	@ApiModelProperty(value = "total", required = true, allowableValues = "Double")
	@Column(name = "total")
	private Double total;

	@JsonIgnore
	@NotNull(message = "StatusMaster cannot be null")
	@ApiModelProperty(value = "StatusMaster", required = true, dataType = "object", allowableValues = "A valid country object")
	@OneToOne()
	@JoinColumn(name = "status_master_id")
	private StatusMaster statusMasterObj;
	
	@Transient
	private String assoName;
	
	@Transient
	private String assoAddress;
	
	@Transient
	private String currName;
	
	@Transient
	private List<BillItems> billItems;
	
	@Transient
	private UUID associateId;
	
	@Transient
	private UUID currencyId;
	
	@Transient
	private Double paidAmount;
	
	@Transient
	private Double payableAmountInLocal;
	
	@Transient
	private Double payableAmountInUSD;
	
	@Transient
	private Double conversionRate;
	
	
	
	
	

}
