package ru.mephi3.service;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.mephi3.domain.Report;
import ru.mephi3.service.exception.ReportCompileException;
import ru.mephi3.service.exception.ReportFillException;
import ru.mephi3.service.exception.ReportIOException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public interface ReportService {

    void saveSourceToFile(InputStream inputStream, String fileName) throws ReportIOException;

    Path saveToTempFile(InputStream is) throws ReportIOException;

    void saveSourceAndCompile(InputStream inputStream, String fileName) throws ReportIOException, ReportCompileException;

    JasperReport getJasperReport(String fileName) throws ReportIOException;

    JasperPrint fillReport(Report report, Map<String, Object> extraParam) throws ReportFillException;

    boolean existsSource (String fileName);
	boolean existsCompiled(String fileName);

    JasperReport compile(InputStream inputStream) throws ReportCompileException;

    Resource getSourceResource(String fileName);

    Resource getCompiledResource(String fileName);

    JasperReport compile(String sourceFileName) throws ReportCompileException;

    Report create(String code);

	Report save(Report report);

	Optional<Report> findById(Integer id);

    @PreAuthorize("hasAuthority('REPORT_READ') or hasRole('ADMIN')")
    InputStream getSourceStream(Report report) throws ReportIOException;

    Page<Report> findAll(Pageable pageable);

	Page<Report> findByString(String value, Pageable pageable);

	Page<Report> findByStringNoLob(String value, Pageable pageable);

	void delete(Report report);
}
