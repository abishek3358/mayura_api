package com.app.entity;

import javax.persistence.Transient;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="vendor_contact_person")
public class VendorContactPerson extends RecordModifier implements Serializable {
	
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
	
	@NotNull(message = "FullName  cannot be blank")
	@ApiModelProperty(value = "FullName", required = true, allowableValues = "Integer")
	@Column(name="full_name")
	private String fullName;
	
	@NotNull(message = "EmailId cannot be blank")
	@ApiModelProperty(value = "EmailId", required = true, allowableValues = "Integer")
	@Column(name="email_id")
	private String emailId;
	
	@NotNull(message = "PhoneNumber cannot be blank")
	@ApiModelProperty(value = "PhoneNumber", required = true, allowableValues = "Integer")
	@Column(name="phone_number")
	private String phoneNumber;
	
	@NotNull(message = "ContactPersonRole cannot be blank")
	@ApiModelProperty(value = "ContactPersonRole", required = true, allowableValues = "Integer")
	@Column(name="contact_person_role")
	private String contactPersonRole;
	
	@Column(name="mark_as_default")
	private Boolean markAsDefaultDb;
	
	@Transient
	private String markAsDefault; 
	
}

