
package ru.mephi3.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.mephi3.domain.Property;

public interface PropertyService {

	Property create(String name);

	Property findByPropertyName(String propertyName);

	Property save(Property property);

	Optional<Property> findById(Integer id);

	Page<Property> findAll(Pageable pageable);
	
	Page<Property> findByFilter(String search, Pageable pageable);

	Page<Property> findByString(String value, Pageable pageable);

	void delete(Property property);
}
