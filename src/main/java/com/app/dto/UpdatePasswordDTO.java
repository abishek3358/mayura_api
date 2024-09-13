package com.app.dto;

public class UpdatePasswordDTO {
	
	private String otp;
	
	private String id;
	
	private String newPassword;
	
	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}

	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
