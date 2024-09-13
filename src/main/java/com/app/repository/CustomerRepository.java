package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.entity.Customer;

public interface CustomerRepository extends WriteableRepository<Customer, UUID> {

Optional<Customer> findByEmailOrMobileNo(String email,String mobileNo);

Customer findByMobileNo(String mobileNo);

@Query("SELECT WH FROM Customer WH WHERE 1=1 and (WH.email = :email or WH.mobileNo = :mobileNo) and WH.id <> :excludeId")
Optional<Customer> findByEmailOrMobileNoExcludeId(@Param("email") String email,@Param("mobileNo") String mobileNo, @Param("excludeId") UUID excludeId);

Optional<Customer> findByFullName(String fullName);

@Query("SELECT WH FROM Customer WH WHERE 1=1 and (WH.fullName = :fullName) and WH.id <> :excludeId")
Optional<Customer> findByFullNameExcludeId(@Param("fullName") String fullName, @Param("excludeId") UUID excludeId);

Optional<Customer> findByEmail(String email);



}