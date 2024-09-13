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

import com.app.enumeration.OfferType;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value = "Offer Details Request", description = "The offer details request payload")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "offer_details")
public class OfferDetails extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Offer name cannot be blank")
	@ApiModelProperty(value = "Valid Offer name", required = true, allowableValues = "NonEmpty String")
	@Column(name = "offer_name")
	private String offerName;

	@NotNull(message = "Offer Code cannot be blank")
	@ApiModelProperty(value = "Valid Offer", required = true, allowableValues = "NonEmpty String")
	@Column(name = "offer_code")
	private String offerCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm aa")
	@NotNull(message = "Start Date cannot be blank")
	@Column(name = "start_date")
	private Date startDateAndTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm aa")
	@NotNull(message = "Expiry Date cannot be blank")
	@Column(name = "expiry_date")
	private Date expiryDateAndTime;

	@NotNull(message = " OfferType cannot be blank")
	@ApiModelProperty(value = "Valid OfferType ", required = true, allowableValues = "CREDIT,DEBIT")
	@Enumerated(EnumType.STRING)
	@Column(name = "offer_type")
	private OfferType offerType;

	@NotNull(message = "Offer Value cannot be blank")
	@ApiModelProperty(value = "Valid Offer", required = true, allowableValues = "NonEmpty String")
	@Column(name = "offer_value")
	private Double offerValue;

	@NotNull(message = "Minimum Order Amount cannot be blank")
	@ApiModelProperty(value = "Valid Offer", required = true, allowableValues = "NonEmpty String")
	@Column(name = "minimum_order_amount")
	private Double minimumOrderAmount;

	@NotNull(message = "Offer Terms and Conditions cannot be blank")
	@ApiModelProperty(value = "Valid Offer no", required = true, allowableValues = "NonEmpty String")
	@Column(name = "terms_conditions")
	private String termsAndConditions;

}
