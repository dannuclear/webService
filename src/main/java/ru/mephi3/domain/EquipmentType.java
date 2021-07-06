package ru.mephi3.domain;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EQUIPMENT_TYPE")
@Relation(collectionRelation = "equipmentTypes")
public class EquipmentType {

	@Id
	@Column(name = "ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "SHORT_NAME")
	private String shortName;
}
