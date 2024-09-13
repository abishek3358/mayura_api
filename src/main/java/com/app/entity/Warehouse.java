package com.app.entity;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "warehouse")
public class Warehouse extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ApiModelProperty(value = "Name", required = false, allowableValues = "String")
	@Column(name = "name")
	private String name;
	
	@ApiModelProperty(value = "Short Name", required = false, allowableValues = "String")
	@Column(name = "short_name")
	private String shortName;

	@ApiModelProperty(value = "Address", required = false, allowableValues = "String")
	@Column(name = "address")
	private String address;

	@ApiModelProperty(value = "Remarks", required = false, allowableValues = "String")
	@Column(name = "remarks")
	private String remarks;
 
	@JsonIgnore
	@OneToOne()
	@JoinColumn(name = "city_id")
	private City cityObj;
	
	@NotNull(message = "City cannot be null")
	@ApiModelProperty(value = "City", required = true, dataType = "object", allowableValues = "A valid city object")
	@Transient
	private UUID cityId;
	
	@NotNull(message = "State cannot be null")
	@ApiModelProperty(value = "State", required = true, dataType = "object", allowableValues = "A valid state object")
	@Transient
	private UUID stateId;
	
	@NotNull(message = "Country cannot be null")
	@ApiModelProperty(value = "Country", required = true, dataType = "object", allowableValues = "A valid country object")
	@Transient
	private UUID countryId;
	
	public UUID getCountryId() {
		return cityObj.getCountryId();
	}
	
	public UUID getStateId() {
		return cityObj.getStateId();
	}
	
	public UUID getCityId() {
		if(null != cityObj) {
			return cityObj.getId();
		}
		return cityId;
	}

}
