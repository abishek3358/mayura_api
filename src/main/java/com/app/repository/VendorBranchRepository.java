package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.VendorBranchInterface;
import com.app.dto.VendorHandledOrderInterface;
import com.app.entity.City;
import com.app.entity.VendorBranch;


public interface VendorBranchRepository extends WriteableRepository<VendorBranch, UUID> {

	List<VendorBranch> findByVendorId(UUID transactionId);
	
	
    @Query("SELECT VB FROM VendorBranch VB WHERE VB.latitude between :start_latitude and :new_latitude "
    		+ "and VB.longitude between :start_longitude and :new_longitude ")
	List<VendorBranch> findByVendors(Double new_latitude, Double new_longitude,
			Double start_latitude, Double start_longitude);

    @Query(value="select v.id as id,v.name as vendorName,v.vendor_code as vendorCode,"
    		+ "vb.location as branchAddress from vendor_branch vb "
    		+ "left join vendor v on vb.vendor_id=v.id where vb.city_id=:cityId group by vb.id",nativeQuery=true)
	List<VendorBranchInterface> findByVendors(String cityId);

    @Query(value="select v.id as id,v.name as vendorName,v.vendor_code as vendorCode,"
    		+ "vb.location as branchAddress from vendor_branch vb "
    		+ "left join vendor v on vb.vendor_id=v.id where vb.state_id=:stateId group by vb.id",nativeQuery=true)
	List<VendorBranchInterface> findByVendorsState(String stateId);  

	//List<VendorBranchInterface> findByVendors(String string);   
}
