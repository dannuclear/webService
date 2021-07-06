package ru.mephi3.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name = "AUTH_PERMISSION")
@EqualsAndHashCode(of = { "id" })
public class Permission implements Serializable {

	@Id
	@Column(name = "id")
	@NotNull
	private Integer id;
	@Column
	private String name;
	@Column
	private String codename;

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "AUTH_USER_PERMISSIONS", joinColumns = { @JoinColumn(name = "permission_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private Set<User> users;
}
