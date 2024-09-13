package com.app.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.UserRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value = "Customer Request", description = "The customer request payload")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "customer")
public class Customer extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull(message = "Full name cannot be blank")
	@ApiModelProperty(value = "Valid Full name", required = true, allowableValues = "NonEmpty String")
	@Column(name = "full_name")
	private String fullName;	
	
	@NotNull(message = "Email cannot be blank")
	@ApiModelProperty(value = "Valid Email", required = true, allowableValues = "NonEmpty String")
	@Column(name = "email")
	private String email;
	
	@NotNull(message = "Mobile no cannot be blank")
	@ApiModelProperty(value = "Valid Mobile no", required = true, allowableValues = "NonEmpty String")
	@Column(name = "mobile_no")
	private String mobileNo;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<CustomerDeliveryAddress> deliveryAddressList;
	
	@Transient
	private String password;	
	
	@Transient
	private String userName;	
	
 
}
