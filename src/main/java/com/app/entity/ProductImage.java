package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.ProductImageType;
import com.app.util.file.AWSS3DirectoryUtil;
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
@Entity
@Getter
@Setter
@Table(name="product_images")
public class ProductImage extends RecordModifier implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product productObj;
	
	@Transient
	private UUID productId;
	
	@ApiModelProperty(value = "Valid image type", required = true, allowableValues = "LISTING, DETAILS")
	@Enumerated(EnumType.STRING)
	@Column(name="product_image_type")
	private ProductImageType productImageType ;
	 
	@Column(name="file_name")
	private String fileName;
	
	@Transient
	private String fileBase64;
	
	@Transient
	private String fileUrl;
	
	@Transient
	private Boolean showDownloadBtn =false;
	
	public String getFileUrl() {
		if(null != id) {
			setShowDownloadBtn(true);
			return AWSS3DirectoryUtil.getProductImageUrl(productObj.getId(), fileName);
		}
		setShowDownloadBtn(false);
		return "";
	}
  
}
