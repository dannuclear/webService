package ru.mephi3.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.mephi3.domain.Report;
import ru.mephi3.reposotory.ReportRepository;
import ru.mephi3.service.ReportService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public Report save(Report report) {
        Report res = reportRepository.save(report);
        return res;
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public Report create(String code) {
        Report report = new Report();
        return report;
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasRole('ADMIN')")
    public Optional<Report> findById(Integer id) {
        Optional<Report> optReport = reportRepository.findById(id);
        return optReport;
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasAnyRole('ADMIN')")
    public Page<Report> findAll(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_EDIT') or hasAnyRole('ADMIN')")
    public void delete(Report report) {
        reportRepository.delete(report);
    }

    @Override
    @PreAuthorize("hasAuthority('REPORT_READ') or hasAnyRole('ADMIN')")
    public Page<Report> findByString(String value, Pageable pageable) {
        return reportRepository.findByNameContainsIgnoreCase(value, pageable);
    }
}
