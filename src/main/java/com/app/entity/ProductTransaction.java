package com.app.entity;

import java.io.Serializable;
import java.util.Date;
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

import com.app.enumeration.TransSource;
import com.app.enumeration.TransType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "Product Transaction", description = "The Product Transaction request payload")
@Entity
@Getter
@Setter
@Table(name="trading_product_transaction")
public class ProductTransaction extends RecordModifier implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull(message = "Product Id cannot be blank")
	@ApiModelProperty(value = "Valid Product Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name="product_id")
	private UUID productId;
	
	@NotNull(message = "Transaction Id cannot be blank")
	@ApiModelProperty(value = "Valid Transaction Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name="trx_id")
	private UUID transactionId;
	
	@NotNull(message = "Transaction ReferenceNumber cannot be blank")
	@ApiModelProperty(value = "Valid Transaction ReferenceNumber", required = true, allowableValues = "NonEmpty String")
	@Column(name="trx_ref_number")
	private String transactionReferenceNumber;
	
	@NotNull(message = "Source cannot be blank")
    @ApiModelProperty(value = "Valid status", required = true, allowableValues = "ACTIVE, INACTIVE")
	@Enumerated(EnumType.STRING)
	@Column(name="trx_source")
	private TransSource transSource;
	
	@NotNull(message = " TransactionDate cannot be blank")
	@ApiModelProperty(value = "Valid TransactionDate ", required = true, allowableValues = "NonEmpty String")
	@Column(name="trx_date")
	private Date transactionDate;
	
	@NotNull(message = " TransactionQuantity cannot be blank")
	@ApiModelProperty(value = "Valid TransactionQuantity ", required = true, allowableValues = "NonEmpty String")
	@Column(name="trx_quantity")
	private Double transactionQuantity;
	
	
	@NotNull(message = " TransactionType cannot be blank")
	@ApiModelProperty(value = "Valid TransactionType ", required = true, allowableValues = "CREDIT,DEBIT")
	@Enumerated(EnumType.STRING)
	@Column(name="trx_type")
	private TransType transactionType;
	
}
