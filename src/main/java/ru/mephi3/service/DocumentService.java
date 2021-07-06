package ru.mephi3.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.mephi3.domain.Document;

public interface DocumentService {

	Document create(String code);

	Document save(Document sample);

	Optional<Document> findById(Integer id);

    Page<Document> findByFilter(String search, Pageable pageable);

    Page<Document> findAll(Pageable pageable);

	Page<Document> findByString(String value, Pageable pageable);

	void delete(Document sample);
}
