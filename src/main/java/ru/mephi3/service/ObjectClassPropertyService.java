package ru.mephi3.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.mephi3.domain.ObjectClass;
import ru.mephi3.domain.ObjectClassProperty;

public interface ObjectClassPropertyService {

	ObjectClassProperty create(String code);

	ObjectClassProperty save(ObjectClassProperty objectClassProperty);

	Optional<ObjectClassProperty> findById(Integer id);

	Page<ObjectClassProperty> findAll(Pageable pageable);

	Page<ObjectClassProperty> findByString(String value, Pageable pageable);

	void delete(ObjectClassProperty objectClassProperty);

	Page<ObjectClassProperty> findByObjectClassAndFilterString(ObjectClass objectClass, String filter, Pageable pageable);
}
