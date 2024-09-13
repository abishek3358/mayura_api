package com.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.UserDAO;
import com.app.dto.AdminDTO;
import com.app.dto.ProfileMobileDTO;
import com.app.dto.UserView;
import com.app.dto.UserViewDTO;
import com.app.entity.OneTimePassword;
import com.app.entity.User;
import com.app.enumeration.UserRoleType;
import com.app.repository.OneTimePasswordRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class UserService implements UserDetailsService {

	private @NonNull UserRepository userRepository;

	private @NonNull RoleRepository roleRepository;

	private @NonNull OneTimePasswordRepository otpRepository;
	
	private @NonNull UserDAO userDAO ;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUserName(username);
		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		User user = userOptional.get();
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public Optional<User> findById(UUID userId) {
		return userRepository.findById(userId);
	}

	public void deleteById(UUID userId) {
		userRepository.deleteById(userId);
	}

	public Optional<User> findByUserNameAndPasswordAndIsDeletedFalseAndIsLockedFalse(String username,
			String encryptedPassword) {

		return userRepository.findByUserNameAndPasswordAndIsDeletedFalseAndIsLockedFalse(username, encryptedPassword);
	}

	public void saveOrUpdate(User userObj) {
		/*
		 * OneTimePassword oneTimePassword =
		 * OneTimePassword.builder().mobileNo(userObj.getMobileNo()).otp("12345")
		 * .userId(userObj.getUserId()).build();
		 * otpRepository.saveAndFlush(oneTimePassword);
		 */

		userRepository.saveAndFlush(userObj);

	}

	public Optional<User> findByPasswordAndIsDeletedFalseAndIsLockedFalse(String encryptedPassword, String userName) {
		return userRepository.findByPasswordAndIsDeletedFalseAndIsLockedFalse(encryptedPassword, userName);
	}

	public Optional<User> findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public Optional<User> findByUserEmail(String userEmail) {
		return userRepository.findByUserEmail(userEmail);
	}

	public List<UserViewDTO> getAll() {
		List<User> userList = userRepository.findAllUsers();
		List<UserViewDTO> userDtoList = new ArrayList<UserViewDTO>();
		UserViewDTO response = null;
		// if(userList.get(0).getStatus().equals("ACTIVE")) {
		for (User userObj : userList) {
			response = new UserViewDTO();
			response.setUserId(userObj.getUserId());
			response.setUserRole(userObj.getUserRole());
			response.setEmail(userObj.getEmail());
			response.setFullName(userObj.getFullName());
			response.setUserName(userObj.getUserName());
			response.setMobileNo(userObj.getMobileNo());
			response.setStatus(userObj.getStatus());
			response.setRoleObj(userObj.getRoleObj());
			userDtoList.add(response);

		}
		return userDtoList;
	}

	public UserViewDTO getById(UUID userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		User userObj = userOptional.get();
		UserViewDTO response = new UserViewDTO();
		response.setUserId(userObj.getUserId());
		response.setUserRole(userObj.getUserRole());
		response.setEmail(userObj.getEmail());
		response.setRoleObj(userObj.getRoleObj());
		response.setFullName(userObj.getFullName());
		response.setUserName(userObj.getUserName());
		response.setMobileNo(userObj.getMobileNo());
		response.setStatus(userObj.getStatus());
		return response;
	}

	public Optional<ProfileMobileDTO> findByMobileNo(String mobileNo, UUID userId) {
		return userRepository.findByMobileNo(mobileNo, userId);
	}

	public Optional<User> getByMobileNo(String mobileNo) {
		return userRepository.getByMobileNo(mobileNo);
	}

	public Optional<User> findByReferenceId(UUID referenceId) {
		return userRepository.findByReferenceId(referenceId.toString());
	}

	public List<UserView> findByVendorStaffs(UUID referenceId) {
		return userRepository.findByVendorStaffs(referenceId.toString());
	}

	public Optional<User> findByUserRoleType(String admin) {
		return userRepository.findByUserRoleType(admin);
	}

	public Optional<User> findByDuplicateUserName(String userName) {
		return userRepository.findByDuplicateUserName(userName);
	}

	public Optional<User> findByVendorMobileNo(String phoneNumber) {
		return userRepository.findByVendorMobileNo(phoneNumber);
	}

	public Optional<User> findByVendorStaffEmail(String email) {
		return userRepository.findByVendorStaffEmail(email);
	}

	public Optional<User> findByDuplicateEmail(String email) {
		return userRepository.findByDuplicateEmail(email);
	}

	public List<User> getVendorStaff() {
		
		return userDAO.getVendorStaff();
	}

	public void UpdateAdmin(AdminDTO request) {
	 
	    User user = userRepository.findByUserId(request.getId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

	    List<User> usersWithSameEmail = userRepository.findByEmailAndUserRole(request.getEmail(), request.getUserRole());

	    usersWithSameEmail.remove(user);

	    if (!usersWithSameEmail.isEmpty()) {
	        throw new IllegalArgumentException("Email already exists.");
	    }
	    List<User> usersWithSameMobileNo = userRepository.findByMobileNoAndUserRole(request.getMobileNo(), request.getUserRole());

	    usersWithSameMobileNo.remove(user);

	    if (!usersWithSameMobileNo.isEmpty()) {
	        throw new IllegalArgumentException(" Mobile number already exists.");
	    }
	    user.setFullName(request.getFullName());
	    user.setEmail(request.getEmail());
	    user.setMobileNo(request.getMobileNo());
	    user.setStatus(request.getStatus());

	    userRepository.save(user);
	}
	
}

