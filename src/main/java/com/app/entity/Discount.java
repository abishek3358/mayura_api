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

import com.app.enumeration.ProductDiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="discount")
public class Discount extends RecordModifier implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull(message = "Discount code cannot be blank")
	@ApiModelProperty(value = "Discount Code", required = true, allowableValues = "String")
	@Column(name = "discount_code")
	private String discountCode;
	
	@ApiModelProperty(value = "Valid status", required = true, allowableValues = "ACTIVE, INACTIVE")
	@Enumerated(EnumType.STRING)
	@Column(name="discount_type")
	private ProductDiscountType discountType;
	
	@NotNull(message = "Discount description  cannot be blank")
	@ApiModelProperty(value = "Discount description", required = true, allowableValues = "String")
	@Column(name="discount_value")
	private String discountValue;
	
	@NotNull(message = "Minimum Order Value in days cannot be blank")
	@ApiModelProperty(value = "Minimum Order Value", required = true, allowableValues = "Integer")
	@Column(name = "minimum_order_value")
	private Integer minOrderValue;
	
	@NotNull(message = "Discount description  cannot be blank")
	@ApiModelProperty(value = "Discount description", required = true, allowableValues = "String")
	@Column(name="discount_description")
	private String discountDescription;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "start_Date",nullable = false, updatable = false)
	private String startDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "expiry_Date",nullable = false, updatable = false)
	private String expiryDate;

}
