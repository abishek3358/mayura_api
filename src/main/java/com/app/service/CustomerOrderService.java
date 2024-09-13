package com.app.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.CustomerOrdersDAO;
import com.app.dao.ProductBrandDAO;
import com.app.dto.AdminViewOrdersDTO;
import com.app.dto.AllocatedOrderSearchDTO;
import com.app.dto.CustomerDeliveryAddressDTO;
import com.app.dto.CustomerOrderDetailsDTO;
import com.app.dto.CustomerOrderItemsDTO;
import com.app.dto.CustomerOrderSummaryDTO;
import com.app.dto.VendorBranchInterface;
import com.app.dto.VendorDtlDTO;
import com.app.dto.VendorHandledOrderInterface;
import com.app.entity.AppSetting;
import com.app.entity.Brand;
import com.app.entity.City;
import com.app.entity.Country;
import com.app.entity.Customer;
import com.app.entity.CustomerDeliveryAddress;
import com.app.entity.CustomerOrder;
import com.app.entity.CustomerOrderItems;
import com.app.entity.FeedbackSetting;
import com.app.entity.Product;
import com.app.entity.ProductImage;
import com.app.entity.State;
import com.app.entity.VendorOrderMapping;
import com.app.enumeration.OrderStatus;
import com.app.enumeration.VendorOrderStatus;
import com.app.repository.AppSettingRepository;
import com.app.repository.CustomerOrderItemsRepository;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.ProductDtlRepository;
import com.app.repository.ProductImageRepository;
import com.app.repository.ProductRepository;
import com.app.repository.VendorOrderMappingRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerOrderService {

	private @NonNull CustomerOrderRepository customerOrderRepository;

	private @NonNull AppSettingRepository appSettingRepository;

	private @NonNull FileService fileUploadService;

	private @NonNull OrderNotificationService orderNotificationService;

	private @NonNull ProductImageRepository imageRepository;

	private @NonNull ProductDtlRepository productDtlRepository;

	private @NonNull ProductRepository productRepository;

	private @NonNull CityService cityService;

	private @NonNull StateService stateService;

	private @NonNull CountryService countryService;
	
	private @NonNull VendorOrderMappingService vendorOrderMappingService;

	private @NonNull VendorOrderMappingRepository vendorOrderMappingRepository;

	private @NonNull VendorBranchService vendorBranchService;

	private @NonNull BrandService brandService;

	private @NonNull CustomerOrderItemsRepository customerOrderItemsRepository;

	private @NonNull ProductBrandDAO brandDAO;

	private @NonNull CustomerDeliveryAddressService customerDeliveryAddressService;

	private @NonNull CustomerOrdersDAO customerOrdersDao;

	@Transactional
	public synchronized CustomerOrder saveOrUpdate(CustomerOrder obj) throws Exception {
		AppSetting appSetting = appSettingRepository.findAll().get(0);
		Integer orderNo = appSetting.getOrderSuffix();
		obj.setOrderNo(appSetting.getOrderPrefix() + "" + orderNo);
		customerOrderRepository.saveAndFlush(obj);
		appSetting.setOrderSuffix(orderNo + 1);
		appSettingRepository.save(appSetting);
		return obj;
	}

	public Optional<CustomerOrder> findById(UUID id) {
		return customerOrderRepository.findById(id);
	}
	public List<CustomerOrder> findAll() {
		return customerOrderRepository.findAll();
	}
	public List<CustomerOrder> findByCustomerObj(Customer customer) {
		return customerOrderRepository.findByCustomerObj(customer);
	}	
	
	public Optional<CustomerOrder> getByCustomerOrderId(UUID customerOrderId) {
		return customerOrderRepository.getByCustomerOrderId(customerOrderId.toString());
	}
//	public List<CustomerOrder> findByOrderStatus(OrderStatus orderStatus,UUID customerId) {
//
//		return brandDAO.findByOrderStatus(orderStatus,customerId);
//	}

	

	public void updateOrderAsProgress(CustomerOrder obj) throws Exception {
		//orderNotificationService.outForDelivery(obj);
		customerOrderRepository.saveAndFlush(obj);
		VendorOrderMapping vendorOrderMapping = vendorOrderMappingRepository.findByCustomerOrderId(obj.getId()).get();
		vendorOrderMapping.setVendorOrderStatus(VendorOrderStatus.SHIPPED);
		vendorOrderMappingRepository.saveAndFlush(vendorOrderMapping);

	}

	public void updateOrderAsDelivered(CustomerOrder obj) throws Exception {
		//orderNotificationService.orderDelivered(obj);
		customerOrderRepository.saveAndFlush(obj);
		VendorOrderMapping vendorOrderMapping = vendorOrderMappingRepository.findByCustomerOrderId(obj.getId()).get();
		vendorOrderMapping.setVendorOrderStatus(VendorOrderStatus.DELIVERED);
		vendorOrderMappingRepository.saveAndFlush(vendorOrderMapping);

	}

	public void updateOrderAsCompleted(CustomerOrder obj) throws Exception {
		// orderNotificationService.orderDelivered(obj);
		customerOrderRepository.saveAndFlush(obj);
		VendorOrderMapping vendorOrderMapping = vendorOrderMappingRepository.findByCustomerOrderId(obj.getId()).get();
		vendorOrderMapping.setVendorOrderStatus(VendorOrderStatus.CLOSED);
		vendorOrderMappingRepository.saveAndFlush(vendorOrderMapping);

	}

	public void updateOrderAsCancelled(CustomerOrder obj) throws Exception {
		customerOrderRepository.saveAndFlush(obj);
		//orderNotificationService.orderCancelled(obj);
	}

	public CustomerOrderSummaryDTO findByCustomerOrderId(UUID customerOrderId) {

		Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findById(customerOrderId);
		

		CustomerOrder customerOrderObj = new CustomerOrder();
		customerOrderObj = customerOrderOptional.get();
		CustomerOrderSummaryDTO responseObj = new CustomerOrderSummaryDTO();
		responseObj.setCustomerOrderId(customerOrderObj.getId());
		responseObj.setOrderNo(customerOrderObj.getOrderNo());
		responseObj.setCustomerName(customerOrderObj.getCustomerObj().getFullName());
		responseObj.setMobileNumber(customerOrderObj.getCustomerObj().getMobileNo());
		responseObj.setEmailId(customerOrderObj.getCustomerObj().getEmail());
		responseObj.setPaymentMode(customerOrderObj.getPaymentMode());
		double orderQtyDouble = customerOrderObj.getCustomerOrderItemsList().get(0).getOrderQty();
		int orderQtyInt = (int) orderQtyDouble;
		responseObj.setOrderQty(orderQtyInt);
		responseObj.setDeliveryFee(customerOrderObj.getDeliveryFee());
		responseObj.setDiscAmount(customerOrderObj.getDiscAmount());
		responseObj.setSubTotal(customerOrderObj.getSubTotal());
		responseObj.setGrandTotal(customerOrderObj.getGrandTotal());
		responseObj.setOrderPlacedDate(customerOrderObj.getCreatedOn());
		responseObj.setActualDeliveryDate(customerOrderObj.getActualDeliveryDate());
		responseObj.setOrderAllocatedDate(customerOrderObj.getOrderAllocatedDate());
		responseObj.setOrderShippedDate(customerOrderObj.getOrderShippedDate());
		responseObj.setOrderDeliveredDate(customerOrderObj.getOrderDeliveredDate());
		responseObj.setOrderCompletedDate(customerOrderObj.getOrdercompletedDate());
		responseObj.setOrderCancelledDate(customerOrderObj.getOrderCancelledDate());
		responseObj.setOrderStatus(customerOrderObj.getOrderStatus());

		CustomerDeliveryAddress deliveryAddressObj = new CustomerDeliveryAddress();

		Optional<CustomerDeliveryAddress> customerDeliveryAddressOptional = customerDeliveryAddressService
				.findById(customerOrderObj.getDeliveryAddressId());
		deliveryAddressObj = customerDeliveryAddressOptional.get();

		CustomerDeliveryAddressDTO customerDeliveryAddressDtoObj = new CustomerDeliveryAddressDTO();
		customerDeliveryAddressDtoObj.setReceiverName(deliveryAddressObj.getName());
		customerDeliveryAddressDtoObj.setMobileNo(deliveryAddressObj.getMobileNo());
		customerDeliveryAddressDtoObj.setAddressType(deliveryAddressObj.getAddressType());
		customerDeliveryAddressDtoObj.setAddressLine1(deliveryAddressObj.getAddressLine1());
		customerDeliveryAddressDtoObj.setAddressLine2(deliveryAddressObj.getAddressLine2());
		customerDeliveryAddressDtoObj.setPostalCode(deliveryAddressObj.getPostalCode());
		City cityObj = new City();
		cityObj = cityService.findById(deliveryAddressObj.getCityObj().getId()).get();
		State stateObj = new State();
		stateObj = stateService.findById(deliveryAddressObj.getCityObj().getStateId()).get();
		Country countryObj = new Country();
		countryObj = countryService.findById(deliveryAddressObj.getCityObj().getCountryId()).get();
		customerDeliveryAddressDtoObj.setCityName(cityObj.getName());
		customerDeliveryAddressDtoObj.setStateName(stateObj.getName());
		customerDeliveryAddressDtoObj.setCountryName(countryObj.getName());
		customerDeliveryAddressDtoObj.setLatitude(deliveryAddressObj.getLatitude());
		customerDeliveryAddressDtoObj.setLocation(deliveryAddressObj.getLocation());
		customerDeliveryAddressDtoObj.setLongitude(deliveryAddressObj.getLongitude());
		responseObj.setDeliveryAddressDtoObj(customerDeliveryAddressDtoObj);
		
		List<CustomerOrderItemsDTO> customerOrderItemsDTOList = new ArrayList<CustomerOrderItemsDTO>();
		List<CustomerOrderItems> customerOrderItemsList = customerOrderItemsRepository
				.findByOrderItemList(customerOrderOptional.get());
		for (CustomerOrderItems itemObj : customerOrderItemsList) {
			CustomerOrderItemsDTO customerOrderItemsDtoObj = new CustomerOrderItemsDTO();
			customerOrderItemsDtoObj.setPriceWithUom("₹ " + itemObj.getOrderQuantityAmount());
			customerOrderItemsDtoObj.setProductName(itemObj.getProductObj().getProductName());
			Product productObj = productRepository.findById(itemObj.getProductObj().getId()).get();
			Brand brandObj = brandService.findById(productObj.getBrandId()).get();
			customerOrderItemsDtoObj.setBrandName(brandObj.getBrandName());
			customerOrderItemsDtoObj.setOrderQty(itemObj.getOrderQty());
			customerOrderItemsDtoObj.setPrice(itemObj.getPrice());	
			customerOrderItemsDtoObj.setOrderQtyAmount(itemObj.getOrderQuantityAmount());
			customerOrderItemsDtoObj.setDiscAmount(itemObj.getDiscAmount());			
			List<ProductImage> imageList = imageRepository.findByProductObj(itemObj.getProductObj());
			for (ProductImage imglst : imageList) {
				customerOrderItemsDtoObj.setImageUrl(imglst.getFileUrl());
			}			
			customerOrderItemsDtoObj.setDispPrice("₹ " + itemObj.getOrderQuantityAmount());
			
			customerOrderItemsDTOList.add(customerOrderItemsDtoObj);
		}
		responseObj.setCustomerOrderItemsDTOList(customerOrderItemsDTOList);

		return responseObj;
	}

	public CustomerOrderDetailsDTO findByCustomerOrderDetails(UUID customerOrderId) {

		Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findById(customerOrderId);

		CustomerOrder customerOrderObj = new CustomerOrder();
		customerOrderObj = customerOrderOptional.get();

		CustomerOrderDetailsDTO responseObj = new CustomerOrderDetailsDTO();

		FeedbackSetting feedbackSetting =  new FeedbackSetting() ;
		List<VendorDtlDTO> vendorDtlList = new ArrayList<VendorDtlDTO>();
		responseObj.setCustomerOrderId(customerOrderObj.getId());
		responseObj.setOrderNo(customerOrderObj.getOrderNo());
		responseObj.setCustomerName(customerOrderObj.getCustomerObj().getFullName());
		responseObj.setCustomerId(customerOrderObj.getCustomerObj().getId());
		responseObj.setMobileNumber(customerOrderObj.getCustomerObj().getMobileNo());
		responseObj.setEmailId(customerOrderObj.getCustomerObj().getEmail());
		responseObj.setSubTotal(customerOrderObj.getSubTotal());
		responseObj.setGrandTotal(customerOrderObj.getGrandTotal());
		responseObj.setOrderStatus(customerOrderObj.getOrderStatus());
		responseObj.setPaymentMode(customerOrderObj.getPaymentMode());
		responseObj.setNoOfProducts(customerOrderObj.getNoOfProducts());
		responseObj.setDeliveryDate(customerOrderObj.getActualDeliveryDate());
		responseObj.setEstimateDate(customerOrderObj.getEstimatedDeliveryDate());
		responseObj.setOrderDate(customerOrderObj.getCreatedOn());
		List<CustomerOrderItems> customerOrderItemsList = customerOrderItemsRepository
				.findByOrderItemList(customerOrderOptional.get());
		Double orderQuantity = 0.0;
		for (CustomerOrderItems customerOrderItems : customerOrderItemsList) {
			 orderQuantity += customerOrderItems.getOrderQty();
		    responseObj.setQuantity(orderQuantity);
		}
		CustomerDeliveryAddress deliveryAddressObj = new CustomerDeliveryAddress();
		CustomerDeliveryAddressDTO customerDeliveryAddressDtoObj = new CustomerDeliveryAddressDTO();
		Optional<CustomerDeliveryAddress> customerDeliveryAddressOptional = customerDeliveryAddressService
				.findById(customerOrderObj.getDeliveryAddressId());
		deliveryAddressObj = customerDeliveryAddressOptional.get();

		customerDeliveryAddressDtoObj.setReceiverName(deliveryAddressObj.getName());
		customerDeliveryAddressDtoObj.setMobileNo(deliveryAddressObj.getMobileNo());
		customerDeliveryAddressDtoObj.setAddressType(deliveryAddressObj.getAddressType());
		customerDeliveryAddressDtoObj.setAddressLine1(deliveryAddressObj.getAddressLine1());
		customerDeliveryAddressDtoObj.setAddressLine2(deliveryAddressObj.getAddressLine2());
		customerDeliveryAddressDtoObj.setLandMark(deliveryAddressObj.getLandMark());
		customerDeliveryAddressDtoObj.setPostalCode(deliveryAddressObj.getPostalCode());

		City cityObj = new City();
		cityObj = cityService.findById(deliveryAddressObj.getCityObj().getId()).get();
		State stateObj = new State();
		stateObj = stateService.findById(deliveryAddressObj.getCityObj().getStateId()).get();
		Country countryObj = new Country();
		countryObj = countryService.findById(deliveryAddressObj.getCityObj().getCountryId()).get();

		customerDeliveryAddressDtoObj.setCityName(cityObj.getName());
		customerDeliveryAddressDtoObj.setStateName(stateObj.getName());
		customerDeliveryAddressDtoObj.setCountryName(countryObj.getName());
		customerDeliveryAddressDtoObj.setLandMark(deliveryAddressObj.getLandMark());
		customerDeliveryAddressDtoObj.setLatitude(deliveryAddressObj.getLatitude());
		customerDeliveryAddressDtoObj.setLocation(deliveryAddressObj.getLocation());
		customerDeliveryAddressDtoObj.setLongitude(deliveryAddressObj.getLongitude());
		responseObj.setDeliveryAddressDtoObj(customerDeliveryAddressDtoObj);

//		Double new_latitude=10.6286438;
//				
//		Double	new_longitude=79.2530889;

//		double radius = 0.00157; // Radius of the earth in 10 km
//		
//		Double new_latitude  = deliveryAddressObj.getLatitude()  + (1 / radius) * (180 / 3.14);
//		Double new_longitude = deliveryAddressObj.getLongitude() + (1/ radius) * (180 / 3.14) /
//				Math.cos(deliveryAddressObj.getLatitude() * 3.14/180);
//		
//		double lat_1=17.620682;
//		double long_1=78.522201;
//		double long_2=0.0;
//		double lat_2=0.0;
//		
//		double R = 0.00157; // Radius of the earth in km
//		
//		long_2=long_2-long_1;
//		lat_2=lat_2-lat_1;
//		lat_2 * (Math.PI/180);
//			  double dLat = deg2rad(lat_2);  // deg2rad below
//			  double dLon = deg2rad(long_2); 
//			  double a = 
//			    Math.sin(dLat/2) * Math.sin(dLat/2) +
//			    Math.cos(deg2rad(lat_1))  Math.cos(deg2rad(lat_2))  
//			    Math.sin(dLon/2) * Math.sin(dLon/2); 
//			  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
//			  double d = R * c; // Distance in km
//
////			  (pi/180) * r_earth = 111 km / degree 
////					  
////			(pi/180)  r_earth  cos(theta*pi/180)
////	
////			  new_latitude  = latitude  + (dy / r_earth) * (180 / pi);
////			  new_longitude = longitude + (dx / r_earth)  (180 / pi) / cos(latitude  pi/180);
////			  
//			  Point basePoint=new Point(long_1,lat_1);
//			  
//			  Distance radius= new Distance(d, Metrics.KILOMETERS);
//			  
//			  Circle  area=new Circle(basePoint,radius);
//			  		
//		List<VendorBranch> vendorBranchList=vendorBranchService.findByVendors(
//				deliveryAddressObj.getLatitude(),deliveryAddressObj.getLongitude(),
//				new_latitude,new_longitude);
		//UUID stateId = deliveryAddressObj.getStateObj().getId();
		
		UUID cityId = deliveryAddressObj.getCityObj().getId();
		if(cityObj.getId() !=null) {				
		List<VendorBranchInterface> vendorBranchList = vendorBranchService
				.findByVendors(cityObj.getId());
		for (VendorBranchInterface vendorBranchObj : vendorBranchList) {
			VendorDtlDTO vendorDtlDTOObj = new VendorDtlDTO();
			vendorDtlDTOObj.setId(vendorBranchObj.getId());
			vendorDtlDTOObj.setVendorName(vendorBranchObj.getVendorName());
			vendorDtlDTOObj.setVendorCode(vendorBranchObj.getVendorCode());
			vendorDtlDTOObj.setBranchAddress(vendorBranchObj.getBranchAddress());	
			
			List<VendorHandledOrderInterface> objlist = vendorOrderMappingService.findByVendorHandledOrder(vendorDtlDTOObj.getId()); 
			if(objlist.size()== 0) {
				vendorDtlDTOObj.setHandledOrders("0");
				vendorDtlDTOObj.setRatings("0.0");
				}			
			for (VendorHandledOrderInterface obj : objlist) {
				if (obj.getHandledOrders() == null){vendorDtlDTOObj.setHandledOrders("0");}
				else {vendorDtlDTOObj.setHandledOrders(obj.getHandledOrders());}				
				if( obj.getRatings() == null) {
					vendorDtlDTOObj.setRatings("0");
				}else {
					 DecimalFormat df = new DecimalFormat("#.#");
			            vendorDtlDTOObj.setRatings(df.format(Double.parseDouble(obj.getRatings())));
				}
				
			}		
			//vendorDtlDTOObj.setRatings(feedbackSetting.getOverallRatings());
			vendorDtlList.add(vendorDtlDTOObj);
		}
		responseObj.setVendorDtlList(vendorDtlList);
	}
		List<VendorBranchInterface> vendorBranchList = vendorBranchService.findByVendors(cityObj.getId());
		if (vendorBranchList.size() <= 0) {
			String stateId = "996e48c3-d724-447b-baec-4941fb1e98c6";
			List<VendorBranchInterface> vendorBranchLists = vendorBranchService.findByVendorsState(stateId);
			for (VendorBranchInterface vendorBranchObj : vendorBranchLists) {
				VendorDtlDTO vendorDtlDTOObj = new VendorDtlDTO();
				vendorDtlDTOObj.setId(vendorBranchObj.getId());
				vendorDtlDTOObj.setVendorName(vendorBranchObj.getVendorName());
				vendorDtlDTOObj.setVendorCode(vendorBranchObj.getVendorCode());
				vendorDtlDTOObj.setBranchAddress(vendorBranchObj.getBranchAddress());
				List<VendorHandledOrderInterface> objlist = vendorOrderMappingService
						.findByVendorHandledOrder(vendorDtlDTOObj.getId());
				if (objlist.size() == 0) {
					vendorDtlDTOObj.setHandledOrders("0");
					//vendorDtlDTOObj.setRatings("0.0");
				}
				for (VendorHandledOrderInterface obj : objlist) {
					if (obj.getHandledOrders() == null) {
						vendorDtlDTOObj.setHandledOrders("0");
					} else {
						vendorDtlDTOObj.setHandledOrders(obj.getHandledOrders());
					}
					/*
					 * if (obj.getRatings() == null) { vendorDtlDTOObj.setRatings("0.0"); } else {
					 * vendorDtlDTOObj.setRatings(obj.getRatings()); }
					 */
				}
				vendorDtlList.add(vendorDtlDTOObj);
			}
			responseObj.setVendorDtlList(vendorDtlList);

		}		
		return responseObj;		
	}
	public List<AdminViewOrdersDTO> findByConfirmedOrders(OrderStatus orderStatus) {
		return customerOrdersDao.findByConfirmedOrders(orderStatus);
	}

	public List<AdminViewOrdersDTO> findByMyOrderStatus(OrderStatus orderStatus, UUID referenceId) {
		return customerOrdersDao.findByMyOrderStatus(orderStatus,referenceId);

	}

	public List<CustomerOrder> findByOrderStatus(OrderStatus orderStatus) {
		return customerOrderRepository.findByOrderStatus(orderStatus);
	}

	/*
	 * public List<AdminViewOrdersDTO> findByProgressingOrders(OrderStatus
	 * orderStatus, AllocatedOrderSearchDTO request) { return
	 * customerOrdersDao.findByProgressingOrders(orderStatus, request); }
	 */
	public List<AdminViewOrdersDTO> findByDeliveredOrders(OrderStatus orderStatus, AllocatedOrderSearchDTO request) {
		return customerOrdersDao.findByDeliveredOrders(orderStatus,request);
	}

	public List<AdminViewOrdersDTO> findByAllOrderStatus(OrderStatus orderStatus, AllocatedOrderSearchDTO request) {
		return customerOrdersDao.findByAllOrderStatus(orderStatus,request);
	}

	public List<AdminViewOrdersDTO> findByCustomerConfirmedOrders(OrderStatus orderStatus) {		
		return customerOrdersDao.findByCustomerConfirmedOrders(orderStatus);
	}

	
	public List<AdminViewOrdersDTO> findByVendorRejectedOrders(VendorOrderStatus VendorOrderStatus) {		
		return customerOrdersDao.findByVendorRejectedOrders(VendorOrderStatus);
	}
	/*
	 * public List<AdminViewOrdersDTO> findByProgressingOrders(OrderStatus
	 * orderStatus, AllocatedOrderSearchDTO request) { return
	 * customerOrdersDao.findByProgressingOrders(orderStatus, request); }
	 */

	public List<AdminViewOrdersDTO> findByProgressingOrders(OrderStatus orderStatus, String vendorName, Date startDate,
			Date endDate) {
		
		return customerOrdersDao.findByProgressingOrders(orderStatus,vendorName,startDate,endDate);
	}

	

	


}
