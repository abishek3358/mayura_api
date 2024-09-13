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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.StatusType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trading_status_master")
@Getter
@Setter
public class StatusMaster implements Serializable, Comparable<StatusMaster> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Status type cannot be blank")
	@ApiModelProperty(value = "Valid Status type", required = true, allowableValues = "SALES, PURCHASE, INVOICE, BILLS")
	@Enumerated(EnumType.STRING)
	@Column(name = "status_type")
	private StatusType statusType;

	@NotNull(message = "Name cannot be blank")
	@ApiModelProperty(value = "name", required = true, allowableValues = "String")
	@Column(name = "name")
	private String name;

	@ApiModelProperty(value = "description", required = false, allowableValues = "String")
	@Column(name = "description")
	private String description;

	// @NotNull(message = "Sequence cannot be blank")
	@ApiModelProperty(value = "Valid Sequence", allowableValues = "Numbers", required = false)
	@Column(name = "sequence")
	private Integer sequence;
	
	@Transient
	@Override
	public int compareTo(StatusMaster v1) {
	    return getSequence().compareTo(v1.getSequence());
	 }

}