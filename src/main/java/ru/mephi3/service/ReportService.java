package ru.mephi3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mephi3.domain.Report;

import java.util.Optional;

public interface ReportService {

	Report create(String code);

	Report save(Report report);

	Optional<Report> findById(Integer id);

	Page<Report> findAll(Pageable pageable);

	Page<Report> findByString(String value, Pageable pageable);

	void delete(Report report);
}
