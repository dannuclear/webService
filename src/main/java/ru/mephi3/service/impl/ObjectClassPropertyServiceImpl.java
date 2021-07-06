package ru.mephi3.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.ObjectClass;
import ru.mephi3.domain.ObjectClassProperty;
import ru.mephi3.reposotory.ObjectClassPropertyRepository;
import ru.mephi3.service.ObjectClassPropertyService;

@Service
@RequiredArgsConstructor
public class ObjectClassPropertyServiceImpl implements ObjectClassPropertyService {

	private final ObjectClassPropertyRepository objectClassPropertyRepository;

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_PROPERTY_EDIT') or hasAnyRole('ADMIN')")
	public ObjectClassProperty save(ObjectClassProperty objectClassProperty) {
		ObjectClassProperty res = objectClassPropertyRepository.save(objectClassProperty);
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_PROPERTY_EDIT') or hasAnyRole('ADMIN')")
	public ObjectClassProperty create(String code) {
		ObjectClassProperty objectClassProperty = new ObjectClassProperty();
		return objectClassProperty;
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_PROPERTY_READ') or hasRole('ADMIN')")
	public Optional<ObjectClassProperty> findById(Integer id) {
		Optional<ObjectClassProperty> optObjectClassProperty = objectClassPropertyRepository.findById(id);
		return optObjectClassProperty;
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_PROPERTY_READ') or hasAnyRole('ADMIN')")
	public Page<ObjectClassProperty> findAll(Pageable pageable) {
		return objectClassPropertyRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_PROPERTY_EDIT') or hasAnyRole('ADMIN')")
	public void delete(ObjectClassProperty objectClassProperty) {
		objectClassPropertyRepository.delete(objectClassProperty);
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_PROPERTY_READ') or hasAnyRole('ADMIN')")
	public Page<ObjectClassProperty> findByString(String value, Pageable pageable) {
		return null;
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_PROPERTY_READ') or hasAnyRole('ADMIN')")
	public Page<ObjectClassProperty> findByObjectClassAndFilterString(ObjectClass objectClass, String filter, Pageable pageable) {
		return objectClassPropertyRepository.findByObjectClassAndPropertyNameContains(objectClass,filter, pageable);
	}
}
