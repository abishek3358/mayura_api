package com.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "one_time_password")
public class OneTimePassword extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ApiModelProperty(value = "userId", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "user_id")
	private UUID userId;
	
	@NotNull(message = "Mobile no cannot be null")
	@ApiModelProperty(value = "10 digit mobile number", required = false, allowableValues = "String")
	@Column(name = "mobile_phone")
	private String mobileNo;

	@NotNull(message = "OTP cannot be null")
	@ApiModelProperty(value = "OTP", required = false, allowableValues = "String")
	@Column(name = "otp")
	private String otp;

}
