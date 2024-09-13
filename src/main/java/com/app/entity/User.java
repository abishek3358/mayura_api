package com.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.UserRoleType;
import com.app.util.PasswordUtil;
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
@Table(name="users")
public class User extends RecordModifier  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "user_id", updatable = false, nullable = false)
	private UUID userId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="user_type")
	private UserRoleType userRole;
	
	@Column(name="user_name")
	private String userName;
	
	@Type(type = "uuid-char")
	@Column(name = "reference_id")
	private UUID referenceId;
	
	@JsonIgnore
	@Column(name="password")
	private String password; 
	
	@Column(name="full_name")
	private String fullName;
	
	@Column(name="email")
	private String email; 
	
	@Column(name="phone_no")
	private String mobileNo;	
	
	@OneToOne()
	@JoinColumn(name = "user_role")
	private Role roleObj;
	
	
	@Column(name = "force_password_change")
	private Boolean forcePasswordChange;
	
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
	@Column(name = "is_locked")
	private Boolean isLocked;

	@Column(name = "otp_verified")
	private Boolean isOtpVerified;
	
	@JsonIgnore
	@Column(name="deleted_on")
	private	Date deletedOn;
	 
	@JsonIgnore
	@Column(name="deleted_by")
	private	String deletedBy;
	
	@Transient
	@NotNull(message = "Password")
    @ApiModelProperty(value = "Valid password", required = true, allowableValues = "String")
	private String reqPassword; 	
		 
	@Transient
	private UUID roleId;
	
	@Column(name = "file_name")
	private String fileName;

	@Transient
	private String fileBase64;	

	@Transient
	private String fileUrl;
	
	@Transient
	private Boolean showDownloadBtn =false;

	
	public String getFileUrl() {
		if (null != userId) {
			setShowDownloadBtn(true);
			return AWSS3DirectoryUtil.getCategoryImageUrl(userId, fileName);
		}
		setShowDownloadBtn(false);
		return "";
	}

	 
	public void setAndEncryptPassword(String password) {
		setPassword(PasswordUtil.getEncryptedPassword(password));
	}
	 
	public UUID getRoleId() {
		if(userId != null && roleObj != null) {
			roleId = roleObj.getId();
		}
		return roleId;
	}	
	
	
	

	
}
