package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Customer;
import com.app.entity.OneTimePassword;
import com.app.entity.ProfileMobile;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.enumeration.MobileType;
import com.app.enumeration.Status;

import com.app.enumeration.UserRoleType;

import com.app.repository.CustomerRepository;
import com.app.repository.OneTimePasswordRepository;
import com.app.repository.ProfileMobileRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.util.IConstant;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class CustomerService {

	private @NonNull CustomerRepository customerRepository;

	private @NonNull OneTimePasswordRepository otpRepository;

	private @NonNull RoleRepository roleRepository;

	private @NonNull UserRepository userRepository;

	private @NonNull UserService userService;
	
	private @NonNull ProfileMobileRepository profileMobileRepository;


	public void saveOrUpdate(Customer obj) throws Exception {
		customerRepository.saveAndFlush(obj);
	}

	@Transactional
	public User doSignUp(Customer obj) {

		Optional<Role> role = roleRepository.findByRoleName(IConstant.ROLE_CUSTOMER);
		customerRepository.saveAndFlush(obj);

		OneTimePassword oneTimePassword = OneTimePassword.builder().mobileNo(obj.getMobileNo()).otp("12345")
				.userId(obj.getId()).build();
		otpRepository.saveAndFlush(oneTimePassword);
		
		User user = User.builder().email(obj.getEmail()).fullName(obj.getFullName()).forcePasswordChange(false)
				.isDeleted(false).isLocked(false).isOtpVerified(false).mobileNo(obj.getMobileNo())				
				.userRole(UserRoleType.CUSTOMER).roleObj(role.get()).userName(obj.getUserName())
				.password(obj.getPassword()).referenceId(obj.getId()).build();
		userRepository.saveAndFlush(user);
		
		ProfileMobile profileMobile=ProfileMobile.builder().customerId(user.getReferenceId())
				.title(MobileType.PRIMARY).mobileNo(obj.getMobileNo()).build();
		profileMobileRepository.saveAndFlush(profileMobile);
		return user;
	}

	@Transactional
	public void validateCustomer(User user, OneTimePassword oneTimePassword) {

		oneTimePassword.setStatus(Status.INACTIVE);
		otpRepository.saveAndFlush(oneTimePassword);

		user.setIsOtpVerified(true);
		userRepository.saveAndFlush(user);
		return;
	}

	@Transactional
	public void validateIsDeleted(User user) {
		user.setIsDeleted(true);
		userRepository.saveAndFlush(user);
		return;
	}

	public Optional<Customer> findById(UUID id) {
		return customerRepository.findById(id);
	}

	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	public Optional<Customer> findByEmailOrMobileNo(String email, String mobileNo) {
		return customerRepository.findByEmailOrMobileNo(email, mobileNo);
	}

	public Optional<Customer> findByEmailOrMobileNoExcludeId(String email, String mobileNo, UUID excludeId) {
		return customerRepository.findByEmailOrMobileNoExcludeId(email, mobileNo, excludeId);
	}

	public Optional<Customer> findByFullName(String fullName) {
		return customerRepository.findByFullName(fullName);
	}

	public Optional<Customer> findByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	public Optional<Customer> findByFullNameExcludeId(String fullName, UUID id) {
		return customerRepository.findByFullNameExcludeId(fullName, id);
	}

	public Optional<User> findByUserNameOrPassword(String userNameOrEmail) {
		return userRepository.findByUserNameOrPassword(userNameOrEmail);
	}
}