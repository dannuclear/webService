package ru.mephi3.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.DocumentType;
import ru.mephi3.reposotory.DocumentTypeRepository;
import ru.mephi3.service.DocumentTypeService;

@Service
@RequiredArgsConstructor
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_EDIT') or hasAnyRole('ADMIN')")
    public DocumentType save(DocumentType documentType) {
        DocumentType res = documentTypeRepository.save(documentType);
        return res;
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_EDIT') or hasAnyRole('ADMIN')")
    public DocumentType create(String code) {
        DocumentType documentType = new DocumentType();
        return documentType;
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_READ') or hasRole('ADMIN')")
    public Optional<DocumentType> findById(Integer id) {
        Optional<DocumentType> optDocumentType = documentTypeRepository.findById(id);
        return optDocumentType;
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_READ') or hasRole('ADMIN')")
    public List<DocumentType> findByIds(List<Integer> ids) {
        List<DocumentType> optDocumentType = documentTypeRepository.findByIdIn(ids);
        return optDocumentType;
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_READ') or hasAnyRole('ADMIN')")
    public Page<DocumentType> findAll(Pageable pageable) {
        return documentTypeRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_EDIT') or hasAnyRole('ADMIN')")
    public void delete(DocumentType documentType) {
        documentTypeRepository.delete(documentType);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE_READ') or hasAnyRole('ADMIN')")
    public Page<DocumentType> findByString(String value, Pageable pageable) {
        return documentTypeRepository.findByNameContainsIgnoreCase(value, pageable);
    }
}
