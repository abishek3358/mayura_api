package com.app.entity;

import java.io.Serializable;
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

import com.app.enumeration.MobileType;
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
@Table(name="profile_mobile_number")

public class ProfileMobile extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Offer name cannot be blank")
	@ApiModelProperty(value = "Valid Offer name", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name = "customer_id")
	private UUID customerId;

	@ApiModelProperty(value = "Valid OfferType ", required = true, allowableValues = "PRIMARY,SECONDARY")
	@Enumerated(EnumType.STRING)
	@Column(name = "mobile_type")
	private MobileType title;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm aa")
	@NotNull(message = "Mobile number cannot be blank")
	@Column(name = "mobile_no")
	private String mobileNo;

	
}
