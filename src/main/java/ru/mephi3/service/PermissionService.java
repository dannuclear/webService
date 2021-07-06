package ru.mephi3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.mephi3.domain.Permission;
import ru.mephi3.domain.User;

public interface PermissionService {
	
	Permission save(Permission Permission);

	Permission findById(Integer id);

	Page<Permission> findAll(Pageable pageable);

	Permission create();

	Page<Permission> findByString(String value, Pageable pageable);

	void delete(Permission Permission);
	
	Page<Permission> findByUsers(User user, Pageable pageable);
}
