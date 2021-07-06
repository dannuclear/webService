package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.Document;
import ru.mephi3.domain.DocumentType;

import java.util.List;

@Transactional(readOnly = true)
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
    Page<DocumentType> findByNameContainsIgnoreCase(String part, Pageable pageable);

    List<DocumentType> findByIdIn(List<Integer> ids);
}
