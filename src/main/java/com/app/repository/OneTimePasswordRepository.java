package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.app.config.WriteableRepository;
import com.app.entity.OneTimePassword;
import com.app.enumeration.Status;

public interface OneTimePasswordRepository extends WriteableRepository<OneTimePassword, Long> {

	public Optional<OneTimePassword> findByUserId(UUID userId);
	
	public Optional<OneTimePassword> findByMobileNoAndStatus(String mobileNo, Status status);
	//neww
	@Query("SELECT otp FROM OneTimePassword otp WHERE otp.mobileNo = :userNameOrEmail or otp.userId=:userNameOrEmail")
	Optional<OneTimePassword> findByUserNameOrEmail(String userNameOrEmail);

	public List<OneTimePassword> findByOtp(String otp);
}
