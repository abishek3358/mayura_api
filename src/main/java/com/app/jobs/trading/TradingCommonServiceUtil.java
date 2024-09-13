package com.app.jobs.trading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.entity.StatusMaster;
import com.app.enumeration.BillType;
import com.app.enumeration.InvoiceType;
import com.app.enumeration.PurchaseOrderTabType;
import com.app.enumeration.SalesOrderTabType;
import com.app.enumeration.StatusType;
import com.app.service.StatusMasterService;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Component
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class TradingCommonServiceUtil implements InitializingBean {
	
	public static Map<PurchaseOrderTabType,String> poTabTypeMap=new HashMap<PurchaseOrderTabType, String>();
	public static Map<SalesOrderTabType,String> soTabTypeMap=new HashMap<SalesOrderTabType, String>();
	public static Map<BillType,String> billTypeMap=new HashMap<BillType, String>();
	public static Map<InvoiceType,String> invoiceTypeMap=new HashMap<InvoiceType, String>();
	
	
	
	private @NonNull StatusMasterService statusMasterService;
    
    private static Map<UUID, StatusMaster> statusMap = new HashMap<UUID, StatusMaster>();
    private static Map<String, StatusMaster> statusMapByName = new HashMap<String, StatusMaster>();
    
    public static StatusMaster getStatusObjByName(String name) {
    	return statusMapByName.get(name);
    }
    
    public static StatusMaster getStatusObjId(UUID id) {
    	return statusMap.get(id);
    }
    
    public static StatusMaster getStatusObjectByPOTabType(PurchaseOrderTabType tabType) {
    	return statusMapByName.get(poTabTypeMap.get(tabType));
    }
    
    public static StatusMaster getStatusObjectBySOTabType(SalesOrderTabType tabType) {
    	return statusMapByName.get(soTabTypeMap.get(tabType));
    }
    
    public static StatusMaster getStatusObjectByBillType(BillType billType) {
    	return statusMapByName.get(billTypeMap.get(billType));
    }
    
    public static StatusMaster getStatusObjectByInvoiceType(InvoiceType invoiceType) {
    	return statusMapByName.get(invoiceTypeMap.get(invoiceType));
    }
    
    public static List<StatusMaster> getAllStatusByStatusType(StatusType statusType) {
    	return statusMap.values().stream().filter(statusObj -> statusType.equals(statusObj.getStatusType()))
                .collect(Collectors.toList()); 
    }
    
	@Override
	public void afterPropertiesSet() throws Exception {
		run();
	}
	
    public void run() {
    	
    	poTabTypeMap.put(PurchaseOrderTabType.RECEIVED, "Order Received & QC Done");
    	poTabTypeMap.put(PurchaseOrderTabType.PLACED, "Order Placed");
    	poTabTypeMap.put(PurchaseOrderTabType.PAYMENTCOMPLETED, "Payment Completed & Closed");
    	poTabTypeMap.put(PurchaseOrderTabType.CANCELLED, "Cancelled");
    	poTabTypeMap.put(PurchaseOrderTabType.RETURNED, "QC Rejected & Returned");
    	
    	soTabTypeMap.put(SalesOrderTabType.RECEIVED, "Order Received");
    	soTabTypeMap.put(SalesOrderTabType.READYTODISPATCH, "Ready to Dispatch");
    	soTabTypeMap.put(SalesOrderTabType.DISPATCHED, "Dispatched");
    	soTabTypeMap.put(SalesOrderTabType.RETURNED, "Rejected & Returned");
    	soTabTypeMap.put(SalesOrderTabType.PAYMENTRECEIVED, "Payment Received & Closed");
    	soTabTypeMap.put(SalesOrderTabType.CANCELLED, "Canceled");
    	
    	billTypeMap.put(BillType.DRAFT, "Draft");
    	billTypeMap.put(BillType.PAID, "Paid");
    	billTypeMap.put(BillType.PARTIALLY_PAID, "Partially Paid");
    	
    	invoiceTypeMap.put(InvoiceType.DRAFT, "Draft");
    	invoiceTypeMap.put(InvoiceType.PAID, "Paid");
    	invoiceTypeMap.put(InvoiceType.PARTIALLY_PAID, "Partially Paid");
    	
    	
    	List<StatusMaster> statusList = statusMasterService.findAll();
    	Map<UUID, StatusMaster> statusMapTemp = new HashMap<UUID, StatusMaster>();
    	Map<String, StatusMaster> statusMapByNameTemp = new HashMap<String, StatusMaster>();
    	for (StatusMaster statusObj : statusList) {
    		statusMapTemp.put(statusObj.getId(), statusObj);
    		statusMapByNameTemp.put(statusObj.getName(), statusObj);
		}
    	statusMap.clear();
    	statusMap.putAll(statusMapTemp);
    	
    	statusMapByName.clear();
    	statusMapByName.putAll(statusMapByNameTemp);
    }
    
}
