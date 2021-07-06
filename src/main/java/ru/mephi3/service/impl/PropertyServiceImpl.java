package ru.mephi3.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Property;
import ru.mephi3.reposotory.PropertyRepository;
import ru.mephi3.service.PropertyService;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

	private final PropertyRepository propertyRepository;

	@Override
	@PreAuthorize("hasAuthority('PROPERTY_EDIT') or hasAnyRole('ADMIN')")
	public Property save(Property property) {
		Property res = propertyRepository.save(property);
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('PROPERTY_EDIT') or hasAnyRole('ADMIN')")
	public Property create(String code) {
		Property property = new Property();
		return property;
	}

	@Override
	@PreAuthorize("hasAuthority('PROPERTY_READ') or hasRole('ADMIN')")
	public Optional<Property> findById(Integer id) {
		Optional<Property> optProperty = propertyRepository.findById(id);
		return optProperty;
	}

	@Override
	@PreAuthorize("hasAuthority('PROPERTY_READ') or hasAnyRole('ADMIN')")
	public Page<Property> findAll(Pageable pageable) {
		return propertyRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('PROPERTY_READ') or hasAnyRole('ADMIN')")
	public Page<Property> findByFilter(String search, Pageable pageable) {
		return propertyRepository.findByNameContainsIgnoreCase(search, pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('PROPERTY_EDIT') or hasAnyRole('ADMIN')")
	public void delete(Property property) {
		propertyRepository.delete(property);
	}

	@Override
	@PreAuthorize("hasAuthority('PROPERTY_READ') or hasAnyRole('ADMIN')")
	public Page<Property> findByString(String value, Pageable pageable) {
		return null;
	}

	@Override
	public Property findByPropertyName(String propertyname) {
		return null;
	}
}
