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

@ApiModel(value = "Account Group", description = "The account group request payload")
@Entity
@Getter
@Setter
@Table(name="trading_account_group")
public class AccountGroup extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Account Id cannot be blank")
	@ApiModelProperty(value = "Valid Account Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name="account_id")
	private UUID accountId;

	@NotNull(message = "Currency Id cannot be blank")
	@ApiModelProperty(value = "Valid Currency Id", required = true, allowableValues = "NonEmpty String")
	@Type(type = "uuid-char")
	@Column(name="currency_id")
	private UUID currencyId;

	@NotNull(message = "Account Group Short Name cannot be blank")
    @ApiModelProperty(value = "Valid Group Short Name", required = true, allowableValues = "NonEmpty String")
	@Column(name="short_name")
	private String shortName;

	@NotNull(message = "Account Group Name cannot be blank")
	@ApiModelProperty(value = "Valid Group Name ", required = true, allowableValues = "NonEmpty String")
	@Column(name="name")
	private String name;
	
	@NotNull(message = "Account Group Description cannot be blank")
    @ApiModelProperty(value = "Valid Account Group Description", required = true, allowableValues = "NonEmpty String")
	@Column(name="description")
	private String description;
	
	@Transient
	private String accountName;
}
