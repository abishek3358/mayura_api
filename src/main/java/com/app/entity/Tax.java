package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tax")
@NoArgsConstructor
public class Tax extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "name cannot be blank")
	@ApiModelProperty(value = "Valid name", required = true, allowableValues = "string")
	@Column(name = "name")
	private String name;

	@NotNull(message = "Short Name cannot be blank")
	@ApiModelProperty(value = "Valid name", required = true, allowableValues = "string")
	@Column(name = "short_name")
	private String shortName;

	@NotNull(message = "Tax Percentage cannot be blank")
	@ApiModelProperty(value = "Valid Tax Percentage", required = true, allowableValues = "Double")
	@Column(name = "tax_percentage")
	private Double taxPercentage;
	
	@Transient
	private Double taxAmount;

}
