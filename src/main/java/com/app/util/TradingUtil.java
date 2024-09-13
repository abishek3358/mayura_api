package com.app.util;

import java.text.DecimalFormat;

import com.app.entity.TradingSetting;

public class TradingUtil {
	
	final static DecimalFormat bookingNoDecimalFormat = new DecimalFormat("000000");  
	final static DecimalFormat deliveryNoDecimalFormat = new DecimalFormat("0000");  
	final static DecimalFormat invoiceNoDecimalFormat = new DecimalFormat("00000");  
	final static DecimalFormat billNoDecimalFormat = new DecimalFormat("00000");  
	
	
	
	public static String getPOBillNo(TradingSetting tradingSetting ) {
    	return tradingSetting.getPoBillPrefix()+"-"+bookingNoDecimalFormat.format(tradingSetting.getPoBillSuffix());
    }
    
    public static String getPONo(TradingSetting tradingSetting ) {
    	 return tradingSetting.getPoPrefix()+"-"+bookingNoDecimalFormat.format(tradingSetting.getPoSuffix());
    }
    
    public static String getSONo(TradingSetting tradingSetting ) {
    	return tradingSetting.getSoPrefix()+"-"+bookingNoDecimalFormat.format(tradingSetting.getSoSuffix());
    }
    
    public static String getSOInvoiceNo(TradingSetting tradingSetting ) {
    	return tradingSetting.getSoInvoicePrefix()+"-"+bookingNoDecimalFormat.format(tradingSetting.getSoInvoiceSuffix());
    }
    
    
}
