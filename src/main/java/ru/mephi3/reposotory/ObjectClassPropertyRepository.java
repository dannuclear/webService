package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.ObjectClass;
import ru.mephi3.domain.ObjectClassProperty;

@Transactional(readOnly = true)
public interface ObjectClassPropertyRepository extends JpaRepository<ObjectClassProperty, Integer> {

	@EntityGraph(attributePaths = { "property.unit", "property.propertyType", "objectClass" })
	Page<ObjectClassProperty> findByObjectClassAndPropertyNameContains(ObjectClass objectClass, String filterString, Pageable pageable);
}
