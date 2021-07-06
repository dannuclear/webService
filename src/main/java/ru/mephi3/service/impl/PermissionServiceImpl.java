package ru.mephi3.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Permission;
import ru.mephi3.domain.User;
import ru.mephi3.reposotory.PermissionRepository;
import ru.mephi3.service.PermissionService;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public Permission save(Permission permission) {
		Permission res = permissionRepository.save(permission);
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
	public Permission findById(Integer id) {
		Permission permission = permissionRepository.findById(id).orElse(null);
		return permission;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasAnyRole('ADMIN')")
	public Page<Permission> findAll(Pageable pageable) {
		return permissionRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public void delete(Permission permission) {
		permissionRepository.delete(permission);
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public Permission create() {
		return null;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasAnyRole('ADMIN')")
	public Page<Permission> findByString(String value, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Permission> findByUsers(User user, Pageable pageable) {
		return permissionRepository.findByUsers(user, pageable);
	}
}
