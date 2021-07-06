package ru.mephi3.reposotory;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.User;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

	public Optional<User> findOneByUsername(String username);

	@Modifying
	@Transactional(readOnly = false)
	@Query("UPDATE User SET password = ?2 where id = ?1")
	public void updatePassword (Integer id, String value);
	
	@Modifying
	@Transactional(readOnly = false)
	@Query("UPDATE User SET lastLogin = ?2 where id = ?1")
	public void updateLastLogin (Integer id, LocalDateTime value);
}
