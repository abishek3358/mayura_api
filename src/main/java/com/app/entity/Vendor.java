package com.app.entity;

import java.io.Serializable;
import java.util.List;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="vendor")
public class Vendor extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull(message = "name cannot be blank")
	@ApiModelProperty(value = "valid name", required = true, allowableValues = "String")
	@Column(name="name")
	private String name;
	
	@NotNull(message = "vendorCode cannot be blank")
	@ApiModelProperty(value = "valid vendorCode", required = true, allowableValues = "String")
	@Column(name="vendor_code")
	private String vendorCode;
	
	@NotNull(message = "phoneNumber cannot be blank")
	@ApiModelProperty(value = "valid phoneNumber", required = true, allowableValues = "String")
	@Column(name="phone_number")
	private String phoneNumber;
	
	@NotNull(message = "emailId cannot be blank")
	@ApiModelProperty(value = "valid emailId", required = true, allowableValues = "String")
	@Column(name="email_id")
	private String emailId;
	
	@NotNull(message = "gstTaxNumber cannot be blank")
	@ApiModelProperty(value = "valid gstTaxNumber", required = true, allowableValues = "String")
	@Column(name="gst_tax_number")
	private String gstTaxNumber;	

	@Transient
	private List<VendorBranch> vendorBranchList;
	
	@Transient
	private List<VendorAccount> vendorAccountList;
	
	@Transient
	private List<VendorContactPerson> vendorContactPersonList;

		
	
}
