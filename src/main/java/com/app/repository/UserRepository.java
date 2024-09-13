package com.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.config.WriteableRepository;
import com.app.dto.ProfileMobileDTO;
import com.app.dto.UserView;
import com.app.dto.UserViewDTO;
import com.app.entity.User;
import com.app.enumeration.UserRoleType;

public interface UserRepository extends WriteableRepository<User, UUID> {

	@Query("SELECT s FROM User s WHERE s.id = :id")
	Optional<User> findUserById(@Param("id") UUID id);

	@Query("SELECT u FROM User u where u.userName = ?#{principal?.phoneNo}")
	public Optional<User> findLoggedInUser();

	public Optional<User> findByUserNameOrEmail(String value1, String value2);

	//neww
	@Query("SELECT u FROM User u WHERE u.userName = :userNameOrEmail or u.email=:userNameOrEmail or u.mobileNo=:userNameOrEmail")
	Optional<User> findByUserNameOrPassword(String userNameOrEmail);

	public Optional<User> findByUserNameAndPasswordAndIsDeletedFalseAndIsLockedFalse(String userName,
			String encryptedPassword);
	
	@Query(value = "SELECT * FROM users u where u.user_name =:userName and u.password =:encryptedPassword", nativeQuery = true)
	Optional<User> findByPasswordAndIsDeletedFalseAndIsLockedFalse(String encryptedPassword,String userName);

	@Query(value = "SELECT * FROM users u where u.user_name =:userName", nativeQuery = true)
	Optional<User> findByUserName(String userName);

	@Query(value = "SELECT u.user_id,u.full_name,u.user_name,u.phone_no,u.email,u.user_type,u.user_role from users u inner join roles r on u.user_role=r.id where user_id=:userId", nativeQuery = true)
	Optional<User> getById(UUID userId);

	@Query(value = "SELECT * FROM users u where user_id=:userId and phone_no=:mobileNo", nativeQuery = true)
	Optional<ProfileMobileDTO> findByMobileNo(String mobileNo,UUID userId);
			
	@Query(value = "SELECT * FROM users u where reference_id=:referenceId", nativeQuery = true)
	Optional<User> findByReferenceId(String referenceId);

	@Query(value = "SELECT * FROM users u where u.user_type='ADMIN' or u.user_type='USER' and u.status='ACTIVE' or u.status='INACTIVE'", nativeQuery = true)
	List<User> findAllUsers();	   

	@Query(value = "SELECT * FROM users u where u.phone_no=:mobileNo and u.user_type='USER'", nativeQuery = true)
	Optional<User> getByMobileNo(String mobileNo);
	
	@Query(value = "SELECT * FROM users u where u.email=:userEmail and u.user_type='USER'", nativeQuery = true)
	Optional<User> findByUserEmail(String userEmail);
	
	@Query(value = "SELECT u.user_id as userId,u.full_name as fullName,u.user_name as userName,u.phone_no as mobileNo,"
			+ "u.email as email,u.user_type as usertype,u.user_role as userRole from users u inner join roles r on u.user_role=r.id where u.reference_id=:referenceId", nativeQuery = true)
	List<UserView> findByVendorStaffs(String referenceId);

	@Query(value = "SELECT * FROM users u where u.user_type=:admin", nativeQuery = true)
	Optional<User> findByUserRoleType(String admin);	
	
    @Query(value="SELECT * FROM users u where u.user_name=:userName",nativeQuery = true)
	Optional<User> findByDuplicateUserName(String userName);
    
    @Query(value = "SELECT * FROM users u where u.phone_no=:phoneNumber and (u.user_type='VENDOR_ADMIN' or u.user_type='VENDOR_STAFF')", nativeQuery = true)
	Optional<User> findByVendorMobileNo(String phoneNumber);
    
    @Query(value = "SELECT * FROM users u where u.email=:email and (u.user_type='VENDOR_ADMIN' or u.user_type='VENDOR_STAFF')", nativeQuery = true)
	Optional<User> findByVendorStaffEmail(String email);
    
    @Query(value = "SELECT * FROM users u where u.phone_no=:mobileNo and u.user_type='CUSTOMER'", nativeQuery = true)
	Optional<User> getByMobileNos(String mobileNo);
  
	@Query(value = "SELECT * FROM users u where u.user_name =:name and u.phone_no=:mobileNo and u.user_type='CUSTOMER'", nativeQuery = true)
	Optional<User> findByUserName(String name, String mobileNo);	

    @Query(value="SELECT * FROM users u where u.email=:email and u.user_type='CUSTOMER' ",nativeQuery = true)
   	Optional<User> findByDuplicateEmail(String email);

	User findByEmail(String email);

	User findByMobileNo(String mobileNo);

	List<User> findByEmailAndUserRole(String email, UserRoleType userRole);

	Optional<User> findByUserId(UUID id);

	List<User> findByMobileNoAndUserRole(String mobileNo, UserRoleType userRole);

	@Query(value = "SELECT * FROM users u where u.phone_no =:mobileNo", nativeQuery = true)
	Optional<User> findByUsersName(String mobileNo);


}
