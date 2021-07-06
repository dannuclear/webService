package ru.mephi3.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.mephi3.domain.User;

public interface UserService {

	User create(String username);

	User findByUsername(String username);

	User save(User user);

	Optional<User> findById(Integer id);

	Page<User> findAll(Pageable pageable);

	User create();

	Page<User> findByString(String value, Pageable pageable);

	void delete(User user);

	void updatePassword(Integer id, String value);

	void updateLastLogin(Integer id, LocalDateTime value);
}
