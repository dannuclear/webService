package ru.mephi3.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.ObjectClass;
import ru.mephi3.reposotory.ObjectClassRepository;
import ru.mephi3.service.ObjectClassService;

@Service
@RequiredArgsConstructor
public class ObjectClassServiceImpl implements ObjectClassService {

	private final ObjectClassRepository objectClassRepository;

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_EDIT') or hasAnyRole('ADMIN')")
	public ObjectClass save(ObjectClass objectClass) {
		ObjectClass res = objectClassRepository.save(objectClass);
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_EDIT') or hasAnyRole('ADMIN')")
	public ObjectClass create(String code) {
		ObjectClass objectClass = new ObjectClass();
		return objectClass;
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_READ') or hasRole('ADMIN')")
	public Optional<ObjectClass> findById(Integer id) {
		Optional<ObjectClass> optObjectClass = objectClassRepository.findById(id);
		return optObjectClass;
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_READ') or hasAnyRole('ADMIN')")
	public Page<ObjectClass> findAll(Pageable pageable) {
		return objectClassRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_EDIT') or hasAnyRole('ADMIN')")
	public void delete(ObjectClass objectClass) {
		objectClassRepository.delete(objectClass);
	}

	@Override
	@PreAuthorize("hasAuthority('OBJECT_CLASS_READ') or hasAnyRole('ADMIN')")
	public Page<ObjectClass> findByString(String value, Pageable pageable) {
		return null;
	}

	@Override
	public ObjectClass findByObjectClassname(String objectClassname) {
		return null;
	}

	@Override
	public List<ObjectClass> findByParent(Integer integer) {
		if (integer == null)
			return objectClassRepository.findByParentIdIsNullOrderByOkpdCode();
		return objectClassRepository.findByParentIdOrderByOkpdCode(integer);
	}
	
	@Override
	public String getObjectClassTreeString(Integer leafId){
		return objectClassRepository.getObjectClassTreeString(leafId);
	}
}
