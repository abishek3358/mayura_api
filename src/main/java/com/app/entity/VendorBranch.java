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

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="vendor_branch")
public class VendorBranch extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Type(type = "uuid-char")
	@Column(name="vendor_id")
	private UUID vendorId;
	
	@Column(name = "branch_name")
	private String branchName;
	
	@Column(name = "branch_code")
	private String branchCode;
	
	@Column(name = "location")
	private String location;
	
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="longitude")
	private Double longitude;
	
	@NotNull(message = "Trainee address cannot be blank")
	@ApiModelProperty(value = "name", required = true, allowableValues = "String")
	@Column(name = "address1")
	private String address1;

	@NotNull(message = "Trainee address cannot be blank")
	@ApiModelProperty(value = "name", required = true, allowableValues = "String")
	@Column(name = "address2")
	private String address2;

	@NotNull(message = "Trainee pincode cannot be blank")
	@ApiModelProperty(value = "name", required = true, allowableValues = "String")
	@Column(name = "postal_code")
	private String postalCode;

	@NotNull(message = "City Id cannot be blank")
	@ApiModelProperty(value = "Valid City Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "city_id")
	private UUID cityId;

	@NotNull(message = "State Id cannot be blank")
	@ApiModelProperty(value = "Valid State Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "state_id")
	private UUID stateId;

	@NotNull(message = "Country Id cannot be blank")
	@ApiModelProperty(value = "Valid Country Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "country_id")
	private UUID countryId;	 
	 
}
