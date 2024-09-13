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

import com.app.enumeration.Status;
import com.app.util.ConvertionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "currency", description = "The Currency request payload")
@Entity
@Table(name="currency")
public class Currency implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name="name")
	private String name; 
	
	@Column(name="code")
	private String code;
	
	@Column(name="symbol")
	private String symbol;
	
	@Column(name="conversion_rate")
	private Double conversionRate;
	
	@Column(name="base_currency")
	private String baseCurrency;
	
	@NotNull(message = "Status cannot be blank")
    @ApiModelProperty(value = "Valid status", required = true, allowableValues = "ACTIVE, INACTIVE")
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;

	@JsonIgnore
	@Column(name="updated_date")
	private Date updatedDate;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSymbol() {
		if(null == symbol) {
			symbol = "";
		}
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getConversionRate() {
		if(null != conversionRate) {
			conversionRate = ConvertionUtil.roundOffToUpper(conversionRate);
		}
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
