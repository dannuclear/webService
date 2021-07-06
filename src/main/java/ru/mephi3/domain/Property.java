package ru.mephi3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "PROPERTY")
public class Property {
	@Id
	@Column(name = "ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@ManyToOne
	@JoinColumn(name = "ID_PROPERTY_TYPE", referencedColumnName = "ID")
	private PropertyType propertyType;
	@ManyToOne
	@JoinColumn(name = "ID_UNIT", referencedColumnName = "ID")
	private Unit unit;
}
