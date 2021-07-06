package ru.mephi3.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Document;
import ru.mephi3.domain.Property;
import ru.mephi3.reposotory.DocumentRepository;
import ru.mephi3.service.DocumentService;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

	private final DocumentRepository documentRepository;

	@Override
	@PreAuthorize("hasAuthority('DOCUMENT_EDIT') or hasAnyRole('ADMIN')")
	public Document save(Document document) {
		Document res = documentRepository.save(document);
		return res;
	}

	@Override
	@PreAuthorize("hasAuthority('DOCUMENT_EDIT') or hasAnyRole('ADMIN')")
	public Document create(String code) {
		Document document = new Document();
		return document;
	}

	@Override
	@PreAuthorize("hasAuthority('DOCUMENT_READ') or hasRole('ADMIN')")
	public Optional<Document> findById(Integer id) {
		Optional<Document> optDocument = documentRepository.findById(id);
		return optDocument;
	}

	@Override
	@PreAuthorize("hasAuthority('DOCUMENT_READ') or hasAnyRole('ADMIN')")
	public Page<Document> findByFilter(String search, Pageable pageable) {
		return documentRepository.findByHeaderContainsIgnoreCase(search, pageable);
	}


	@Override
	@PreAuthorize("hasAuthority('DOCUMENT_READ') or hasAnyRole('ADMIN')")
	public Page<Document> findAll(Pageable pageable) {
		return documentRepository.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('DOCUMENT_EDIT') or hasAnyRole('ADMIN')")
	public void delete(Document document) {
		documentRepository.delete(document);
	}

	@Override
	@PreAuthorize("hasAuthority('DOCUMENT_READ') or hasAnyRole('ADMIN')")
	public Page<Document> findByString(String value, Pageable pageable) {
		return null;
	}
}
