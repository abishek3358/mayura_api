package com.app.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.app.enumeration.OrderStatus;
import com.app.enumeration.VendorOrderStatus;

public class TextUtil {
	
	public static Boolean isContains(String description, String searchKey) {
		if(null != searchKey && (null != description && description.toLowerCase().contains(searchKey.toLowerCase()))) {
			return true;
		}
		return false;
	}
	public static Boolean isContainsInt(Integer description, Integer searchKey) {
		if(null != searchKey && null != description ) {
			return true;
		}
		return false;
	}
	
	public static Boolean isEmptyOrNull(String searchKey) {
		if(null == searchKey || searchKey.isEmpty()) {
			return true;
		}
		return false;
	}
	public static Boolean isEmptyOrNullInt(Integer searchKey) {
		if(null == searchKey ) {
			return true;
		}
		return false;
	}
	public static boolean isNull(OrderStatus orderStatus) {
		
		if(null == orderStatus ) {
			return true;
		}
		return false;
	}
	public static boolean isEmptyOrNull(UUID id) {
		if(null == id ) {
			return true;
		}
		
		return false;
	}
	public static boolean isEmptyOrNull(List<UUID> ids) {
		if(null == ids ) {
			return true;
		}
		
		
		return false;
	}
	public static boolean isEmptyOrNullDouble(List<Double> doublelist) {
		if(null == doublelist ) {
			return true;
		}
		
		return false;
	}
	public static boolean isEmptyOrNull(Double doubleValue) {
		
		if(null == doubleValue ) {
			return true;
		}
		return false;
	}
	public static boolean isEmptyOrNull(Integer integerValue) {

		if(null == integerValue ) {
			return true;
		}
		return false;
	}
	public static boolean isNull(VendorOrderStatus orderStatus) {
		if(null == orderStatus ) {
			return true;
		}
		return false;
	}
	public static boolean isNull(Date date) {
		if(null == date ) {
			return true;
		}
		return false;
	}
	   	 

 
}
