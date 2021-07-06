package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.Permission;
import ru.mephi3.domain.User;

@Transactional(readOnly = true)
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
	public Page<Permission> findByUsers(User user, Pageable pageable);

}
