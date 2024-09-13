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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "Payment Term", description = "The payment term request payload")
@Entity
@Getter
@Setter
@Table(name="trading_payment_term")

public class PaymentTerm extends RecordModifier implements Serializable{
 
	 private static final long serialVersionUID = 1L;
	 
	    @Id
		@GeneratedValue(generator = "UUID")
		@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
		@Type(type = "uuid-char")
		@Column(name = "id", updatable = false, nullable = false)
		private UUID id;
	    
	    @NotNull(message = "Payment Term cannot be blank")
	    @ApiModelProperty(value = "Valid Payment Term Name", required = true, allowableValues = "NonEmpty String")
		@Column(name="term_name")
		private String termName;
	    
	    
	    @NotNull(message = "Payment Term cannot be blank")
	    @ApiModelProperty(value = "Valid Payment Term No Of Days", required = true, allowableValues = "NonEmpty String")
		@Column(name="no_of_days")
		private Integer noOfDays;

}