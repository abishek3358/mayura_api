package com.app.dto;

import java.util.UUID;




public interface UserView {

	UUID getuserId();

	String getuserRole();

	String getuserName();

	String getfullName();

	String getemail();

	String getmobileNo();

	String getuserType();

}
