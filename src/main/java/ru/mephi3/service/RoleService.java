package ru.mephi3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.mephi3.domain.Role;

public interface RoleService {
	
	Role save(Role Role);

	Role findById(Integer id);

	Page<Role> findAll(Pageable pageable);

	Role create();

	Page<Role> findByString(String value, Pageable pageable);

	void delete(Role Role);
}
