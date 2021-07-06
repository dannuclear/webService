package ru.mephi3.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.User;
import ru.mephi3.reposotory.UserRepository;
import ru.mephi3.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

	private static final String EMPTY_PASSWORD_PLACEHOLDER = "******";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User findByUsername(String username) {
		return userRepository.findOneByUsername(username).get();
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public User save(User user) {
		User res = userRepository.save(user);
		if (!EMPTY_PASSWORD_PLACEHOLDER.equals(user.getPassword()) && user.getId() != 0)
			updatePassword(user.getId(), passwordEncoder.encode(user.getPassword()));
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public User create(String username) {
		User user = new User();
		user.setUsername(username);
		return user;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
	public Optional<User> findById(Integer id) {
		Optional<User> optUser = userRepository.findById(id);
		if (optUser.isPresent())
			optUser.get().setPassword(EMPTY_PASSWORD_PLACEHOLDER);
		return optUser;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasAnyRole('ADMIN')")
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public User create() {
		return null;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_READ') or hasAnyRole('ADMIN')")
	public Page<User> findByString(String value, Pageable pageable) {
		return null;
	}

	@Override
	@PreAuthorize("hasAuthority('USER_EDIT') or hasAnyRole('ADMIN')")
	public void updatePassword(Integer id, String value) {
		userRepository.updatePassword(id, value);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOpt = userRepository.findOneByUsername(username);
		if (!userOpt.isPresent())
			throw new UsernameNotFoundException("Пользователь не найден");
		User user = userOpt.get();
		user.getAuthorities();
		return user;
	}
	
	@Override
	public void updateLastLogin (Integer id, LocalDateTime value) {
		userRepository.updateLastLogin(id, value);
	}
}
