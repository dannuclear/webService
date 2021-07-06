package ru.mephi3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.mephi3.domain.ObjectClass;

public interface ObjectClassService {

	ObjectClass create(String code);

	ObjectClass findByObjectClassname(String objectClassname);

	ObjectClass save(ObjectClass objectClass);

	Optional<ObjectClass> findById(Integer id);

	Page<ObjectClass> findAll(Pageable pageable);

	Page<ObjectClass> findByString(String value, Pageable pageable);

	void delete(ObjectClass objectClass);

	List<ObjectClass> findByParent(Integer integer);

	String getObjectClassTreeString(Integer leafId);
}
