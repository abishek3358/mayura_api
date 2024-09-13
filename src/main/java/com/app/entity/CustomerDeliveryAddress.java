package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value = "Customer Delivery Address Request", description = "The customer delivery address request payload")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "customer_delivery_address")
public class CustomerDeliveryAddress extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Address type cannot be blank")
	@ApiModelProperty(value = "Valid Address type", required = true, allowableValues = "Home, Work, Other")
	@Enumerated(EnumType.STRING)
	@Column(name = "address_type")
	private AddressType addressType;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@NotNull(message = "Name can be null but not blank")
	@ApiModelProperty(value = "Name", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "name")
	private String name;

	@NotNull(message = "Mobile no cannot be blank")
	@ApiModelProperty(value = "Valid Mobile no", required = true, allowableValues = "NonEmpty String")
	@Column(name = "mobile_no")
	private String mobileNo;

	@NotNull(message = "Delivery Area can be null but not blank")
	@ApiModelProperty(value = "Delivery Area", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "delivery_area")
	private String landMark;

	@NotNull(message = "Address Line1 can be null but not blank")
	@ApiModelProperty(value = "Address Line1", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "address_line1")
	private String addressLine1;

	@ApiModelProperty(value = "Address Line2", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "address_line2")
	private String addressLine2;

	@ApiModelProperty(value = "Delivery Instruction", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "delivery_instruction")
	private String deliveryInstruction;

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id")
	private City cityObj;
	
	@ApiModelProperty(value = "Postal code", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "postal_code")
	private String postalCode;

	@NotNull(message = "City cannot be null")
	@ApiModelProperty(value = "City", required = true, dataType = "object", allowableValues = "UUID")
	@Transient
	private UUID cityId;

	@Transient
	private String cityName;

	@NotNull(message = "State cannot be null")
	@ApiModelProperty(value = "State", required = true, dataType = "object", allowableValues = "UUID")
	@Transient
	private UUID stateId;

	@Transient
	private String stateName;

	@NotNull(message = "City latitude cannot be null")
	@ApiModelProperty(value = "City", required = true, dataType = "object", allowableValues = "UUID")
	@Column
	private Double latitude;

	@NotNull(message = "City longitude cannot be null")
	@ApiModelProperty(value = "City", required = true, dataType = "object", allowableValues = "UUID")
	@Column
	private Double longitude;

	@NotNull(message = "City location cannot be null")
	@ApiModelProperty(value = "City", required = true, dataType = "object", allowableValues = "UUID")
	@Column
	private String location;

	@NotNull(message = "Country cannot be null")
	@ApiModelProperty(value = "Country", required = true, dataType = "object", allowableValues = "UUID")
	@Transient
	private UUID countryId;

	@Transient
	private String countryName;
	public void setCityRelatedInformation() {
		if (null != cityObj) {
			setCityId(cityObj.getId());
			setCityName(cityObj.getName());
			setStateId(cityObj.getStateId());
			setCountryId(cityObj.getCountryId());
		}
	}	
	
}
