package com.app.entity;

import java.io.Serializable;
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

import com.app.util.file.AWSS3DirectoryUtil;

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
@Table(name="brand")
public class Brand extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;  
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull(message = "Brand name cannot be blank")
	@ApiModelProperty(value = "Brand name", required = true, allowableValues = "String")
	@Column(name="name")
	private String brandName;
	
	@NotNull(message = "description cannot be blank")
	@ApiModelProperty(value = "description", required = true, allowableValues = "String")
	@Column(name="description")
	private String description; 
	
	@Column(name = "file_name")
	private String fileName;

	@Transient
	private String fileBase64;

	@Transient
	private String fileUrl;
	

	@Transient
	private String productCount;
	
	@Transient
	private Boolean showDownloadBtn =false;

	public String getFileUrl() {
		if (null != id) {
			setShowDownloadBtn(true);
			return AWSS3DirectoryUtil.getBrandImageUrl(id, fileName);
		}
		setShowDownloadBtn(false);
		return "";
	}
}
