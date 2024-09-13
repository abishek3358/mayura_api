package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.Status;
import com.app.util.file.AWSS3DirectoryUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "product_category")
public class ProductCategory extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Product Category name cannot be blank")
	@ApiModelProperty(value = "Product Category name", required = true, allowableValues = "String")
	@Column(name = "name")
	private String name;

	
	@NotNull(message = "Product Category code cannot be blank")
	@ApiModelProperty(value = "Product Category code", required = true, allowableValues = "String")
	@Column(name = "short_name")
	private String shortName;

	@NotNull(message = "Product Category description cannot be blank")
	@ApiModelProperty(value = "Product Category description", required = true, allowableValues = "String")
	@Column(name = "description")
	private String description;

	@NotNull(message = "Status cannot be blank")
	@ApiModelProperty(value = "status", required = true, allowableValues = "String")
	@Column(name = "status")
	private Status status;

//	@JsonIgnore
//	@JoinColumn(name = "product_type_id")
//	@OneToOne
//	private ProductType productTypeObj;

	@NotNull(message = "Product Type cannot be blank")
	@ApiModelProperty(value = "Product Type", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "product_type_id")
	private UUID productTypeId;

	@NotNull(message = "Product Type cannot be blank")
	@ApiModelProperty(value = "Product Type", required = true, allowableValues = "String")
	@Transient
	private String productTypeName;

	@Column(name = "filename")
	private String fileName;

	@Transient
	private String fileBase64;
	
	@Transient
	private String productCount;

	@Transient
	private String fileUrl;
	
	@Transient
	private Boolean showDownloadBtn =false;

	
	public String getFileUrl() {
		if (null != id) {
			setShowDownloadBtn(true);
			return AWSS3DirectoryUtil.getCategoryImageUrl(id, fileName);
		}
		setShowDownloadBtn(false);
		return "";
	}

}
