package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi3.domain.Report;


@Transactional(readOnly = true)
public interface ReportRepository extends JpaRepository<Report, Integer> {
    Page<Report> findByNameContainsIgnoreCase(String string, Pageable pageable);
}
