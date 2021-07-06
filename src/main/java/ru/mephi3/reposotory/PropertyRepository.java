package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.Property;

@Transactional(readOnly = true)
public interface PropertyRepository extends JpaRepository<Property, Integer> {

	Page<Property> findByNameContainsIgnoreCase(String part, Pageable pageable);
}
