package com.app.repository;

import java.util.Optional;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.Role;

public interface RoleRepository extends WriteableRepository<Role, Long> {

	public Optional<Role> findByRoleName(String roleName);

	public Optional<Role> findByRoleNameAndIdNot(String roleName, UUID id);
}
