package com.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.app.enumeration.Status;
import com.app.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class UploadObject extends RecordModifier  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Title cannot be blank")
    @ApiModelProperty(value = "Valid Title", required = true, allowableValues = "NonEmpty String")
	@Column(name="title")
	private String title;
	
	@NotNull(message = "Description cannot be blank")
    @ApiModelProperty(value = "Valid Description", required = true, allowableValues = "NonEmpty String")
	@Column(name="description")
	private String description;

	@Column(name="file_name")
	private String fileName;
	
	@Column(name="file_url")
	private String fileUrl;
	
	@Column(name="file_type")
	private String fileType;
	
	@Column(name="file_size")
	private Long fileSize;
	
	@JsonIgnore
	@Column(name="uploaded_on")
	private Date uploadedOn;
	
	@Transient
	private String uploadDate;
 
	public String getUploadDate() {
		if(null != uploadedOn) {
			uploadDate = DateUtil.getViewDateFormat(uploadedOn);
		}else {
			uploadDate = "";
		}
		return uploadDate;
	}
	
}
