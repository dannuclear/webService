package ru.mephi3.rest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Permission;
import ru.mephi3.domain.Role;
import ru.mephi3.domain.User;
import ru.mephi3.dto.PrincipalDTO;
import ru.mephi3.reposotory.PermissionRepository;
import ru.mephi3.reposotory.RoleRepository;
import ru.mephi3.service.UserService;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserRestController {

	private final UserService userService;
	private final PermissionRepository permissionRepository;
	private final RoleRepository roleRepository;

	@PostMapping("/users")
	public ResponseEntity<DataTablesResponse<User>> main(@RequestBody DataTablesRequest dataTablesRequest) {
		Page<User> usersPage = userService.findAll(dataTablesRequest.getPageRequest());
		return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), usersPage));
	}

	@GetMapping("/users/{userId}/permissions")
	public ResponseEntity<DataTablesResponse<Permission>> findPerms(@PathVariable("userId") Integer userId, Pageable pageable, DataTablesRequest dataTablesRequest) {
		User user = userService.findById(userId).get();
		return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), permissionRepository.findByUsers(user, pageable)));
	}

	@GetMapping("/users/{userId}/roles")
	public ResponseEntity<DataTablesResponse<Role>> findRoles(@PathVariable("userId") Integer userId, Pageable pageable, DataTablesRequest dataTablesRequest) {
		User user = userService.findById(userId).get();
		return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), roleRepository.findByUsers(user, pageable)));
	}

	@GetMapping("/users/current")
	public ResponseEntity<PrincipalDTO> main(Principal principal) {
		return ResponseEntity.ok(PrincipalDTO.fromPrincipal(principal));
	}
}