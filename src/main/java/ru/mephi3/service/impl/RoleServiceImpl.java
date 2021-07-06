package ru.mephi3.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Role;
import ru.mephi3.reposotory.RoleRepository;
import ru.mephi3.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public Role save(Role role) {
		Role res = roleRepository.save(role);
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
	public Role findById(Integer id) {
		Role role = roleRepository.findById(id).orElse(null);
		return role;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasAnyRole('ADMIN')")
	public Page<Role> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public void delete(Role role) {
		roleRepository.delete(role);
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public Role create() {
		return null;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasAnyRole('ADMIN')")
	public Page<Role> findByString(String value, Pageable pageable) {
		return null;
	}

//	@Override
//	public Page<Role> findByUsers(User user, Pageable pageable) {
//		return roleRepository.findByUsers(user, pageable);
//	}
}
