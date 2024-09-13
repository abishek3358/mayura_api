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

import com.app.enumeration.Status;

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
@Table(name = "unit_of_measurement")
public class UnitOfMeasurement extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	

	@NotNull(message = "Name cannot be blank")
	@ApiModelProperty(value = "name", required = true, allowableValues = "String")
	@Column(name = "name")
	private String name;

	@NotNull(message = "Short Name cannot be blank")
	@ApiModelProperty(value = "shortName", required = true, allowableValues = "String")
	@Column(name = "short_name")
	private String shortName;
	
	@NotNull(message = "Status cannot be blank")
	@ApiModelProperty(value = "status", required = true, allowableValues = "String")
	@Column(name = "status")
	private Status status;

}