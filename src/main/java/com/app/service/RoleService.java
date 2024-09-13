package com.app.service;

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

import com.app.entity.CustomerDeliveryAddress;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class RoleService {

	private @NonNull RoleRepository roleRepository;

	public void saveOrUpdate(Role obj) {
		roleRepository.saveAndFlush(obj);
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public void save(Role obj) {
		roleRepository.saveAndFlush(obj);
	}

	public Optional<Role> findByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}

	public Optional<Role> findByRoleNameAndIdNot(String roleName, UUID id) {
		return roleRepository.findByRoleNameAndIdNot(roleName, id);
	}
}
