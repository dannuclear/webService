package ru.mephi3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.mephi3.domain.DocumentType;

public interface DocumentTypeService {

	DocumentType create(String code);

	DocumentType save(DocumentType documentType);

	Optional<DocumentType> findById(Integer id);

    List<DocumentType> findByIds(List<Integer> ids);

    Page<DocumentType> findAll(Pageable pageable);

	Page<DocumentType> findByString(String value, Pageable pageable);

	void delete(DocumentType documentType);
}
