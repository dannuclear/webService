package ru.mephi3.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_USER")
@EqualsAndHashCode(of = { "id" })
public class User implements UserDetails {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	@NotEmpty
	private String username;
	@Column(updatable = false)
	@NotEmpty(message = "Пароль не может быть пустым")
	private String password;
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	@Column(name = "IS_SUPERUSER")
	private Boolean isSuperuser;
	@Column(name = "DATE_JOINED")
	private LocalDate dateJoined;
	@Column(name = "LAST_LOGIN")
	private LocalDateTime lastLogin;
	@Transient
	private Collection<SimpleGrantedAuthority> authorities;

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "AUTH_USER_PERMISSIONS", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id") })
	private Set<Permission> permissions;

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "AUTH_USER_GROUPS", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
	private Set<Role> roles;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities == null) {
			authorities = permissions.stream().map(Permission::getCodename).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
			authorities.addAll(roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
		}
//		return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN, ROLE_USER, DOCUMENT_READ");
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public static User defaultUser() {
		return User.builder().id(0).isActive(false).permissions(new HashSet<Permission>()).roles(new HashSet<Role>()).isSuperuser(false).build();
	}
}
