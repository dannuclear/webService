package ru.mephi3.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OBJECT_CLASS")
public class ObjectClass {

	@Id
	@Column(name = "ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "OKPD_CODE")
	private String okpdCode;
	@Column(name = "NAME")
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PARENT", referencedColumnName = "ID")
	private ObjectClass parent;
	@Transient
	private Collection<ObjectClass> childs;

	@OneToMany(mappedBy = "objectClass", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
	private Collection<ObjectClassProperty> properties;
}
