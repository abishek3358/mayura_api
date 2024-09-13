package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.dto.ProfileMobileDTO;
import com.app.entity.ProfileMobile;
import com.app.enumeration.MobileType;
import com.app.enumeration.Status;

public interface ProfileMobileRepository extends WriteableRepository<ProfileMobile, UUID> {
	
	List<ProfileMobile> findByCustomerId(UUID customerId);

	@Query("SELECT PM FROM ProfileMobile PM WHERE mobileNo=:mobileNo AND customerId=:customerId")	
	Optional<ProfileMobile> findByMobileNo(String mobileNo,UUID customerId);

	@Query("SELECT PM FROM ProfileMobile PM WHERE title=:title AND customerId=:customerId")
	Optional<ProfileMobile> findByMobileNo(MobileType title, UUID customerId);

	@Query("SELECT PM FROM ProfileMobile PM WHERE customerId=:referenceId AND status=:active")
	List<ProfileMobile> findByCustomerIdAndStatus(UUID referenceId, Status active);


}
