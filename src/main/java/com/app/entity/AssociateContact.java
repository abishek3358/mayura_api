package com.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.entity.RecordModifier;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@ApiModel(value = "Agent Contact", description = "The agent contacts request payload")
@Entity
@Getter
@Setter
@Table(name="trading_associate_contact")
public class AssociateContact extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Associate Id cannot be blank")
	@ApiModelProperty(value = "Valid Associate Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name="associate_id")
	private UUID associateId;
	
	@NotNull(message = "Contact Name cannot be blank")
    @ApiModelProperty(value = "Valid Contact name", required = true, allowableValues = "NonEmpty String")
	@Column(name="name")
	private String name;
	
	@NotNull(message = "Contact email cannot be blank")
    @ApiModelProperty(value = "Valid Contact email", required = true, allowableValues = "NonEmpty String")
	@Column(name="email")
	private String email;
	 
	@NotNull(message = "Mobile number cannot be blank")
    @ApiModelProperty(value = "Valid Mobile number", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name="mobile_no")
	private String mobileNo;

	
	@NotNull(message = "Specify whether the contact is default or not")
    @ApiModelProperty(value = "Flag denoting whether the contact is default or not", required = true, 
    	dataType = "boolean", allowableValues = "true, false")
	@Column(name="is_default")
	private Boolean isDefault;
	
	@Transient
	private String defaultContact;

	public Boolean getIsDefault() {
		if(null == isDefault) {
			isDefault = false;
		}
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getDefaultContact() {
		if(isDefault != null && isDefault) {
			defaultContact="Yes";
		}else {
			defaultContact="No";
		}
		return defaultContact;
	}

	public void setDefaultContact(String defaultContact) {
		this.defaultContact = defaultContact;
	}
	
}
