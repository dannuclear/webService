package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.Role;
import ru.mephi3.domain.User;

@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Page<Role> findByUsers(User user, Pageable pageable);
}
